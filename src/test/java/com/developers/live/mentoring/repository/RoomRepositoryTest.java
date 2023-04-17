package com.developers.live.mentoring.repository;

import com.developers.live.config.JpaConfig;
import com.developers.live.mentoring.entity.Room;
import com.developers.live.mentoring.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class)
@ActiveProfiles("live-prod")
public class RoomRepositoryTest {

    @Autowired RoomRepository roomRepository;

    @Test
    void save() {
        // given
        Room room = Room.builder()
                .mentorId(1L)
                .mentorName("김멘토")
                .title("mentoring room")
                .description("contents is ...")
                .build();
        Room createdRoom = roomRepository.save(room);

        // when
        Optional<Room> result = roomRepository.findById(createdRoom.getMentoringRoomId());

        // then
        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().getMentorId()).isEqualTo(1L);
    }

    @Test
    void get() {
        // given
        Room room = Room.builder()
                .mentorId(1L)
                .mentorName("김멘토")
                .title("mentoring room")
                .description("contents is ...")
                .build();
        Room createdRoom = roomRepository.save(room);

        Long roomId = room.getMentoringRoomId();

        // when
        Optional<Room> foundRoom = roomRepository.findById(roomId);

        // then
        assertThat(foundRoom.isPresent()).isEqualTo(true);
        assertThat(foundRoom.get().getMentoringRoomId()).isEqualTo(roomId);
    }

    @DisplayName("50개의 방 데이터 추가")
    @Test
    void bulkSave() {
        // given
        LongStream.rangeClosed(1, 50).forEach(i -> {
            Room room = Room.builder()
                    .mentorId(1L)
                    .mentorName("멘토@"+i)
                    .title("mentoring room")
                    .description("contents is ...")
                    .build();

            roomRepository.save(room);
        });

        // when
        List<Room> allRooms = roomRepository.findAll();

        // then
        for (Room room : allRooms) {
            assertThat(room.getMentoringRoomId()).isNotNull();
        }
    }
}
