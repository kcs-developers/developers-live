package com.developers.live.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SessionRedisFindAllResponse {
    private String code;
    private String msg;
    private String urls;
    private String users;
}
