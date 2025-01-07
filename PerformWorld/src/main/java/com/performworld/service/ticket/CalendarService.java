package com.performworld.service.ticket;

import com.performworld.dto.ticket.CalendarEventDTO;

import java.util.List;

public interface CalendarService {

    List<CalendarEventDTO> findBookedDatesByUserIdAndStatus(String userId, String status);
}