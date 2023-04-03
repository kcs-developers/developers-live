package com.developers.live.developers.mentoring.controller;

import com.developers.live.developers.mentoring.dto.RegisterRequestDto;
import com.developers.live.developers.mentoring.dto.RegisterResponseDto;
import com.developers.live.developers.mentoring.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/register")
@RestController
public class RegisterController {

  private final RegisterService registerService;

  @PostMapping
  public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
    RegisterResponseDto response = registerService.register(request);
    log.info("멘티의 멘토링 신청 요청: " + response);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
