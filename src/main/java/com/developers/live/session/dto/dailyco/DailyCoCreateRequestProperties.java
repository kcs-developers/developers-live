package com.developers.live.session.dto.dailyco;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DailyCoCreateRequestProperties {
    private Long nbf;
    private Long exp;
}
