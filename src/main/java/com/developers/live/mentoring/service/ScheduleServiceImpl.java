package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.*;
import com.developers.live.developers.mentoring.entity.Room;
import com.developers.live.developers.mentoring.entity.Schedule;
import com.developers.live.developers.mentoring.repository.RoomRepository;
import com.developers.live.developers.mentoring.repository.ScheduleRepository;
import com.developers.live.mentoring.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

  private final RoomRepository roomRepository;
  private final ScheduleRepository scheduleRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public ScheduleAddResponseDto addSchedule(ScheduleAddRequestDto request) {
    Optional<Room> room = roomRepository.findById(request.getMentoringRoomId());

    ScheduleAddResponseDto response;
    if (room.isPresent()) {
      Schedule schedule = scheduleRepository.save(
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
              .msg("해당 스케줄에 대한 정보를 찾지 못했습니다.")
              .data(null)
              .build();
    }
    return response;
  }

  @Override
  public ScheduleDeleteResponseDto deleteScheduleAsMentor(Long scheduleId) {
    Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);

    if (optionalSchedule.isPresent()) {
      Schedule schedule = optionalSchedule.get();

      if (schedule.getMenteeId() == null) {
        scheduleRepository.deleteById(scheduleId);

        return ScheduleDeleteResponseDto.builder()
                .code(HttpStatus.OK.name())
                .msg("정상적으로 멘토링 일정이 취소되었습니다.")
                .data(String.valueOf(scheduleId))
                .build();
      }
      else {
        return ScheduleDeleteResponseDto.builder()
                .code(HttpStatus.BAD_REQUEST.name())
                .msg("이미 일정에 신청한 사용자가 있어 일정 취소가 불가능합니다.")
                .data(null)
                .build();
      }
    }
    else {
      return ScheduleDeleteResponseDto.builder()
              .code(HttpStatus.NOT_FOUND.name())
              .msg("스케쥴에 대한 정보를 찾을 수 없습니다.")
              .data(null)
              .build();
    }
  }

  @Override
  @Transactional
  public ScheduleDeleteResponseDto deleteScheduleAsMentee(Long scheduleId) {
    Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);

    if (optionalSchedule.isPresent()) {
      optionalSchedule.get().changeMentee(null);

      return ScheduleDeleteResponseDto.builder()
              .code(HttpStatus.OK.name())
              .msg("정상적으로 신청이 취소되었습니다.")
              .data(String.valueOf(scheduleId))
              .build();
    }
    else {
      return ScheduleDeleteResponseDto.builder()
              .code(HttpStatus.NOT_FOUND.name())
              .msg("해당 스케쥴에 대한 정보가 없습니다.")
              .data(null)
              .build();
    }
  }

  @Override
  public ScheduleListResponseDto getScheduleListAsMentor(Long memberId) {
    List<ScheduleGetDto> dtoList = scheduleRepository.findAllByMentorId(memberId).stream().map(schedule -> entityToDto(schedule)).toList();

    return ScheduleListResponseDto.builder()
            .code(String.valueOf(HttpStatus.OK))
            .msg("멘토로서의 일정 조회가 완료되었습니다.")
            .data(dtoList)
            .build();
  }

  @Override
  public ScheduleListResponseDto getScheduleListAsMentee(Long memberId) {
    List<ScheduleGetDto> dtoList = scheduleRepository.findAllByMenteeId(memberId).stream().map(schedule -> entityToDto(schedule)).toList();

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

  @Override
  public MentoringEndResponseDto endMentoring(Long scheduleId) {
    Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);

    if (optionalSchedule.isPresent()) {
      // redis 에서 해당 스케쥴 관련된 데이터 삭제
      // RDB 에서 또한 스케쥴 soft delete 진행
      redisTemplate.delete(String.valueOf(scheduleId));
      scheduleRepository.deleteById(scheduleId);

      return MentoringEndResponseDto.builder()
              .code(HttpStatus.OK.name())
              .msg("성공적으로 멘토링 정보가 정리되었습니다.")
              .data(String.valueOf(scheduleId))
              .build();
    }
    else {
      return MentoringEndResponseDto.builder()
              .code(HttpStatus.NOT_FOUND.name())
              .msg("해당 스케쥴에 대한 정보가 없습니다.")
              .data(null)
              .build();
    }
  }
}
