package com.developers.live.developers.mentoring.service;

import com.developers.live.developers.mentoring.dto.RegisterRequestDto;
import com.developers.live.developers.mentoring.dto.RegisterResponseDto;

public interface RegisterService {

  RegisterResponseDto register(RegisterRequestDto request);
}
