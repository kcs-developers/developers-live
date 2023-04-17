package com.developers.live.session.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SessionRedisRemoveRequest {
    @NotNull
    private String roomName;
    @NotNull(message = "멘토만이 방을 삭제할 수 있습니다")
    private Long userId;
    @NotNull
    private Long scheduleId;
}
