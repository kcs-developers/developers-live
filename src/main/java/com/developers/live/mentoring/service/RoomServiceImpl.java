package com.developers.live.mentoring.service;

import com.developers.live.mentoring.entity.Room;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.repository.RoomRepository;
import com.developers.live.mentoring.repository.ScheduleRepository;
import com.developers.live.mentoring.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  // 밑에서 지정해줬듯이 120000ms(2분)마다 한번씩 scheduler 가 실행되어 첫번째 100개의 데이터를 DB와 맞춘다.
  @Override
  @Scheduled(fixedDelay = 120000)
  public void syncWithData() {
    cachingRoomService.removeFirstMentoringRoomList();
    cachingRoomService.getAndUpdateFirstCacheStorage();
  }

  @Override
  public RoomListResponseDto getFirstCacheList() {
    return RoomListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("첫번째 캐시 저장소에 저장된 데이터")
            .data(cachingRoomService.getAndUpdateFirstCacheStorage())
            .build();
  }

  @Override
  public RoomListResponseDto getNextList(LocalDateTime lastDateTime) {
    List<Room> roomList = roomRepository.findAllByCreatedAtBeforeOrderByCreatedAtDesc(lastDateTime, PageRequest.of(0, 100));
    List<RoomGetDto> result = roomList.stream().map(room -> entityToDto(room)).toList();

    return RoomListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("다음 데이터 불러오기")
            .data(result)
            .build();
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
    // 방 삭제 시 관련 스케쥴도 삭제 처리
    scheduleRepository.deleteAll(scheduleList);

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
