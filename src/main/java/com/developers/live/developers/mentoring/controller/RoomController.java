package com.developers.live.developers.mentoring.controller;

import com.developers.live.developers.mentoring.dto.*;
import com.developers.live.developers.mentoring.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {
    private final RoomService roomService;

    /**
     * RoomController에 포함된 비즈니스 로직은 RoomService 구현체에서 수행하도록 수정해야 한다.
     */

    @GetMapping
    public ResponseEntity<RoomListResponseDto> roomList() {
        RoomListResponseDto response = roomService.getList();
        log.info("방 전체 목록 조회: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<RoomAddResponseDto> roomInit(@RequestBody @Valid RoomAddRequestDto request) {
        RoomAddResponseDto response = roomService.addRoom(request);
        log.info("방 생성: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{param}")
    public ResponseEntity<RoomListResponseDto> roomListWithParam(@PathVariable("param") String param) {
        RoomListResponseDto response = roomService.getListWithSearch(param);
        log.info("검색어로 방 목록 조회: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/sort/recent")
    public ResponseEntity<RoomListResponseDto> roomListWithRecent() {
        RoomListResponseDto response = roomService.getListWithRecent();
        log.info("최신순으로 정렬 후 목록 조회: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<RoomUpdateResponseDto> roomUpdate(@RequestBody @Valid RoomUpdateRequestDto request) {
        RoomUpdateResponseDto response = roomService.updateRoom(request);
        log.info("방 정보 업데이트: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
