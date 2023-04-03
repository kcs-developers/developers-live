package com.developers.live.developers.service;

import com.developers.live.developers.mentoring.dto.ScheduleAddRequestDto;
import com.developers.live.developers.mentoring.dto.ScheduleAddResponseDto;
import com.developers.live.developers.mentoring.dto.ScheduleListResponseDto;
import com.developers.live.developers.mentoring.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    assertThat(response.getCode()).isEqualTo(String.valueOf(HttpStatus.OK));
  }

  @Test
  public void 멘토_스케쥴_조회() {
    // given
    Long memberId = 1L;

    // when
    ScheduleListResponseDto response = scheduleService.getScheduleListAsMentor(memberId);

    // then
    assertThat(response.getCode()).isEqualTo(String.valueOf(HttpStatus.OK));
  }

  // 멘티 스케쥴 조회
}
