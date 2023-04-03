package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.RoomGetDto;
import com.developers.live.mentoring.entity.Room;
import com.developers.live.mentoring.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@EnableScheduling
public class CachingRoomServiceImpl implements CachingRoomService {

  private final RoomRepository roomRepository;

  // 첫번째 저장소 비우기
  @Override
  @CacheEvict(value = "firstMentoringRoomList")
  public void removeFirstMentoringRoomList() { }

  // 예비 저장소 비우기
  @Override
  @CacheEvict(value = "spareMentoringRoomList")
  public void removeSpareMentoringRoomList() { }

  // 첫번째 저장소 내용을 DB 정보로 update 한다.
  @Override
  @Cacheable(value = "firstMentoringRoomList")
  public List<RoomGetDto> getAndUpdateFirstCacheStorage() {
    List<Room> firstMentoringRoomList = roomRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 100));
    return firstMentoringRoomList.stream().map(room -> entityToDto(room)).toList();
  }

  // 첫 목록 조회 요청, 새로 고침 등의 상황에서 첫번째 데이터 다음의 데이터로 예비 저장소를 채워놓는다.
  @Override
  @CachePut(value = "spareMentoringRoomList")
  public List<RoomGetDto> initSpareCacheStorage() {
    List<Room> spareMentoringRoomList = roomRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(1, 100));
    return spareMentoringRoomList.stream().map(room -> entityToDto(room)).toList();
  }

  // 기본적으로 예비 저장소에는 한 묶음의 데이터가 모두 조회되고 뒤의 데이터를 더 요청할 것을 대비한 데이터가 들어있다.
  // 첫 목록 조회 요청(페이지 방문), 새로 고침 등의 상황에서 가져온 첫 데이터를 기준으로 예비 데이터는,
  // 앞에서 조회된 데이터의 마지막 createdAt을 기준으로 보다 늦게 생성된 데이터 중 한 묶음(size = 100)을 가지고 있게 된다.
  @Override
  @Cacheable(value = "spareMentoringRoomList")
  public List<RoomGetDto> getAndUpdateSpareCacheStorage(LocalDateTime lastDateTime) {
    // 앞 데이터를 모두 조회했을 때 불러올 일종의 예비 데이터
    List<Room> spareMentoringRoomList = roomRepository.findAllByCreatedAtBeforeOrderByCreatedAtDesc(lastDateTime, PageRequest.of(0, 100));
    return spareMentoringRoomList.stream().map(room -> entityToDto(room)).toList();
  }
}
