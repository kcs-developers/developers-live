package com.developers.live.mentoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoomListResponseDto {
  private String code;
  private String msg;
  private List<RoomGetDto> data;
}
