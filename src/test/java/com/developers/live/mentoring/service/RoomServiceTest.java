package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.*;
import com.developers.live.mentoring.entity.Room;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.repository.RoomRepository;
import com.developers.live.mentoring.repository.ScheduleRepository;
import com.developers.live.mentoring.service.CachingRoomService;
import com.developers.live.mentoring.service.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

  @Mock ScheduleRepository scheduleRepository;

  @Mock CachingRoomService cachingRoomService;

  @Mock RoomRepository roomRepository;

  @InjectMocks RoomServiceImpl roomService;

  @Test
  void 멘토링룸_생성() {
    // given
    RoomAddRequestDto req = RoomAddRequestDto.builder()
            .mentorId(1L)
            .title("mentoring room")
            .description("contents is ...")
            .build();
    Room room = req.toEntity();
    when(roomRepository.save(any())).thenReturn(room);

    // when
    RoomAddResponseDto response = roomService.addRoom(req);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("정상적으로 채팅방을 생성하였습니다.");
    assertThat(response.getData()).isInstanceOf(String.class);
  }

  @Test
  void 멘토링룸_정보_수정() {
    // given
    RoomUpdateRequestDto req = RoomUpdateRequestDto.builder()
            .mentoringRoomId(1L)
            .title("방제 수정이요!")
            .description("내용 수정이요!")
            .build();
    Room room = Room.builder()
            .mentorId(1L)
            .title("방제")
            .description("방 소개")
            .build();
    when(roomRepository.findById(any())).thenReturn(Optional.of(room));

    // when
    RoomUpdateResponseDto response = roomService.updateRoom(req);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("방 정보 수정이 완료되었습니다.");
    assertThat(response.getData()).isInstanceOf(String.class);
  }

  @Test
  void 멘토링룸_삭제() {
    // given
    Long mentoringRoomId = 1L;
    List<Schedule> scheduleList = new ArrayList<>();
    Schedule schedule1 = Schedule.builder()
            .mentoringRoomId(mentoringRoomId)
            .mentorId(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    Schedule schedule2 = Schedule.builder()
            .mentoringRoomId(mentoringRoomId)
            .mentorId(1L)
            /* 에러
            .menteeId(2L)
             */
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    scheduleList.add(schedule1);
    scheduleList.add(schedule2);
    when(scheduleRepository.findAllByMentoringRoomId(any())).thenReturn(scheduleList);

    // when
    RoomDeleteResponseDto response = roomService.deleteRoom(mentoringRoomId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("정상적으로 멘토링룸 삭제가 완료되었습니다.");
    assertThat(response.getData()).isInstanceOf(String.class);
  }

  @Test
  void 멘토링룸_조회() {
    // given
    List<Room> roomList = new ArrayList<>();
    Room room1 = Room.builder()
            .mentorId(1L)
            .title("첫번째 방")
            .description("첫번째 방입니다.")
            .build();
    Room room2 = Room.builder()
            .mentorId(1L)
            .title("두번째 방")
            .description("두번째 방입니다.")
            .build();
    roomList.add(room1);
    roomList.add(room2);
    List<RoomGetDto> dtoList = roomList.stream().map(room -> roomService.entityToDto(room)).toList();
    when(cachingRoomService.getAndUpdateFirstCacheStorage()).thenReturn(dtoList);

    // when
    RoomListResponseDto response = roomService.getFirstCacheList();

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("첫번째 캐시 저장소에 저장된 데이터");
    assertThat(response.getData().get(0)).isInstanceOf(RoomGetDto.class);
  }
}
