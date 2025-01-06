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
public class TicketingInfoDTO {
    private String eventName;
    private String genre;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    private LocalDateTime openDatetime;  // 티켓팅 오픈 일시
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventPeriodStart;  // 오픈 공연 회차 시작일
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventPeriodEnd;  // 오픈 공연 회차 종료일
}
