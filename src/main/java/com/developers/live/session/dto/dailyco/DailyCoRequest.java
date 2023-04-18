package com.developers.live.session.dto.dailyco;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DailyCoRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long scheduleId;
    @NotNull
    private String privacy;
    private DailyCoCreateRequestProperties properties;
}
