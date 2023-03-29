package com.developers.live.developers.service;

import com.developers.live.developers.mentoring.dto.RegisterRequestDto;
import com.developers.live.developers.mentoring.dto.RegisterResponseDto;
import com.developers.live.developers.mentoring.entity.Schedule;
import com.developers.live.developers.mentoring.service.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RegisterServiceTest {

  @Autowired RegisterService registerService;
  @Test
  public void 멘토링_신청() {
    // given
    RegisterRequestDto request = RegisterRequestDto.builder()
            .scheduleId(3L)
            .menteeId(2L)
            .build();

    // when
    RegisterResponseDto response = registerService.register(request);

    // then
    assertThat(response.getCode()).isEqualTo(String.valueOf(HttpStatus.OK));
    assertThat(response.getData()).isEqualTo(String.valueOf(request.getMenteeId()));
  }
}
