package com.performworld.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long paymentId;
    private Long bookingId;
    private String payMethod;
    private Long payAmount;
    private LocalDateTime paymentDate;
    private String status;
}
