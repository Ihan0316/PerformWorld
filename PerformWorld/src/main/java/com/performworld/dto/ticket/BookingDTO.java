package com.performworld.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {

    private Long bookingId;
    private String userId;
    private Long scheduleId;
    private List<String> seatIds;
    private boolean isDelivery;
    private Long totalPrice;
    private String status;
    private PaymentDTO payment;  // 예매 결제 정보
    private Integer resultCode;  // 예매하기 결과 리턴용
    private String eventInfo;  // 후기 작성 시 공연정보 출력용
}
