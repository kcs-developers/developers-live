package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.*;
import com.developers.live.developers.mentoring.entity.Schedule;
import com.developers.live.mentoring.dto.*;

public interface ScheduleService {

  ScheduleAddResponseDto addSchedule(ScheduleAddRequestDto request);
  ScheduleDeleteResponseDto deleteScheduleAsMentor(Long scheduleId);
  ScheduleDeleteResponseDto deleteScheduleAsMentee(Long scheduleId);
  ScheduleListResponseDto getScheduleListAsMentor(Long memberId);
  ScheduleListResponseDto getScheduleListAsMentee(Long memberId);
  String getMentoringRoomTitle(Long mentoringRoomId);
  MentoringEndResponseDto endMentoring(Long scheduleId);

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
