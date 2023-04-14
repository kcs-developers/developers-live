package com.developers.live.mentoring.service;

import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

public interface ScheduleService {

  ScheduleAddResponseDto addSchedule(ScheduleAddRequestDto request);
  ScheduleDeleteResponseDto deleteScheduleAsMentor(Long scheduleId);
  ScheduleDeleteResponseDto deleteScheduleAsMentee(Long scheduleId);
  ScheduleListResponseDto getScheduleListAsMentor(Long memberId);
  ScheduleListResponseDto getScheduleListAsMentee(Long memberId);
  String getMentoringRoomTitle(Long mentoringRoomId);
  MentoringEndResponseDto endMentoring(Long scheduleId);
  ScheduleListResponseDto getMentoringRoomSchedules(Long mentoringRoomId);

  RestTemplate restTemplate = new RestTemplate();

  default String getMentorName(Long mentorId) {
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

    return (String) response.getBody().get("memberName");
  }

  default String getMemberName(Long memberId) {
    // TODO: 멘토 정보 조회 중 예외 처리
    // TODO: 추후 uri 변경 필요
    URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:9000")
            .path("/api/member")
            .queryParam("memberId", memberId)
            .encode()
            .build()
            .toUri();
    ResponseEntity<Map> response = restTemplate.getForEntity(uri, Map.class);

    String memberName = null;
    try {
      memberName = (String) response.getBody().get("memberName");
    } catch (HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException e) {
      e.printStackTrace();
    }

    return memberName;
  }

  default ScheduleGetDto entityToDto(Schedule entity) {
    return ScheduleGetDto.builder()
            .scheduleId(entity.getScheduleId())
            .mentoringRoomTitle(getMentoringRoomTitle(entity.getMentoringRoomId()))
            .mentorName(getMentorName(entity.getMentorId()))
            .menteeName(entity.getMenteeId() == null ? null : getMemberName(entity.getMenteeId()))
            .startDate(entity.getStart())
            .endDate(entity.getEnd())
            .build();
  }
}
