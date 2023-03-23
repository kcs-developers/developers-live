package com.developers.live.developers.mentoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ScheduleGetDto {
  private Long scheduleId;
  private String mentoringRoomTitle;
  private String mentorName;
  private String menteeName;
  private LocalDateTime start;
  private LocalDateTime end;
}
