package com.developers.live.mentoring.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ScheduleAddRequestDto {
  @NotNull
  private Long mentoringRoomId;

  @NotNull
  private Long mentorId;

  @NotNull
  private LocalDateTime start;

  @NotNull
  private LocalDateTime end;
}
