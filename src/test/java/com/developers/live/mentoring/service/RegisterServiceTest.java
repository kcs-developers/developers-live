package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.RegisterRequestDto;
import com.developers.live.mentoring.dto.RegisterResponseDto;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.repository.ScheduleRepository;
import com.developers.live.mentoring.service.RegisterServiceImpl;
import com.developers.live.mentoring.service.ScheduleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest {

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @Mock ScheduleRepository scheduleRepository;

  @Mock ScheduleServiceImpl scheduleService;

  @InjectMocks
  RegisterServiceImpl registerService;

  @Test
  public void 멘토링_신청() {
    // given
    ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);
    Long scheduleId = 1L;
    Long menteeId = 2L;
    RegisterRequestDto request = RegisterRequestDto.builder()
            .scheduleId(scheduleId)
            .menteeId(menteeId)
            .build();
    Schedule schedule = Schedule.builder()
            .mentoringRoomId(1L)
            .mentorId(1L)
            .start(LocalDateTime.now())
            .start(LocalDateTime.now().plusHours(1))
            .build();
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(redisTemplate.opsForValue().get(String.valueOf(scheduleId))).thenReturn(String.valueOf(menteeId));
    when(scheduleRepository.findById(any())).thenReturn(Optional.of(schedule));

    // when
    RegisterResponseDto response = registerService.register(request);

    // then
    assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
    assertThat(response.getMsg()).isEqualTo("정상적으로 신청이 완료되었습니다.");
    assertThat(response.getData()).isInstanceOf(String.class);
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
