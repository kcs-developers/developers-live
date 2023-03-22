package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.RoomAddRequestDto;
import com.developers.live.developers.mentoring.dto.RoomAddResponseDto;
import com.developers.live.developers.mentoring.dto.RoomListResponseDto;
import com.developers.live.developers.mentoring.entity.Room;
import com.developers.live.developers.mentoring.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

  private final RoomRepository roomRepository;

  @Override
  public List<RoomListResponseDto> getList() {
    List<Room> entityList = roomRepository.findAll();
    return entityList.stream().map(room -> entityToDto(room)).collect(Collectors.toList());
  }

  @Override
  public RoomAddResponseDto addRoom(RoomAddRequestDto req) {
    Room result = roomRepository.save(
            Room.builder()
            .mentorId(req.getMentorId())
            .title(req.getTitle())
            .description(req.getDescription())
            .point(req.getPoint())
            .build()
    );

    return RoomAddResponseDto.builder()
            .code(String.valueOf(HttpStatus.OK))
            .msg("정상적으로 채팅방을 생성하였습니다.")
            .data(String.valueOf(result.getMentoringRoomId()))
            .build();
  }

  @Override
  public List<RoomListResponseDto> getListWithSearch(String param) {
    List<Room> entityList = roomRepository.findByTitleContaining(param);
    return entityList.stream().map(room -> entityToDto(room)).collect(Collectors.toList());
  }
}
