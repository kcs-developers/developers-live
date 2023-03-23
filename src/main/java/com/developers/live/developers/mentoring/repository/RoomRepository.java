package com.developers.live.developers.mentoring.repository;

import com.developers.live.developers.mentoring.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

  List<Room> findByTitleContaining(String param);
  List<Room> findAllByOrderByUpdatedAtDesc();
}
