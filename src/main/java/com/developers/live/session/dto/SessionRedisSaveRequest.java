package com.developers.live.session.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionRedisSaveRequest {
    @NotNull
    private String roomName;
    @NotNull
    private String userName;
    @NotNull
    private Long userId; // userId로 비교하기 있기 때문

    @NotNull
    private Long time;

    @NotNull
    private Long scheduleId; // 스케쥴 정보로 조회 필요
}
