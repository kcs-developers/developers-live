package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.RoomAddRequestDto;
import com.developers.live.developers.mentoring.dto.RoomAddResponseDto;
import com.developers.live.developers.mentoring.dto.RoomListResponseDto;
import com.developers.live.developers.mentoring.entity.Room;

import java.util.List;

public interface RoomService {

  List<RoomListResponseDto> getList();

  RoomAddResponseDto addRoom(RoomAddRequestDto req);

  List<RoomListResponseDto> getListWithSearch(String param);

  default RoomListResponseDto entityToDto(Room entity) {
    return RoomListResponseDto.builder()
            .mentoringRoomId(entity.getMentoringRoomId())

            // TODO: entity의 mentorId 데이터로 mentor 이름 가져오기
            .mentorName("")
            .title(entity.getTitle())
            .description(entity.getDescription())
            .point(entity.getPoint())
            .build();
  }
}
