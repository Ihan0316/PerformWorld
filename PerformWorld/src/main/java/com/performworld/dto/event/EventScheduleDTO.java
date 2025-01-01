package com.performworld.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventScheduleDTO {

    private Long scheduleId;
    private Long eventId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventDate;
    private String eventCast;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchDate;  // 날짜별 회차 조회용
}
