package com.developers.live.session.service;

import com.developers.live.session.dto.dailyco.DailyCoResponse;

public interface DailyCoService {
    String create();
    DailyCoResponse delete(String roomName);
}
