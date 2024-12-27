package com.performworld.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventSearchListDTO {
    private String eventName;
    private LocalDate StartDate;
    private LocalDate EndDate;
    private String locationCode;
}
