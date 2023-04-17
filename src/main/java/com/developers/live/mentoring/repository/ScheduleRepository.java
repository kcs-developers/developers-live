package com.developers.live.mentoring.repository;

import com.developers.live.mentoring.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  List<Schedule> findAllByMentorId(Long memberId);
  List<Schedule> findAllByMenteeId(Long memberId);
  List<Schedule> findAllByMentoringRoomId(Long mentoringRoomId);
  List<Schedule> findAllByMentoringRoomIdAndMenteeIdIsNull(Long mentoringRoomId);
  Optional<Schedule> findScheduleByMentoringRoomIdIsAndStartIs(Long mentoringRoomId, LocalDateTime start);
}
