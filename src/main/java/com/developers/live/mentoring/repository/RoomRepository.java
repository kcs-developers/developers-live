package com.developers.live.mentoring.repository;

import com.developers.live.mentoring.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

  List<Room> findByTitleContaining(String param);
  List<Room> findAllByOrderByCreatedAtDesc(Pageable pageable);
  List<Room> findAllByCreatedAtBeforeOrderByCreatedAtDesc(LocalDateTime lastDateTime, Pageable pageable);
  List<Room> findAllByMentorId(Long mentorId);
}
