package com.performworld.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventPeriodStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventPeriodEnd;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime openDatetime;
}
