package com.developers.live.service;

import com.developers.live.mentoring.dto.RoomAddRequestDto;
import com.developers.live.mentoring.dto.RoomAddResponseDto;
import com.developers.live.mentoring.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RoomServiceTest {

  @Autowired RoomService roomService;

  @Test
  void 멘토링룸_생성() {
    // given
    RoomAddRequestDto req = RoomAddRequestDto.builder()
            .mentorId(1L)
            .title("mentoring room")
            .description("contents is ...")
            .point(10L)
            .build();

    // when
    RoomAddResponseDto response = roomService.addRoom(req);

    // then
    assertThat(response.getCode()).isEqualTo(String.valueOf(HttpStatus.OK));
  }
}
