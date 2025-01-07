package com.performworld.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private String address;
    private Long totalPrice;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime regDate;
    private PaymentDTO payment;  // 예매 결제 정보
    private Integer resultCode;  // 예매하기 결과 리턴용
    private String eventInfo;  // 공연정보 출력용
    private String eventLocation;  // 공연장
    private String eventDate;  // 공연날짜
    private String eventCast;  // 캐스팅
    private String userName;
}
