package com.developers.live.controller;

import com.developers.live.mentoring.controller.ScheduleController;
import com.developers.live.mentoring.dto.*;
import com.developers.live.mentoring.service.ScheduleServiceImpl;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ScheduleController.class)
public class ScheduleControllerTest {

  @Autowired ObjectMapper objectMapper;

  @Autowired private MockMvc mockMvc;

  @MockBean ScheduleServiceImpl scheduleService;

  private static final String BASE_URL = "/api/schedules";

  @Test
  void addSchedule() throws Exception {
    // given
    Long mentoringRoomId = 1L;
    Long mentorId = 1L;
    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end = LocalDateTime.now().plusHours(1);

    ScheduleAddResponseDto response = ScheduleAddResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("일정이 추가되었습니다.")
            .data("1")
            .build();

    given(scheduleService.addSchedule(any())).willReturn(response);

    ScheduleAddRequestDto request = ScheduleAddRequestDto.builder()
            .mentoringRoomId(mentoringRoomId)
            .mentorId(mentorId)
            .start(start)
            .end(end)
            .build();

    // when
    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("schedule/add",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(scheduleService, times(1)).addSchedule(request);
  }

  @Test
  void deleteScheduleAsMentor() throws Exception {
    // given
    Long scheduleId = 1L;

    ScheduleDeleteResponseDto response = ScheduleDeleteResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("정상적으로 멘토링 일정이 취소되었습니다.")
            .data(String.valueOf(scheduleId))
            .build();

    given(scheduleService.deleteScheduleAsMentor(any())).willReturn(response);

    // when
    mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/mentor/" + String.valueOf(scheduleId)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("schedule/delete-mentor",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(scheduleService, times(1)).deleteScheduleAsMentor(scheduleId);
  }

  @Test
  void deleteScheduleAsMentee() throws Exception {
    // given
    Long scheduleId = 1L;

    ScheduleDeleteResponseDto response = ScheduleDeleteResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("정상적으로 신청이 취소되었습니다.")
            .data(String.valueOf(scheduleId))
            .build();

    given(scheduleService.deleteScheduleAsMentee(any())).willReturn(response);

    // when
    mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/mentee/" + String.valueOf(scheduleId)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("schedule/delete-mentee",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(scheduleService, times(1)).deleteScheduleAsMentee(scheduleId);
  }

  @Test
  void getScheduleAsMentor() throws Exception {
    // given
    Long memberId = 1L;

    List<ScheduleGetDto> scheduleList = new ArrayList<>();
    ScheduleGetDto schedule1 = ScheduleGetDto.builder()
            .scheduleId(1L)
            .mentoringRoomTitle("방제")
            .mentorName("김멘토")
            .menteeName("김학생")
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    ScheduleGetDto schedule2 = ScheduleGetDto.builder()
            .scheduleId(2L)
            .mentoringRoomTitle("방제")
            .mentorName("김멘토")
            .menteeName("null")
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    scheduleList.add(schedule1);
    scheduleList.add(schedule2);

    ScheduleListResponseDto response = ScheduleListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("멘토로서의 일정 조회가 완료되었습니다.")
            .data(scheduleList)
            .build();

    given(scheduleService.getScheduleListAsMentor(any())).willReturn(response);

    // when
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/mentor/" + String.valueOf(memberId)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("schedule/get-mentor",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(scheduleService, times(1)).getScheduleListAsMentor(memberId);
  }

  @Test
  void getScheduleAsMentee() throws Exception {
    // given
    Long memberId = 1L;

    List<ScheduleGetDto> scheduleList = new ArrayList<>();
    ScheduleGetDto schedule1 = ScheduleGetDto.builder()
            .scheduleId(1L)
            .mentoringRoomTitle("방제")
            .mentorName("김멘토")
            .menteeName("김학생")
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    ScheduleGetDto schedule2 = ScheduleGetDto.builder()
            .scheduleId(2L)
            .mentoringRoomTitle("방제")
            .mentorName("이멘토")
            .menteeName("김학생")
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    scheduleList.add(schedule1);
    scheduleList.add(schedule2);

    ScheduleListResponseDto response = ScheduleListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("멘티로서의 일정 조회가 완료되었습니다.")
            .data(scheduleList)
            .build();

    given(scheduleService.getScheduleListAsMentee(any())).willReturn(response);

    // when
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/mentee/" + String.valueOf(memberId)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("schedule/get-mentee",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(scheduleService, times(1)).getScheduleListAsMentee(memberId);
  }

  @Test
  void endMentoring() throws Exception {
    // given
    Long scheduleId = 1L;

    MentoringEndResponseDto response = MentoringEndResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("성공적으로 멘토링 정보가 정리되었습니다.")
            .data(String.valueOf(scheduleId))
            .build();

    given(scheduleService.endMentoring(any())).willReturn(response);

    // when
    mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/end/" + String.valueOf(scheduleId)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("schedule/end-mentoring",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(scheduleService, times(1)).endMentoring(scheduleId);
  }
}
