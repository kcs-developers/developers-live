package com.developers.live.session.service;

import com.developers.live.session.dto.*;

public interface SessionService {
    SessionRedisSaveResponse enter(SessionRedisSaveRequest request);
    SessionRedisFindAllResponse list();

    SessionRedisRemoveResponse remove(SessionRedisRemoveRequest request);
}
