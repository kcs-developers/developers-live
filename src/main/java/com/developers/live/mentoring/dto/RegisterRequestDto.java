package com.developers.live.mentoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterRequestDto {

  @NotNull
  private Long scheduleId;

  @NotNull
  private Long menteeId;

  @NotBlank
  private String menteeName;
}
