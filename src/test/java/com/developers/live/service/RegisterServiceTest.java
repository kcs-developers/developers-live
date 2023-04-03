package com.developers.live.service;

import com.developers.live.mentoring.dto.RegisterRequestDto;
import com.developers.live.mentoring.dto.RegisterResponseDto;
import com.developers.live.mentoring.dto.ScheduleAddRequestDto;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.service.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

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

  @Test
  public void 사람20명_한꺼번에_한_스케줄에_신청() throws InterruptedException {
    final int people = 20;

    List<Thread> workers = new ArrayList<>();

    for (int i = 0; i < people; i++) {
      workers.add(new Thread(new Register((long) i, 2L)));
    }

    workers.forEach(Thread::start);
  }

  private class Register implements Runnable{
    private final Long userId;
    private final Long scheduleId;

    public Register(Long userId, Long scheduleId) {
      this.userId = userId;
      this.scheduleId = scheduleId;
    }

    @Override
    public void run() {
      registerService.register(
              RegisterRequestDto.builder()
                      .scheduleId(scheduleId)
                      .menteeId(userId)
                      .build()
      );
    }
  }
}
