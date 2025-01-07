package com.performworld.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.performworld.domain.Event;
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
public class TicketingSaveDTO {
    private Long ticketingId;
    private Long eventId;  // Event 객체를 포함
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime openDatetime;  // 티켓팅 오픈 일시
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventPeriodStart;  // 오픈 공연 회차 시작일
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventPeriodEnd;  // 오픈 공연 회차 종료일
}
