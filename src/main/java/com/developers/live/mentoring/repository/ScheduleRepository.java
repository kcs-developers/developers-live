package com.developers.live.mentoring.repository;

import com.developers.live.mentoring.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  List<Schedule> findAllByMentorId(Long memberId);
  List<Schedule> findAllByMenteeId(Long memberId);
  List<Schedule> findAllByMentoringRoomId(Long mentoringRoomId);
  List<Schedule> findAllByMentoringRoomIdAndMenteeIdIsNull(Long mentoringRoomId);
}
