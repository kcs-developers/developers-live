package com.developers.live.mentoring.service;

import com.developers.live.mentoring.entity.Room;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.repository.RoomRepository;
import com.developers.live.mentoring.repository.ScheduleRepository;
import com.developers.live.mentoring.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@EnableCaching
@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

  private final RoomRepository roomRepository;
  private final ScheduleRepository scheduleRepository;
  private final CachingRoomService cachingRoomService;

  // 첫 목록 조회 요청(페이지 방문), 새로고침 등의 상황에서 첫번째, 예비 저장소 모두 새로 받아오도록 한다.
  @Override
  // TODO: fixedDelay 설정 변경 필요
  @Scheduled(fixedDelay = 30000)
  public void initCacheStorage() {
    cachingRoomService.removeFirstMentoringRoomList();
    cachingRoomService.getAndUpdateFirstCacheStorage();
    cachingRoomService.initSpareCacheStorage();
  }

  // 밑에서 지정해줬듯이 180000ms 즉, 3분마다 한번씩 scheduler 가 실행되어 첫번째 데이터만 업데이트 한다.
  // 즉, 3분마다 새로운 방 정보를 받아오고
  @Override
  public RoomListResponseDto getFirstCacheList() {
    return RoomListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("첫번째 캐시 저장소에 저장된 데이터")
            .data(cachingRoomService.getAndUpdateFirstCacheStorage())
            .build();
  }

  @Override
  public RoomListResponseDto getSpareCacheList(LocalDateTime lastDateTime) {
    RoomListResponseDto response = RoomListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("예비 캐시 저장소에 저장된 데이터")
            .data(cachingRoomService.getAndUpdateSpareCacheStorage(lastDateTime))
            .build();

    cachingRoomService.removeSpareMentoringRoomList();
    cachingRoomService.getAndUpdateSpareCacheStorage(lastDateTime);

    return response;
  }

  @Override
  public RoomAddResponseDto addRoom(RoomAddRequestDto req) {
    Room result = roomRepository.save(
            Room.builder()
            .mentorId(req.getMentorId())
            .title(req.getTitle())
            .description(req.getDescription())
            .point(req.getPoint())
            .build()
    );

    return RoomAddResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("정상적으로 채팅방을 생성하였습니다.")
            .data(String.valueOf(result.getMentoringRoomId()))
            .build();
  }

  @Override
  @Transactional
  public RoomUpdateResponseDto updateRoom(RoomUpdateRequestDto req) {
    Optional<Room> optionalRoom = roomRepository.findById(req.getMentoringRoomId());

    RoomUpdateResponseDto response;
    if (optionalRoom.isPresent()) {
      Room room = optionalRoom.get();

      room.updateRoomInfo(req.getTitle().replaceAll("\\s+", " "), req.getDescription());

      response = RoomUpdateResponseDto.builder()
              .code(HttpStatus.OK.toString())
              .msg("방 정보 수정이 완료되었습니다.")
              .data(String.valueOf(req.getMentoringRoomId()))
              .build();
    }
    else {
      response = RoomUpdateResponseDto.builder()
              .code(HttpStatus.NOT_FOUND.toString())
              .msg("수정하려는 방의 정보를 찾을 수 없습니다.")
              .data(null)
              .build();
    }
    return response;
  }

  // 멘토링룸의 스케쥴을 모두 가져와 menteeId가 할당된 데이터가 있다면 에러를 반환하도록 한다.
  @Override
  public RoomDeleteResponseDto deleteRoom(Long mentoringRoomId) {
    List<Schedule> scheduleList = scheduleRepository.findAllByMentoringRoomId(mentoringRoomId);

    for (Schedule schedule : scheduleList) {
      if (schedule.getMenteeId() != null) {
        return RoomDeleteResponseDto.builder()
                .code(HttpStatus.BAD_REQUEST.toString())
                .msg("아직 멘티가 잡혀있는 일정이 있기에 삭제가 불가능합니다.")
                .data(null)
                .build();
      }
    }

    roomRepository.deleteById(mentoringRoomId);

    return RoomDeleteResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("정상적으로 멘토링룸 삭제가 완료되었습니다.")
            .data(String.valueOf(mentoringRoomId))
            .build();
  }

  // 입력받은 검색어의 1개 이상 공백을 감지해 공백 1개로 치환한 후 방 검색 결과 리스트를 반환한다.
  @Override
  public RoomListResponseDto getRoomWithSearchingWord(String searchingWord) {
    List<Room> roomList = roomRepository.findByTitleContaining(searchingWord.replaceAll("\\s+", " "));
    List<RoomGetDto> dtoList = roomList.stream().map(room -> entityToDto(room)).toList();

    return RoomListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("검색어로 멘토링룸 목록 조회가 완료되었습니다.")
            .data(dtoList)
            .build();
  }
}
