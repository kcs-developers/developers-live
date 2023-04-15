package com.developers.live.mentoring.repository;

import com.developers.live.config.JpaConfig;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class)
@ActiveProfiles("local")
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
    scheduleRepository.findAll();

    // then
    Optional<Schedule> result = scheduleRepository.findById(room.getScheduleId());
    assertThat(result.isPresent()).isEqualTo(true);
    assertThat(result.get().getMentorId()).isEqualTo(1L);
  }

  @Test
  void get() {
    // given
    Schedule schedule = Schedule.builder()
            .mentoringRoomId(1L)
            .mentorId(1L)
            .menteeId(2L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .build();
    scheduleRepository.save(schedule);

    Long scheduleId = schedule.getScheduleId();

    // when
    Optional<Schedule> foundSchedule = scheduleRepository.findById(scheduleId);

    // then
    assertThat(foundSchedule.isPresent()).isEqualTo(true);
    assertThat(foundSchedule.get().getScheduleId()).isEqualTo(scheduleId);
  }

  @Test
  void bulkSave() {
    // given
    LongStream.rangeClosed(1, 20).forEach(i -> {
      Schedule schedule = Schedule.builder()
              .mentoringRoomId(i)
              .mentorId(1L)
              .start(LocalDateTime.now())
              .end(LocalDateTime.now().plusHours(1))
              .build();

      scheduleRepository.save(schedule);
    });

    // when
    List<Schedule> foundScheduleList = scheduleRepository.findAll();
    for (Schedule schedule : foundScheduleList) {
      System.out.println(schedule);
    }

    // then
    for (Schedule schedule : foundScheduleList) {
      assertThat(schedule.getScheduleId()).isNotNull();
    }
  }
}
