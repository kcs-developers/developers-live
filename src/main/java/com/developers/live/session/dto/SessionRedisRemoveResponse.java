package com.developers.live.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SessionRedisRemoveResponse {
    private String code;
    private String msg;
    private String data;
}
