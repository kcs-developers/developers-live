package com.developers.live.mentoring.service;

import com.developers.live.mentoring.entity.Room;
import com.developers.live.mentoring.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

public interface RoomService {

  void syncWithData();
  RoomListResponseDto getFirstCacheList();
  RoomListResponseDto getNextList(LocalDateTime lastDateTime);
  RoomAddResponseDto addRoom(RoomAddRequestDto req);
  RoomUpdateResponseDto updateRoom(RoomUpdateRequestDto req);
  RoomDeleteResponseDto deleteRoom(Long mentoringRoomId);
  RoomListResponseDto getRoomWithSearchingWord(String searchingWord);

  default String getMentorName(Long mentorId) {
    RestTemplate restTemplate = new RestTemplate();

    // TODO: 멘토 정보 조회 중 예외 처리
    // TODO: 추후 uri 변경 필요
    URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:9000")
            .path("/api/mentor")
            .queryParam("mentorId", mentorId)
            .encode()
            .build()
            .toUri();

    ResponseEntity<Map> response = restTemplate.getForEntity(uri, Map.class);

    String mentorName = null;
    try {
      mentorName = (String) response.getBody().get("memberName");
    } catch (HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException e) {
      e.printStackTrace();
    }

    return mentorName;
  }

  default RoomGetDto entityToDto(Room entity) {
    return RoomGetDto.builder()
            .mentoringRoomId(entity.getMentoringRoomId())
            .mentorName(getMentorName(entity.getMentorId()))
            .title(entity.getTitle())
            .description(entity.getDescription())
            .createdAt(entity.getCreatedAt())
            .build();
  }
}
