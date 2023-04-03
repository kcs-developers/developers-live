package com.developers.live.mentoring.service;

import com.developers.live.mentoring.dto.RegisterRequestDto;
import com.developers.live.mentoring.dto.RegisterResponseDto;

public interface RegisterService {

  RegisterResponseDto register(RegisterRequestDto request);
}
