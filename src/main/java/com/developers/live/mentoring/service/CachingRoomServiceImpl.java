package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.RoomGetDto;
import com.developers.live.mentoring.entity.Room;
import com.developers.live.mentoring.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@EnableScheduling
public class CachingRoomServiceImpl implements CachingRoomService {

  private final RoomRepository roomRepository;

  // 첫번째 저장소 비우기
  @Override
  @CacheEvict(cacheNames = "mentoringRoomList")
  public void removeFirstMentoringRoomList() { }

  // 첫번째 저장소 내용을 DB 정보로 update 한다.
  @Override
  @Cacheable(cacheNames = "mentoringRoomList")
  public List<RoomGetDto> getAndUpdateFirstCacheStorage() {
    List<Room> firstMentoringRoomList = roomRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 100));
    return firstMentoringRoomList.stream().map(room -> entityToDto(room)).toList();
  }
}
