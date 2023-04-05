package com.developers.live.service;

import com.developers.live.mentoring.dto.ScheduleAddRequestDto;
import com.developers.live.mentoring.dto.ScheduleAddResponseDto;
import com.developers.live.mentoring.dto.ScheduleDeleteResponseDto;
import com.developers.live.mentoring.dto.ScheduleListResponseDto;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;

@SpringBootTest
public class ScheduleServiceTest {

  @Autowired ScheduleService scheduleService;

  @Test
  public void 스케쥴_추가() {
    // given
    ScheduleAddRequestDto req = ScheduleAddRequestDto.builder()
            .mentorId(1L)
            .mentoringRoomId(2L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();

    // when
    ScheduleAddResponseDto response = scheduleService.addSchedule(req);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
  }

  @Test
  public void 멘토_스케쥴_삭제() {
    // given
    ScheduleListResponseDto scheduleList = scheduleService.getScheduleListAsMentor(1L);
    int lastDataIndex = scheduleList.getData().size() - 1;
    long scheduleId = scheduleList.getData().get(lastDataIndex).getScheduleId();

    // when
    ScheduleDeleteResponseDto response = scheduleService.deleteScheduleAsMentor(scheduleId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
  }

  @Test
  public void 멘티의_멘토링_취소() {
    // given
    ScheduleListResponseDto scheduleList = scheduleService.getScheduleListAsMentee(3L);
    int lastDataIndex = scheduleList.getData().size() - 1;
    long scheduleId = scheduleList.getData().get(lastDataIndex).getScheduleId();

    // when
    ScheduleDeleteResponseDto response = scheduleService.deleteScheduleAsMentee(scheduleId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
  }

  @Test
  public void 멘토_스케쥴_조회() {
    // given
    Long memberId = 1L;

    // when
    ScheduleListResponseDto response = scheduleService.getScheduleListAsMentor(memberId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
  }

  @Test
  public void 멘티_스케쥴_조회() {
    // given
    Long menteeId = 3L;

    // when
    ScheduleListResponseDto response = scheduleService.getScheduleListAsMentee(menteeId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
  }
}
