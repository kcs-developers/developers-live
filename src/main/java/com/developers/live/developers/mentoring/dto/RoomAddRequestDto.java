package com.developers.live.developers.mentoring.dto;

import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoomAddRequestDto {
    @NotNull
    private Long mentorId;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String description;

    // TODO: point validation 최소, 최댓값 결정 필요
    @NotNull
    @Min(0)
    @Max(100)
    private Long point;
}
