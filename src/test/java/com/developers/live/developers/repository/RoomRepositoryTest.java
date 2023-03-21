package com.developers.live.developers.repository;

import com.developers.live.developers.mentoring.entity.Room;
import com.developers.live.developers.mentoring.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        roomRepository.save(room);

        // when
        List<Room> roomList = roomRepository.findAll();

        // then
        Room result = roomList.get(0);
        assertThat(result.getRoomId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("mentoring room");
        assertThat(result.getDescription()).isEqualTo("contents is ...");
    }
}
