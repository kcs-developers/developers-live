package com.developers.live.session.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SessionRedisSaveResponse {
    private String code;
    private String msg;
    private String room;
    private String name;
    private String url;
}
