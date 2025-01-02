package com.performworld.repository.event;

import com.performworld.dto.ticket.BookingDTO;
import com.performworld.dto.ticket.TicketingDTO;

import java.util.List;

public interface BookCustomRepo {

    List<TicketingDTO> findRecentTicketing(Long eventId);

    List<TicketingDTO> getEventTicketing(Long eventId);

    List<BookingDTO> getBookedList(Long scheduleId);
}
