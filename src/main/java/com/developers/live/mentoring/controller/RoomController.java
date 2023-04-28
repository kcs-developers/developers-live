package com.developers.live.mentoring.controller;

import com.developers.live.mentoring.dto.*;
import com.developers.live.mentoring.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/room")
@RestController
public class RoomController {
    private final RoomService roomService;

    /**
     * RoomController에 포함된 비즈니스 로직은 RoomService 구현체에서 수행하도록 수정해야 한다.
     */

    @GetMapping
    public ResponseEntity<RoomListResponseDto> getFirstList() {
        RoomListResponseDto response = roomService.getFirstList();
        log.info("첫번째 저장소에 저장된 데이터");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<RoomAddResponseDto> roomInit(@RequestBody @Valid RoomAddRequestDto request) {
        RoomAddResponseDto response = roomService.addRoom(request);
        log.info("방 생성: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/next")
    public ResponseEntity<RoomListResponseDto> getSpareList(@RequestParam("lastDateTime") LocalDateTime lastDateTime) {
        RoomListResponseDto response = roomService.getNextList(lastDateTime);
        log.info("다음 데이터: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<RoomUpdateResponseDto> roomUpdate(@RequestBody @Valid RoomUpdateRequestDto request) {
        RoomUpdateResponseDto response = roomService.updateRoom(request);
        log.info("방 정보 업데이트: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{mentoringRoomId}")
    public ResponseEntity<RoomDeleteResponseDto> roomDelete(@PathVariable("mentoringRoomId") Long mentoringRoomId) {
        RoomDeleteResponseDto response = roomService.deleteRoom(mentoringRoomId);
        log.info("방 삭제: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{searchingWord:.+}")
    public ResponseEntity<RoomListResponseDto> roomListWithSearchingWord(@PathVariable("searchingWord") String searchingWord) {
        RoomListResponseDto response = roomService.getRoomWithSearchingWord(searchingWord);
        log.info("검색어 이용해 방 목록 조회: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<RoomListResponseDto> roomListWithMentorId(@PathVariable Long mentorId) {
        RoomListResponseDto response = roomService.getRoomWithMentorId(mentorId);
        log.info("멘토 Id로 방 목록 조회: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/top")
    public ResponseEntity<RoomListResponseDto> roomListTop10() {
        RoomListResponseDto response = roomService.getRoomTop10();
        log.info("가장 최신에 만들어진 방 10개 조회: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
