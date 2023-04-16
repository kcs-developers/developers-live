package com.developers.live.mentoring.dto;

import com.developers.live.mentoring.entity.Schedule;
import jakarta.validation.constraints.NotBlank;
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
  private String mentorName;

  @NotNull
  private LocalDateTime start;

  @NotNull
  private LocalDateTime end;

  public Schedule toEntity() {
    return Schedule.builder()
            .mentoringRoomId(mentoringRoomId)
            .mentorId(mentorId)
            .start(start)
            .end(end)
            .build();
  }
}
