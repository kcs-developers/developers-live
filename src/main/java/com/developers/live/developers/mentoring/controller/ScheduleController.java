package com.developers.live.developers.mentoring.controller;

import com.developers.live.developers.mentoring.dto.ScheduleAddRequestDto;
import com.developers.live.developers.mentoring.dto.ScheduleAddResponseDto;
import com.developers.live.developers.mentoring.dto.ScheduleDeleteResponseDto;
import com.developers.live.developers.mentoring.dto.ScheduleListResponseDto;
import com.developers.live.developers.mentoring.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

  private final ScheduleService scheduleService;

  @PostMapping("")
  public ResponseEntity<ScheduleAddResponseDto> scheduleAdd(@RequestBody @Valid ScheduleAddRequestDto request) {
    ScheduleAddResponseDto response = scheduleService.addSchedule(request);
    log.info("멘토링룸에 대한 일정 추가: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<ScheduleDeleteResponseDto> scheduleDelete(@PathVariable Long scheduleId) {
    ScheduleDeleteResponseDto response = scheduleService.deleteSchedule(scheduleId);
    log.info("일정 삭제: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{memberId}/mentor")
  public ResponseEntity<ScheduleListResponseDto> scheduleAsMentor(@PathVariable Long memberId) {
    ScheduleListResponseDto response = scheduleService.getScheduleListAsMentor(memberId);
    log.info("사용자의 멘토로서의 모든 일정 조회: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{memberId}/mentee")
  public ResponseEntity<ScheduleListResponseDto> scheduleAsMentee(@PathVariable Long memberId) {
    ScheduleListResponseDto response = scheduleService.getScheduleListAsMentee(memberId);
    log.info("사용자의 멘티로서의 모든 일정 조회: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
