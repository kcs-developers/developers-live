package com.developers.live.session.dto.dailyco;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DailyCoResponse {
    private String code;
    private String msg;
    private DailyCoAnswer data;
    private ResponseEntity<String> delete;
}
