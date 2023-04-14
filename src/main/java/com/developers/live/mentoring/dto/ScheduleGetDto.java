package com.developers.live.mentoring.dto;

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
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
