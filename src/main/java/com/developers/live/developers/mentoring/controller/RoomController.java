package com.developers.live.developers.mentoring.controller;

import com.developers.live.developers.mentoring.dto.RoomAddRequestDto;
import com.developers.live.developers.mentoring.dto.RoomAddResponseDto;
import com.developers.live.developers.mentoring.dto.RoomEnterRequestDto;
import com.developers.live.developers.mentoring.dto.RoomEnterResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/room")
@RestController
public class RoomController {
//    private final RoomService roomService;
    private final RestTemplate restTemplate;

    /**
     *
     * RoomController에 포함된 비즈니스 로직은 RoomService 구현체에서 수행하도록 수정해야 한다.
     */
    @PostMapping("/init")
    public ResponseEntity<RoomAddResponseDto> roomInit(@RequestBody RoomAddRequestDto request) {
        String sessionId = null;
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9003")
                .path("/api/sessions")
                .encode()
                .build()
                .toUri();
        RoomAddResponseDto response = RoomAddResponseDto.builder()
                .code(String.valueOf(HttpStatus.OK))
                .msg("정상적으로 채팅방을 생성하였습니다.")
                .data(sessionId)
                .build();
        System.out.println(response);
        ResponseEntity<RoomAddResponseDto> responseEntity = restTemplate.getForEntity(uri, RoomAddResponseDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/enter")
    public ResponseEntity<RoomEnterResponseDto> roomEnter(
            @RequestBody RoomEnterRequestDto request,
            @RequestParam("seesionid") String sessionId
    ) {
        String sessionid = null;

        RoomEnterResponseDto response = RoomEnterResponseDto.builder()
                .code(String.valueOf(HttpStatus.OK))
                .msg("정상적으로 채팅방에 입장하였습니다.")
                .data(sessionId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
