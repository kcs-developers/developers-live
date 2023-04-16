package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.*;
import com.developers.live.mentoring.entity.Schedule;

public interface ScheduleService {

  ScheduleAddResponseDto addSchedule(ScheduleAddRequestDto request);
  ScheduleDeleteResponseDto deleteScheduleAsMentor(Long scheduleId);
  ScheduleDeleteResponseDto deleteScheduleAsMentee(Long scheduleId);
  ScheduleListResponseDto getScheduleListAsMentor(Long memberId);
  ScheduleListResponseDto getScheduleListAsMentee(Long memberId);
  String getMentoringRoomTitle(Long mentoringRoomId);
  MentoringEndResponseDto endMentoring(Long scheduleId);
  ScheduleListResponseDto getMentoringRoomSchedules(Long mentoringRoomId);

  default ScheduleGetDto entityToDto(Schedule entity) {
    return ScheduleGetDto.builder()
            .scheduleId(entity.getScheduleId())
            .mentoringRoomTitle(getMentoringRoomTitle(entity.getMentoringRoomId()))
            .mentorName(entity.getMentorName())
            .menteeName(entity.getMenteeName())
            .startDate(entity.getStart())
            .endDate(entity.getEnd())
            .build();
  }
}
