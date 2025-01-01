package com.performworld.dto.admin;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDTO {
    private String seatId;  // 좌석 ID
    private String section; // 좌석 구역
    private Long price;     // 좌석 가격
}