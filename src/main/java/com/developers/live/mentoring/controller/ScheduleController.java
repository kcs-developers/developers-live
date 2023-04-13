package com.developers.live.mentoring.controller;

import com.developers.live.mentoring.dto.*;
import com.developers.live.mentoring.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
public class ScheduleController {

  private final ScheduleService scheduleService;

  @PostMapping
  public ResponseEntity<ScheduleAddResponseDto> scheduleAdd(@RequestBody @Valid ScheduleAddRequestDto request) {
    ScheduleAddResponseDto response = scheduleService.addSchedule(request);
    log.info("멘토링룸에 대한 일정 추가: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/mentor/{scheduleId}")
  public ResponseEntity<ScheduleDeleteResponseDto> scheduleDeleteAsMentor(@PathVariable Long scheduleId) {
    ScheduleDeleteResponseDto response = scheduleService.deleteScheduleAsMentor(scheduleId);
    log.info("일정 삭제: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/mentee/{scheduleId}")
  public ResponseEntity<ScheduleDeleteResponseDto> scheduleDeleteAsMentee(@PathVariable Long scheduleId) {
    ScheduleDeleteResponseDto response = scheduleService.deleteScheduleAsMentee(scheduleId);
    log.info("일정 삭제: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/mentor/{memberId}")
  public ResponseEntity<ScheduleListResponseDto> scheduleAsMentor(@PathVariable Long memberId) {
    ScheduleListResponseDto response = scheduleService.getScheduleListAsMentor(memberId);
    log.info("사용자의 멘토로서의 모든 일정 조회: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/mentee/{memberId}")
  public ResponseEntity<ScheduleListResponseDto> scheduleAsMentee(@PathVariable Long memberId) {
    ScheduleListResponseDto response = scheduleService.getScheduleListAsMentee(memberId);
    log.info("사용자의 멘티로서의 모든 일정 조회: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/end/{scheduleId}")
  public ResponseEntity<MentoringEndResponseDto> endMentoring(@PathVariable Long scheduleId) {
    MentoringEndResponseDto response = scheduleService.endMentoring(scheduleId);
    log.info("회의 종료 후 관련 데이터 정리 요청 API: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{mentoringRoomId}")
  public ResponseEntity<ScheduleListResponseDto> getMentoringRoomSchedules(@PathVariable Long mentoringRoomId) {
    ScheduleListResponseDto response = scheduleService.getMentoringRoomSchedules(mentoringRoomId);
    log.info("신청 가능한 스케쥴 조회 API: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
