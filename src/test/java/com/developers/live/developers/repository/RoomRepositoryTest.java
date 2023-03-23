package com.developers.live.developers.repository;

import com.developers.live.developers.mentoring.entity.Room;
import com.developers.live.developers.mentoring.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RoomRepositoryTest {

    @Autowired RoomRepository roomRepository;

    @Test
    public void save() {
        // given
        Room room = Room.builder()
                .mentorId(1L)
                .title("mentoring room")
                .description("contents is ...")
                .point(10L)
                .build();
        Room createdRoom = roomRepository.save(room);

        // when
        Optional<Room> result = roomRepository.findById(createdRoom.getMentoringRoomId());

        // then
        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().getMentorId()).isEqualTo(1L);
        assertThat(result.get().getPoint()).isEqualTo(10L);
    }

    @Test
    public void get() {
        // given
        Room room = Room.builder()
                .mentorId(1L)
                .title("mentoring room")
                .description("contents is ...")
                .point(10L)
                .build();
        Room createdRoom = roomRepository.save(room);

        Long roomId = room.getMentoringRoomId();

        // when
        Optional<Room> foundRoom = roomRepository.findById(roomId);

        // then
        assertThat(foundRoom.isPresent()).isEqualTo(true);
        assertThat(foundRoom.get().getMentoringRoomId()).isEqualTo(roomId);
    }
}
