package com.developers.live.session.dto.dailyco;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DailyCoCreateRequest {
    @NotNull(message = "멘토만 방을 생성할 수 있습니다")
    private Long userId;
    @NotNull
    private Long scheduleId;
    @NotNull
    private String privacy;
    private DailyCoCreateRequestProperties properties;
}
