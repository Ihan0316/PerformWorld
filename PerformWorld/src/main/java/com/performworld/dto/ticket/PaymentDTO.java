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

    private String paymentId;
    private Long bookingId;
    private String paymentMethod;
    private Integer paymentAmount;
    private LocalDateTime paymentDate;
    private String email;
    private String status;
}
