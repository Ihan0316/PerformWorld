package com.performworld.service.ticket;

import com.performworld.domain.Ticketing;
import com.performworld.dto.ticket.TicketingDTO;
import com.performworld.dto.ticket.TicketingSaveDTO;

import java.util.List;

public interface TicketService {
    List<TicketingSaveDTO> findTicketingByEventId(Long eventId);

    void saveTicketingData(List<TicketingSaveDTO> ticketingData);

    void deleteTicketingData(Long eventId);
}
