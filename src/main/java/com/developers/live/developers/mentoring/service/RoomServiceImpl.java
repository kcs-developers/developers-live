package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.RoomAddRequestDto;
import com.developers.live.developers.mentoring.dto.RoomAddResponseDto;
import com.developers.live.developers.mentoring.dto.RoomGetDto;
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
}
