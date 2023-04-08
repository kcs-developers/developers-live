package com.developers.live.service;

import com.developers.live.mentoring.dto.*;
import com.developers.live.mentoring.entity.Room;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.repository.RoomRepository;
import com.developers.live.mentoring.repository.ScheduleRepository;
import com.developers.live.mentoring.service.ScheduleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

  @Mock RedisTemplate redisTemplate;

  @Mock RoomRepository roomRepository;

  @Mock ScheduleRepository scheduleRepository;

  @InjectMocks ScheduleServiceImpl scheduleService;

  @Test
  public void 스케쥴_추가() {
    // given
    Long mentoringRoomId = 1L;
    Room room = Room.builder()
            .mentorId(1L)
            .title("방제")
            .description("방 소개")
            .build();
    ScheduleAddRequestDto req = ScheduleAddRequestDto.builder()
            .mentoringRoomId(mentoringRoomId)
            .mentorId(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    Schedule schedule = req.toEntity();
    when(roomRepository.findById(mentoringRoomId)).thenReturn(Optional.of(room));
    when(scheduleRepository.save(any())).thenReturn(schedule);

    // when
    ScheduleAddResponseDto response = scheduleService.addSchedule(req);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("일정이 추가되었습니다.");
    assertThat(response.getData()).isInstanceOf(String.class);
  }

  @Test
  public void 멘토_스케쥴_삭제() {
    // given
    Long scheduleId = 1L;
    Schedule schedule = Schedule.builder()
            .mentoringRoomId(1L)
            .mentorId(1L)
            .start(LocalDateTime.now())
            .start(LocalDateTime.now().plusHours(1))
            .build();
    when(scheduleRepository.findById(any())).thenReturn(Optional.of(schedule));

    // when
    ScheduleDeleteResponseDto response = scheduleService.deleteScheduleAsMentor(scheduleId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("정상적으로 멘토링 일정이 취소되었습니다.");
    assertThat(response.getData()).isInstanceOf(String.class);
  }

  @Test
  public void 멘티의_멘토링_취소() {
    // given
    Long scheduleId = 1L;
    Schedule schedule = Schedule.builder()
            .mentoringRoomId(1L)
            .mentorId(1L)
            .menteeId(2L)
            .start(LocalDateTime.now())
            .start(LocalDateTime.now().plusHours(1))
            .build();
    when(scheduleRepository.findById(any())).thenReturn(Optional.of(schedule));

    // when
    ScheduleDeleteResponseDto response = scheduleService.deleteScheduleAsMentee(scheduleId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("정상적으로 신청이 취소되었습니다.");
    assertThat(response.getData()).isInstanceOf(String.class);
  }

  @Test
  public void 멘토_스케쥴_조회() {
    // given
    Long memberId = 1L;
    List<Schedule> scheduleList = new ArrayList<>();
    Schedule schedule1 = Schedule.builder()
            .mentoringRoomId(1L)
            .mentorId(memberId)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    Schedule schedule2 = Schedule.builder()
            .mentoringRoomId(2L)
            .mentorId(memberId)
            .menteeId(2L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    scheduleList.add(schedule1);
    scheduleList.add(schedule2);
    when(scheduleRepository.findAllByMentorId(any())).thenReturn(scheduleList);

    // when
    ScheduleListResponseDto response = scheduleService.getScheduleListAsMentor(memberId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("멘토로서의 일정 조회가 완료되었습니다.");
    assertThat(response.getData().get(0)).isInstanceOf(ScheduleGetDto.class);
  }

  @Test
  public void 멘티_스케쥴_조회() {
    // given
    Long memberId = 1L;
    List<Schedule> scheduleList = new ArrayList<>();
    Schedule schedule1 = Schedule.builder()
            .mentoringRoomId(1L)
            .mentorId(1L)
            .menteeId(memberId)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    Schedule schedule2 = Schedule.builder()
            .mentoringRoomId(2L)
            .mentorId(1L)
            .menteeId(memberId)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    scheduleList.add(schedule1);
    scheduleList.add(schedule2);
    when(scheduleRepository.findAllByMenteeId(any())).thenReturn(scheduleList);

    // when
    ScheduleListResponseDto response = scheduleService.getScheduleListAsMentee(memberId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("멘티로서의 일정 조회가 완료되었습니다.");
    assertThat(response.getData().get(0)).isInstanceOf(ScheduleGetDto.class);
  }

  @Test
  public void 멘토링_종료_후_DB_정리() {
    // given
    Long scheduleId = 1L;
    Schedule schedule = Schedule.builder()
            .mentoringRoomId(2L)
            .mentorId(1L)
            .menteeId(2L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    when(scheduleRepository.findById(any())).thenReturn(Optional.of(schedule));

    // when
    MentoringEndResponseDto response = scheduleService.endMentoring(scheduleId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("성공적으로 멘토링 정보가 정리되었습니다.");
    assertThat(response.getData()).isInstanceOf(String.class);
  }
}
