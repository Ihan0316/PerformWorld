package com.performworld.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketingDTO {
    private Long eventId;
    private String eventName;
    private Long ticketingId;
    private LocalDateTime openDatetime;
    private LocalDate eventPeriodStart;
    private LocalDate eventPeriodEnd;
}
