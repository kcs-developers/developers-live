package com.developers.live.developers.mentoring.controller;

import com.developers.live.developers.mentoring.dto.RoomAddRequestDto;
import com.developers.live.developers.mentoring.dto.RoomAddResponseDto;
import com.developers.live.developers.mentoring.dto.RoomListResponseDto;
import com.developers.live.developers.mentoring.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {
    private final RoomService roomService;
    private final RestTemplate restTemplate;

    /**
     * RoomController에 포함된 비즈니스 로직은 RoomService 구현체에서 수행하도록 수정해야 한다.
     */

    @GetMapping("")
    public ResponseEntity<List<RoomListResponseDto>> roomList() {
        List<RoomListResponseDto> response = roomService.getList();
        log.info("방 전체 목록 조회: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    public ResponseEntity<RoomAddResponseDto> roomInit(@RequestBody @Valid RoomAddRequestDto request) {
        RoomAddResponseDto response = roomService.addRoom(request);
        log.info("방 생성: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{param}")
    public ResponseEntity<List<RoomListResponseDto>> roomListWithParam(@PathVariable("param") String param) {
        List<RoomListResponseDto> response = roomService.getListWithSearch(param);
        log.info("검색어로 방 목록 조회: " + response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
