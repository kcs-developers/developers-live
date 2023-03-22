package com.developers.live.developers.service;

import com.developers.live.developers.mentoring.dto.RoomAddRequestDto;
import com.developers.live.developers.mentoring.dto.RoomAddResponseDto;
import com.developers.live.developers.mentoring.dto.RoomListResponseDto;
import com.developers.live.developers.mentoring.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RoomServiceTest {

  @Autowired RoomService roomService;

  @Test
  void 전체_멘토링룸_조회() {
    // given

    // when
    List<RoomListResponseDto> allRoom = roomService.getList();

    // then
    System.out.println("현재 만들어진 멘토링룸 갯수: " + allRoom.size());
    System.out.println("전체 멘토링룸 조회: " + allRoom);
  }

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

  @Test
  void 검색어로_멘토링룸_조회() {
    // given
    String param = "mentoring";

    // when
    List<RoomListResponseDto> roomList = roomService.getListWithSearch(param);

    // then
    System.out.println(param + " 으로 찾은 멘토링룸 개수: " + roomList.size());
    System.out.println(param + " 으로 찾은 멘토링룸 조회: " + roomList);
  }
}
