package com.developers.live.session.dto.dailyco;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DailyCoDeleteRequest {
    @NotNull
    private String roomName;
    @NotNull(message = "멘토만 삭제 가능합니다!")
    private Long userId;
    @NotNull
    private Long scheduleId;
}
