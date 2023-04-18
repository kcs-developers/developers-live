package com.developers.live.session.service;

import com.developers.live.session.dto.dailyco.DailyCoDeleteRequest;
import com.developers.live.session.dto.dailyco.DailyCoResponse;

public interface DailyCoService {
    String create(Long nbf, Long exp);
    DailyCoResponse delete(DailyCoDeleteRequest request);
}
