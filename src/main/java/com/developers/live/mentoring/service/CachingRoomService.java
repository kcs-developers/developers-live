package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.RoomGetDto;
import com.developers.live.mentoring.entity.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public interface CachingRoomService {

  void removeFirstMentoringRoomList();
  List<RoomGetDto> getAndUpdateFirstCacheStorage();

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
      return null;
    }

    return mentorName;
  }

  default RoomGetDto entityToDto(Room entity) {
    return RoomGetDto.builder()
            .mentoringRoomId(entity.getMentoringRoomId())
            .mentorName(getMentorName(entity.getMentorId()))
            .title(entity.getTitle())
            .description(entity.getDescription())
            .point(entity.getPoint())
            .createdAt(entity.getCreatedAt())
            .build();
  }
}
