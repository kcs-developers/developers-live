package com.developers.live.mentoring.controller;

import com.developers.live.mentoring.controller.RoomController;
import com.developers.live.mentoring.dto.*;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.service.RoomServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = RoomController.class)
@WithMockUser("USER")
public class RoomControllerTest {
  // 내가 생성한 메서드에 관해서만 @MockBean 사용
  @Autowired ObjectMapper objectMapper;

  @Autowired private MockMvc mockMvc;

  @MockBean RoomServiceImpl roomService;

  private static final String BASE_URL = "/api/room";

  @Test
  void getFirstList() throws Exception {
    // given
    List<RoomGetDto> roomList = new ArrayList<>();
    RoomGetDto room1 = RoomGetDto.builder()
            .mentoringRoomId(1L)
            .mentorName("김멘토")
            .title("첫번째 방")
            .description("첫번째 방입니다.")
            .createdAt(LocalDateTime.now())
            .build();
    RoomGetDto room2 = RoomGetDto.builder()
            .mentoringRoomId(2L)
            .mentorName("이멘토")
            .title("두번째 방")
            .description("두번째 방입니다.")
            .createdAt(LocalDateTime.now())
            .build();
    roomList.add(room1);
    roomList.add(room2);
    RoomListResponseDto response = RoomListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("첫번째 캐시 저장소에 저장된 데이터")
            .data(roomList)
            .build();
    given(roomService.getFirstList()).willReturn(response);

    // when
    mockMvc.perform(get(BASE_URL).with(csrf()))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("room/list",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(roomService, times(1)).getFirstList();
  }

  @Test
  void addRoom() throws Exception {
    // given
    Long mentorId = 1L;
    String mentorName = "김멘토";
    String title = "방 제목";
    String description = "방 소개글";

    RoomAddResponseDto response = RoomAddResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("정상적으로 채팅방을 생성하였습니다.")
            .data("1")
            .build();

    given(roomService.addRoom(any())).willReturn(response);

    RoomAddRequestDto request = RoomAddRequestDto.builder()
            .mentorId(mentorId)
            .mentorName(mentorName)
            .title(title)
            .description(description)
            .build();

    // when
    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("room/add",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(roomService, times(1)).addRoom(request);
  }

  @Test
  void getNextList() throws Exception {
    // given
    LocalDateTime lastDateTime = LocalDateTime.now().minusHours(1);

    List<RoomGetDto> roomList = new ArrayList<>();
    RoomGetDto room1 = RoomGetDto.builder()
            .mentoringRoomId(1L)
            .mentorName("김멘토")
            .title("첫번째 방")
            .description("첫번째 방입니다.")
            .createdAt(LocalDateTime.now().minusHours(2))
            .build();
    RoomGetDto room2 = RoomGetDto.builder()
            .mentoringRoomId(2L)
            .mentorName("이멘토")
            .title("두번째 방")
            .description("두번째 방입니다.")
            .createdAt(LocalDateTime.now().minusHours(3))
            .build();
    roomList.add(room1);
    roomList.add(room2);
    RoomListResponseDto response = RoomListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("다음 데이터 불러오기")
            .data(roomList)
            .build();
    given(roomService.getNextList(any())).willReturn(response);

    // when
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/next").with(csrf())
            .queryParam("lastDateTime", String.valueOf(lastDateTime)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("room/list-next",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(roomService, times(1)).getNextList(lastDateTime);
  }

  @Test
  void updateRoom() throws Exception {
    // given
    Long mentoringRoomId = 1L;
    String title = "방제 수정";
    String description = "수정된 소개글";

    RoomUpdateResponseDto response = RoomUpdateResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("방 정보 수정이 완료되었습니다.")
            .data("1")
            .build();

    given(roomService.updateRoom(any())).willReturn(response);

    RoomUpdateRequestDto request = RoomUpdateRequestDto.builder()
            .mentoringRoomId(mentoringRoomId)
            .title(title)
            .description(description)
            .build();
    // when
    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/update")
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("room/update",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(roomService, times(1)).updateRoom(request);
  }

  @Test
  void deleteRoom() throws Exception {
    // given
    Long mentoringRoomId = 1L;

    RoomDeleteResponseDto response = RoomDeleteResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("정상적으로 멘토링룸 삭제가 완료되었습니다.")
            .data(String.valueOf(mentoringRoomId))
            .build();
    given(roomService.deleteRoom(any())).willReturn(response);

    // when
    mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + String.valueOf(mentoringRoomId )).with(csrf()))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("room/delete",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(roomService, times(1)).deleteRoom(mentoringRoomId);
  }

  @Test
  void getRoomsWithMentorId() throws Exception {
    // given
    Long mentorId = 1L;
    List<RoomGetDto> roomList = new ArrayList<>();
    RoomGetDto room1 = RoomGetDto.builder()
            .mentoringRoomId(1L)
            .mentorName("김멘토")
            .title("첫번째 방")
            .description("첫번째 방입니다.")
            .createdAt(LocalDateTime.now())
            .build();
    RoomGetDto room2 = RoomGetDto.builder()
            .mentoringRoomId(1L)
            .mentorName("김멘토")
            .title("두번째 방")
            .description("두번째 방입니다.")
            .createdAt(LocalDateTime.now())
            .build();

    RoomListResponseDto response = RoomListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("멘토 Id로 멘토링룸 조회가 완료되었습니다.")
            .data(roomList)
            .build();

    given(roomService.getRoomWithMentorId(any())).willReturn(response);

    // when
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/mentor/" + String.valueOf(mentorId)).with(csrf()))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("room/get-mentorId",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(roomService, times(1)).getRoomWithMentorId(mentorId);
  }

  @Test
  void getTop10() throws Exception {
    // given
    List<RoomGetDto> roomList = new ArrayList<>();
    RoomGetDto room1 = RoomGetDto.builder()
            .mentoringRoomId(1L)
            .mentorName("김멘토")
            .title("첫번째 방")
            .description("첫번째 방입니다.")
            .createdAt(LocalDateTime.now())
            .build();
    RoomGetDto room2 = RoomGetDto.builder()
            .mentoringRoomId(2L)
            .mentorName("이멘토")
            .title("두번째 방")
            .description("두번째 방입니다.")
            .createdAt(LocalDateTime.now())
            .build();
    roomList.add(room1);
    roomList.add(room2);
    RoomListResponseDto response = RoomListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("최신순으로 정렬 후 상위 10개 방 정보 조회가 완료되었습니다.")
            .data(roomList)
            .build();
    given(roomService.getRoomTop10()).willReturn(response);

    // when
    mockMvc.perform(get(BASE_URL + "/top").with(csrf()))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("room/top-list",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(roomService, times(1)).getRoomTop10();
  }
}
