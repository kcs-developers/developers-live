package com.developers.live.service;

import com.developers.live.mentoring.dto.*;
import com.developers.live.mentoring.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

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
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
  }

  @Test
  void 멘토링룸_정보_수정() {
    // given
    RoomUpdateRequestDto req = RoomUpdateRequestDto.builder()
            .mentoringRoomId(1L)
            .title("방제 수정이요!")
            .description("내용 수정이요!")
            .build();

    // when
    RoomUpdateResponseDto response = roomService.updateRoom(req);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
  }

  @Test
  void 멘토링룸_삭제() {
    // given
    Long mentoringRoomId = 1L;

    // when
    RoomDeleteResponseDto response = roomService.deleteRoom(mentoringRoomId);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
  }

  @Test
  void 멘토링룸_조회() {
    // given

    // when
    RoomListResponseDto response = roomService.getFirstCacheList();

    // then
    System.out.println(response.getData());
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
  }

  @Test
  void 날짜데이터로_멘토링룸_조회() {
    // given
    List<RoomGetDto> roomList = roomService.getFirstCacheList().getData();
    LocalDateTime lastDateTime = roomList.get(10).getCreatedAt();

    // when
    RoomListResponseDto response = roomService.getNextList(lastDateTime);

    // then
    System.out.println(response);
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getData().get(0).getCreatedAt()).isBefore(lastDateTime);
  }
}
