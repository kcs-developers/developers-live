package com.developers.live.session.service;

import com.developers.live.session.dto.dailyco.DailyCoCreateRequest;
import com.developers.live.session.dto.dailyco.DailyCoResponse;

public interface DailyCoService {
    DailyCoResponse create(DailyCoCreateRequest request);
    DailyCoResponse delete(String roomName);
}
