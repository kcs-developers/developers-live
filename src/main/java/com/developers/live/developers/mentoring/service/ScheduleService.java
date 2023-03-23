package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.*;
import com.developers.live.developers.mentoring.entity.Schedule;

public interface ScheduleService {

  ScheduleAddResponseDto addSchedule(ScheduleAddRequestDto request);
  ScheduleDeleteResponseDto deleteSchedule(Long scheduleId);
  ScheduleListResponseDto getScheduleListAsMentor(Long memberId);
  ScheduleListResponseDto getScheduleListAsMentee(Long memberId);
  String getMentoringRoomTitle(Long mentoringRoomId);

  default ScheduleGetDto entityToDto(Schedule entity) {
    return ScheduleGetDto.builder()
            .scheduleId(entity.getScheduleId())
            .mentoringRoomTitle(getMentoringRoomTitle(entity.getMentoringRoomId()))
            // TODO: entity의 mentorId, menteeId 데이터로 mentorName, menteeName 가져오기
            .mentorName("")
            .menteeName("")
            .start(entity.getStart())
            .end(entity.getEnd())
            .build();
  }
}
