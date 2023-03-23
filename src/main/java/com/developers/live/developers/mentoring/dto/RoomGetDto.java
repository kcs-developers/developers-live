package com.developers.live.developers.mentoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoomGetDto {
  private Long mentoringRoomId;
  private String mentorName;
  private String title;
  private String description;
  private Long point;
}
