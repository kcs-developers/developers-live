package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.*;
import com.developers.live.developers.mentoring.entity.Room;
import com.developers.live.developers.mentoring.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

  private final RoomRepository roomRepository;

  @Override
  public RoomListResponseDto getList() {
    List<RoomGetDto> dtoList = roomRepository.findAll().stream().map(room -> entityToDto(room)).toList();
    return RoomListResponseDto.builder()
            .code(String.valueOf(HttpStatus.OK))
            .msg("정상적으로 멘토링방 전체 조회가 되었습니다.")
            .data(dtoList)
            .build();
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
  public RoomListResponseDto getListWithSearch(String param) {
    List<RoomGetDto> dtoList = roomRepository.findByTitleContaining(param.replaceAll("\\s+", " ")).stream().map(room -> entityToDto(room)).toList();
    return RoomListResponseDto.builder()
            .code(String.valueOf(HttpStatus.OK))
            .msg("정상적으로 멘토링방 문자열 검색이 되었습니다.")
            .data(dtoList)
            .build();
  }

  @Override
  public RoomListResponseDto getListWithRecent() {
    List<RoomGetDto> dtoList = roomRepository.findAllByOrderByUpdatedAtDesc().stream().map(room -> entityToDto(room)).toList();
    return RoomListResponseDto.builder()
            .code(String.valueOf(HttpStatus.OK))
            .msg("정상적으로 최신순으로 정렬된 채팅방 목록이 조회되었습니다.")
            .data(dtoList)
            .build();
  }

  @Override
  @Transactional
  public RoomUpdateResponseDto updateRoom(RoomUpdateRequestDto req) {
    Optional<Room> optionalRoom = roomRepository.findById(req.getMentoringRoomId());

    RoomUpdateResponseDto response;
    if (optionalRoom.isPresent()) {
      Room room = optionalRoom.get();

      room.updateRoomInfo(req.getTitle(), req.getDescription());

      response = RoomUpdateResponseDto.builder()
              .code(HttpStatus.OK.name())
              .msg("방 정보 수정이 완료되었습니다.")
              .data(String.valueOf(req.getMentoringRoomId()))
              .build();
    }
    else {
      response = RoomUpdateResponseDto.builder()
              .code(HttpStatus.NOT_FOUND.name())
              .msg("수정하려는 방의 정보를 찾을 수 없습니다.")
              .data(null)
              .build();
    }
    return response;
  }
}
