package com.developers.live.mentoring.service;

import com.developers.live.mentoring.entity.Room;
import com.developers.live.mentoring.entity.Schedule;
import com.developers.live.mentoring.repository.RoomRepository;
import com.developers.live.mentoring.repository.ScheduleRepository;
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
              .code(HttpStatus.OK.toString())
              .msg("일정이 추가되었습니다.")
              .data(String.valueOf(schedule.getScheduleId()))
              .build();
    }
    else {
      response = ScheduleAddResponseDto.builder()
              .code(HttpStatus.NOT_FOUND.toString())
              .msg("해당 스케줄에 대한 정보를 찾지 못했습니다.")
              .data(null)
              .build();
    }
    return response;
  }

  // 멘토가 멘토링룸에 대한 일정을 취소할 때의 로직을 수행하는 메서드
  // 해당 일정에 대해 멘티가 예정되어 있다면 삭제를 수행하지 않고 바로 에러를 반환하도록 한다.
  @Override
  public ScheduleDeleteResponseDto deleteScheduleAsMentor(Long scheduleId) {
    Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);

    if (optionalSchedule.isPresent()) {
      Schedule schedule = optionalSchedule.get();

      if (schedule.getMenteeId() == null) {
        scheduleRepository.deleteById(scheduleId);

        return ScheduleDeleteResponseDto.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 멘토링 일정이 취소되었습니다.")
                .data(String.valueOf(scheduleId))
                .build();
      }
      else {
        return ScheduleDeleteResponseDto.builder()
                .code(HttpStatus.BAD_REQUEST.toString())
                .msg("이미 일정에 신청한 사용자가 있어 일정 취소가 불가능합니다.")
                .data(null)
                .build();
      }
    }
    else {
      return ScheduleDeleteResponseDto.builder()
              .code(HttpStatus.NOT_FOUND.toString())
              .msg("스케쥴에 대한 정보를 찾을 수 없습니다.")
              .data(null)
              .build();
    }
  }

  // 멘티가 신청한 멘토링을 취소할 때의 로직을 수행하는 메서드
  // 해당 일정에 대해 mentee 를 매칭해놓은 menteeId 속성 값을 null 로 바꿔준다.
  @Override
  @Transactional
  public ScheduleDeleteResponseDto deleteScheduleAsMentee(Long scheduleId) {
    Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);

    if (optionalSchedule.isPresent()) {
      optionalSchedule.get().changeMentee(null);

      return ScheduleDeleteResponseDto.builder()
              .code(HttpStatus.OK.toString())
              .msg("정상적으로 신청이 취소되었습니다.")
              .data(String.valueOf(scheduleId))
              .build();
    }
    else {
      return ScheduleDeleteResponseDto.builder()
              .code(HttpStatus.NOT_FOUND.toString())
              .msg("해당 스케쥴에 대한 정보가 없습니다.")
              .data(null)
              .build();
    }
  }

  @Override
  public ScheduleListResponseDto getScheduleListAsMentor(Long memberId) {
    List<ScheduleGetDto> dtoList = scheduleRepository.findAllByMentorId(memberId).stream().map(schedule -> entityToDto(schedule)).toList();

    return ScheduleListResponseDto.builder()
            .code(HttpStatus.OK.toString())
            .msg("멘토로서의 일정 조회가 완료되었습니다.")
            .data(dtoList)
            .build();
  }

  @Override
  public ScheduleListResponseDto getScheduleListAsMentee(Long memberId) {
    List<ScheduleGetDto> dtoList = scheduleRepository.findAllByMenteeId(memberId).stream().map(schedule -> entityToDto(schedule)).toList();

    return ScheduleListResponseDto.builder()
            .code(HttpStatus.OK.toString())
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

  // 멘토링이 끝난 후, 멘토가 멘토링방 "종료" 버튼을 눌러 방에 대한 정보를 삭제하는 로직을 수행하는 메서드
  // redis 에 저장된 scheduleId - menteeId 로 매핑된 정보를 지우고, RDB 에서도 해당 스케줄에 대한 정보 soft delete 방식으로 삭제 처리를 해준다.
  @Override
  public MentoringEndResponseDto endMentoring(Long scheduleId) {
    Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);

    if (optionalSchedule.isPresent()) {
      redisTemplate.delete(String.valueOf(scheduleId));
      scheduleRepository.deleteById(scheduleId);

      return MentoringEndResponseDto.builder()
              .code(HttpStatus.OK.toString())
              .msg("성공적으로 멘토링 정보가 정리되었습니다.")
              .data(String.valueOf(scheduleId))
              .build();
    }
    else {
      return MentoringEndResponseDto.builder()
              .code(HttpStatus.NOT_FOUND.toString())
              .msg("해당 스케쥴에 대한 정보가 없습니다.")
              .data(null)
              .build();
    }
  }
}
