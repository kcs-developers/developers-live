package com.developers.live.developers.mentoring.dto;

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
public class RoomUpdateRequestDto {

  @NotNull
  private Long mentoringRoomId;

  @NotBlank
  private String title;

  @NotBlank
  private String description;
}
