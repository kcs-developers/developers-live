package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.RoomGetDto;
import com.developers.live.mentoring.entity.Room;

import java.util.List;

public interface CachingRoomService {

  void removeFirstMentoringRoomList();
  List<RoomGetDto> getAndUpdateFirstCacheStorage();

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
