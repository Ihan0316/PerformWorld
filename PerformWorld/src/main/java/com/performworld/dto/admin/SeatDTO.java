package com.performworld.dto.admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDTO {
    private String seatId;  // 좌석 ID (필수 여부가 명확하지 않으므로 유효성 검사 생략)

    @NotNull(message = "섹션은 null일 수 없습니다.")
    private String section; // 좌석 구역

    @NotNull(message = "가격은 null일 수 없습니다.")
    @Positive(message = "가격은 0보다 큰 값이어야 합니다.")
    private Long price;     // 좌석 가격
}