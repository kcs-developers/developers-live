package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.RoomAddRequestDto;
import com.developers.live.developers.mentoring.dto.RoomAddResponseDto;
import com.developers.live.developers.mentoring.dto.RoomGetDto;
import com.developers.live.developers.mentoring.dto.RoomListResponseDto;
import com.developers.live.developers.mentoring.entity.Room;

import java.util.List;

public interface RoomService {

  RoomListResponseDto getList();
  RoomAddResponseDto addRoom(RoomAddRequestDto req);
  RoomListResponseDto getListWithSearch(String param);
  RoomListResponseDto getListWithRecent();

  default RoomGetDto entityToDto(Room entity) {
    return RoomGetDto.builder()
            .mentoringRoomId(entity.getMentoringRoomId())

            // TODO: entity의 mentorId 데이터로 mentor 이름 가져오기
            .mentorName("")
            .title(entity.getTitle())
            .description(entity.getDescription())
            .point(entity.getPoint())
            .build();
  }
}
