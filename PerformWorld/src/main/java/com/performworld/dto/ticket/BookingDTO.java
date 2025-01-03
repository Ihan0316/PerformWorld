package com.performworld.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {

    private Long bookingId;
    private String userId;
    private Long scheduleId;
    private String seatId;
    private boolean isDelivery;
    private Long totalPrice;
    private String status;
}
