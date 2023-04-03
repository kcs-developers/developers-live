package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.RegisterRequestDto;
import com.developers.live.mentoring.dto.RegisterResponseDto;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegisterServiceImpl implements RegisterService {

  private final RedisTemplate<String, Object> redisTemplate;

  private final ScheduleRepository scheduleRepository;

  @Transactional
  @Override
  public RegisterResponseDto register(RegisterRequestDto request) {
    Optional<Schedule> schedule = scheduleRepository.findById(request.getScheduleId());

    if (schedule.isPresent()) {
      if (schedule.get().getMenteeId() == null) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.setIfAbsent(String.valueOf(request.getScheduleId()), request.getMenteeId());

        Long registeredMentee = Long.parseLong(String.valueOf(valueOperations.get(String.valueOf(request.getScheduleId()))));

        if (request.getMenteeId().equals(registeredMentee)) {
          // TODO: 사용자 측에 포인트 차감 요청
          schedule.get().changeMentee(request.getMenteeId());

          return RegisterResponseDto.builder()
                  .code(String.valueOf(HttpStatus.OK))
                  .msg("정상적으로 신청이 완료되었습니다.")
                  .data(String.valueOf(request.getMenteeId()))
                  .build();
        }
      }
      return RegisterResponseDto.builder()
              .code(String.valueOf(HttpStatus.NOT_FOUND))
              .msg("이미 예약된 멘토링 일정입니다.")
              .data(null)
              .build();
    }
    return RegisterResponseDto.builder()
            .code(HttpStatus.NOT_FOUND.name())
            .msg("해당 스케줄에 대한 정보를 찾지 못했습니다.")
            .data(null)
            .build();
  }
}
