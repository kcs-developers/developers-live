package com.developers.live.mentoring.controller;

import com.developers.live.mentoring.controller.RegisterController;
import com.developers.live.mentoring.dto.RegisterRequestDto;
import com.developers.live.mentoring.dto.RegisterResponseDto;
import com.developers.live.mentoring.service.RegisterServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = RegisterController.class)
@WithMockUser("USER")
public class RegisterControllerTest {

  @Autowired ObjectMapper objectMapper;

  @Autowired private MockMvc mockMvc;

  @MockBean RegisterServiceImpl registerService;

  private static final String BASE_URL = "/api/register";

  @Test
  void register() throws Exception {
    // given
    Long scheduleId = 1L;
    Long menteeId = 1L;
    String menteeName = "김멘티";

    RegisterRequestDto request = RegisterRequestDto.builder()
            .scheduleId(scheduleId)
            .menteeId(menteeId)
            .menteeName(menteeName)
            .build();

    RegisterResponseDto response = RegisterResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("정상적으로 신청이 완료되었습니다.")
            .data(String.valueOf(menteeId))
            .build();

    given(registerService.register(any())).willReturn(response);

    // when
    mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("register/req",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

    // then
    verify(registerService, times(1)).register(request);
  }
}
