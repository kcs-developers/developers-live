package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.*;
import com.developers.live.mentoring.entity.Room;

import java.time.LocalDateTime;

public interface RoomService {

  RoomListResponseDto getFirstList();
  RoomListResponseDto getNextList(LocalDateTime lastDateTime);
  RoomAddResponseDto addRoom(RoomAddRequestDto req);
  RoomUpdateResponseDto updateRoom(RoomUpdateRequestDto req);
  RoomDeleteResponseDto deleteRoom(Long mentoringRoomId);
  RoomListResponseDto getRoomWithSearchingWord(String searchingWord);
  RoomListResponseDto getRoomWithMentorId(Long mentorId);
  RoomListResponseDto getRoomTop10();

  default RoomGetDto entityToDto(Room entity) {
    return RoomGetDto.builder()
            .mentoringRoomId(entity.getMentoringRoomId())
            .mentorName(entity.getMentorName())
            .title(entity.getTitle())
            .description(entity.getDescription())
            .createdAt(entity.getCreatedAt())
            .build();
  }
}
