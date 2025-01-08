package com.performworld.dto.ticket;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
public class CalendarEventDTO {
    private String dateTime;  // 날짜와 시간을 포함한 문자열
    private String title;

    public CalendarEventDTO(String dateTime, String title) {
        this.dateTime = dateTime;
        this.title = title;
    }

    public String getFormattedDate() {
        // dateTime을 LocalDateTime으로 변환
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @Override
    public String toString() {
        return "CalendarEventDTO{" +
                "dateTime='" + dateTime + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}