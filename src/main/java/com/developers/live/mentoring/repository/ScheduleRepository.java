package com.developers.live.developers.mentoring.repository;

import com.developers.live.developers.mentoring.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  List<Schedule> findAllByMentorId(Long memberId);
  List<Schedule> findAllByMenteeId(Long memberId);
  List<Schedule> findAllByMentoringRoomId(Long mentoringRoomId);
}
