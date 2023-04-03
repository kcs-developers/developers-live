package com.developers.live.developers.repository;

import com.developers.live.developers.mentoring.entity.Schedule;
import com.developers.live.developers.mentoring.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ScheduleRepositoryTest {

  @Autowired
  ScheduleRepository scheduleRepository;

  @Test
  void save() {
    // given
    Schedule room = Schedule.builder()
            .mentoringRoomId(1L)
            .mentorId(1L)
            .menteeId(2L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().minusHours(1L))
            .build();

    scheduleRepository.save(room);

    // when
    List<Schedule> scheduleList = scheduleRepository.findAll();

    // then
    Optional<Schedule> result = scheduleRepository.findById(room.getScheduleId());
    assertThat(result.isPresent()).isEqualTo(true);
    assertThat(result.get().getMentorId()).isEqualTo(1L);
  }

  @Test
  public void get() {
    // given
    Schedule schedule = Schedule.builder()
            .mentoringRoomId(1L)
            .mentorId(1L)
            .menteeId(2L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    Schedule createdSchedule = scheduleRepository.save(schedule);

    Long scheduleId = schedule.getScheduleId();

    // when
    Optional<Schedule> foundSchedule = scheduleRepository.findById(scheduleId);

    // then
    assertThat(foundSchedule.isPresent()).isEqualTo(true);
    assertThat(foundSchedule.get().getScheduleId()).isEqualTo(scheduleId);
  }
}
