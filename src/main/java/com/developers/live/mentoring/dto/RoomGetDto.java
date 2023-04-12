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
public class RoomGetDto {
  private Long mentoringRoomId;
  private String mentorName;
  private String title;
  private String description;
  private LocalDateTime createdAt;
}
