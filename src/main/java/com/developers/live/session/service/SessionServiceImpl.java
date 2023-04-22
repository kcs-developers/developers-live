package com.developers.live.session.service;

import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.repository.ScheduleRepository;
import com.developers.live.session.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class SessionServiceImpl implements SessionService {

    private final StringRedisTemplate stringRedisTemplate; // 정확하게 문자열로 저장
    private final ScheduleRepository scheduleRepository; // 스케쥴에 해당하는 사용자 확인
    private final DailyCoServiceImpl dailyCoService; //dailyco 서버에서 요청 처리

    @Override
    public SessionRedisSaveResponse enter(SessionRedisSaveRequest request) {
        String roomName = request.getRoomName();
        String userName = request.getUserName();
        Long expireTime = request.getTime();
        String roomUrl;

        // 스케쥴 기반으로, 스케쥴에 해당 mentee가 등록된 사용자인지 여부 체크
        Optional<Schedule> schedule = scheduleRepository.findById(request.getScheduleId());
        if (schedule.isPresent()) {
            if (schedule.get().getMentorId().equals(request.getUserId())) {
                log.info(stringRedisTemplate.opsForHash().get("rooms", roomName));
                roomUrl = String.valueOf(stringRedisTemplate.opsForHash().get("rooms", roomName));
                log.info(roomName+roomUrl);
                if (roomUrl == null) {
                    roomUrl = dailyCoService.create();
                }
            } else if (schedule.get().getMenteeId().equals(request.getUserId())) {
                roomUrl = String.valueOf(stringRedisTemplate.opsForHash().get("rooms", roomName));
                log.info(roomName+roomUrl);
                if (roomUrl == null) {
                    log.error("멘토가 방을 아직 생성하지 않았습니다!");
                    throw new InvalidDataAccessApiUsageException("멘토가 방을 아직 생성하지 않았습니다!");
                }
            } else {
                log.error("스케쥴에 예약한 사용자만 입장할 수 있습니다! 요청한 사용자: " + request.getUserName());
                throw new InvalidDataAccessApiUsageException(request.getUserName() + " 사용자는 해당 방에 입장할 수 없습니다");
            }

            return getSessionRedisSaveResponse(roomName, userName, roomUrl, expireTime);
        } else {
            log.error("스케쥴 정보 오류!");
            throw new InvalidDataAccessApiUsageException("해당 일정은 존재하지 않습니다!");
        }
    }

    private SessionRedisSaveResponse getSessionRedisSaveResponse(String roomName, String userName, String roomUrl, Long expireTime) {
        try {
            // 1. Redis 데이터 삽입 로직 수행
            stringRedisTemplate.opsForSet().add(roomName, userName);
            stringRedisTemplate.expire(roomName, Duration.ofMinutes(expireTime));
            stringRedisTemplate.opsForHash().put("rooms", roomName, roomUrl);

            // 2. 삽입한 데이터 클라이언트에 전달
            SessionRedisSaveResponse response = SessionRedisSaveResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .msg(roomUrl+"경로로 "+roomName+"에 "+userName+"가 들어왔습니다")
                    .room(roomName)
                    .name(userName)
                    .url(roomUrl).build();
            log.info("Redis 세션 저장! " + roomName + "에 " + userName + "저장!");
            return response;
        } catch (Exception e) {
            log.error("Redis 세션 저장 오류! 방 정보: " + roomName + " 사용자 정보: " + userName, e);
            throw new RedisException("Redis 세션 저장에 요류가 발생하였습니다. ", e);
        }
    }

    @Override
    public SessionRedisFindAllResponse list() {
        try {
            // 1. Redis에서 모든 채팅방 정보를 가져온다.
            Set<Object> rooms = stringRedisTemplate.opsForHash().keys("rooms");
            Map<Object, Object> roomUrl = new HashMap();
            Map<String, Set<String>> roomUsers = new HashMap<>();

            for (Object room : rooms) {
                String roomName = room.toString();
                String url = stringRedisTemplate.opsForHash().get("rooms", roomName).toString();
                Set<String> users = stringRedisTemplate.opsForSet().members(roomName);

                roomUrl.put(roomName, url);
                roomUsers.put(roomName, users);
            }



            if (rooms == null || rooms.isEmpty()) {
                log.error("Redis 세션 전체 출력 오류! ");
                throw new InvalidDataAccessApiUsageException("현재 Redis 세션이 없습니다. ");
            }

            // Redis 반환 값 객체 변환
            ObjectMapper mapper = new ObjectMapper();
            String urlData = mapper.writeValueAsString(roomUrl);
            String userData = mapper.writeValueAsString(roomUsers);


            // 2. Redis에 있는 모든 채팅방 정보를 응답해야 한다.
            SessionRedisFindAllResponse response = SessionRedisFindAllResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .msg("정상적으로 처리되었습니다.")
                    .urls(urlData)
                    .users(userData)
                    .build();
            log.info("Redis 세션 불러오기 완료!");
            return response;
        } catch (RedisException e) {
            log.error("Redis 세션 전체 출력 오류! ", e);
            throw new IllegalArgumentException("Redis 세션 전체를 불러오는데 오류가 발생했습니다. ", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SessionRedisRemoveResponse remove(SessionRedisRemoveRequest request) {
        String roomName = request.getRoomName();

        Optional<Schedule> schedule = scheduleRepository.findById(request.getScheduleId());
        if (schedule.isPresent()) {
            if (schedule.get().getMentorId().equals(request.getUserId())) {
                try {
                    if (!stringRedisTemplate.opsForHash().hasKey("rooms", roomName)) {
                        log.error("Redis 세션 삭제 오류! ", roomName);
                        throw new IllegalArgumentException("Redis에서 해당 세션은 존재하지 않습니다. ");
                    }
                    // 1. Redis에 request.getRoomId()를 가지고 가서 해당하는 데이터 삭제
                    stringRedisTemplate.opsForHash().delete("rooms", roomName);

                    // 2. 삭제한 데이터
                    SessionRedisRemoveResponse response = SessionRedisRemoveResponse.builder()
                            .code(HttpStatus.OK.toString())
                            .msg("정상적으로 처리되었습니다.")
                            .data(String.valueOf(stringRedisTemplate.delete(roomName)))
                            .build();
                    log.info("Redis 세션 삭제 완료! " + roomName + "에 대한 세션 삭제!");

                    dailyCoService.delete(request.getRoomUUID());

                    return response;
                } catch (Exception e) {
                    log.error("Redis 세션 삭제 오류! ", e);
                    throw new IllegalArgumentException("Redis에서 세션을 삭제하는데 오류가 발생했습니다. ", e);
                }
            } else {
                log.error("멘토 방 삭제 오류!");
                throw new IllegalArgumentException("멘토만 방을 삭제할 수 있습니다!");
            }
        } else {
            log.error("스케쥴 정보 오류!");
            throw new InvalidDataAccessApiUsageException("해당 일정은 존재하지 않습니다!");
        }
    }
}
