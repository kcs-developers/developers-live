package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.*;
import com.developers.live.developers.mentoring.entity.Room;
import com.developers.live.developers.mentoring.entity.Schedule;
import com.developers.live.developers.mentoring.repository.RoomRepository;
import com.developers.live.developers.mentoring.repository.ScheduleRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

  private final RoomRepository roomRepository;
  private final ScheduleRespository scheduleRespository;

  @Override
  public ScheduleAddResponseDto addSchedule(ScheduleAddRequestDto request) {
    Optional<Room> room = roomRepository.findById(request.getMentoringRoomId());

    ScheduleAddResponseDto response;
    if (room.isPresent()) {
      Schedule schedule = scheduleRespository.save(
              Schedule.builder()
                      .mentoringRoomId(request.getMentoringRoomId())
                      .mentorId(request.getMentorId())
                      .start(request.getStart())
                      .end(request.getEnd())
                      .build());

      response = ScheduleAddResponseDto.builder()
              .code(String.valueOf(HttpStatus.OK))
              .msg("일정이 추가되었습니다.")
              .data(String.valueOf(schedule.getScheduleId()))
              .build();
    }
    else {
      response = ScheduleAddResponseDto.builder()
              .code(String.valueOf(HttpStatus.NOT_FOUND))
              .msg("방에 대한 정보를 찾지 못했습니다.")
              .data("null")
              .build();
    }
    return response;
  }

  @Override
  public ScheduleDeleteResponseDto deleteSchedule(Long scheduleId) {
    scheduleRespository.deleteById(scheduleId);
    return ScheduleDeleteResponseDto.builder()
            .code(String.valueOf(HttpStatus.OK))
            .msg("일정이 삭제되었습니다.")
            .data(String.valueOf(scheduleId))
            .build();
  }

  @Override
  public ScheduleListResponseDto getScheduleListAsMentor(Long memberId) {
    List<ScheduleGetDto> dtoList = scheduleRespository.findAllByMentorId(memberId).stream().map(schedule -> entityToDto(schedule)).toList();

    return ScheduleListResponseDto.builder()
            .code(String.valueOf(HttpStatus.OK))
            .msg("멘토로서의 일정 조회가 완료되었습니다.")
            .data(dtoList)
            .build();
  }

  @Override
  public ScheduleListResponseDto getScheduleListAsMentee(Long memberId) {
    List<ScheduleGetDto> dtoList = scheduleRespository.findAllByMenteeId(memberId).stream().map(schedule -> entityToDto(schedule)).toList();

    return ScheduleListResponseDto.builder()
            .code(String.valueOf(HttpStatus.OK))
            .msg("멘티로서의 일정 조회가 완료되었습니다.")
            .data(dtoList)
            .build();
  }

  @Override
  public String getMentoringRoomTitle(Long mentoringRoomId) {
    Optional<Room> foundRoom = roomRepository.findById(mentoringRoomId);

    if (foundRoom.isPresent()) {
      return foundRoom.get().getTitle();
    }
    else {
      return null;
    }
  }
}
