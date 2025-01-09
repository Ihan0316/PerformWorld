package com.performworld.service.event;

import com.performworld.dto.ticket.TicketingDTO;

import java.util.List;

public interface BookService {

    List<TicketingDTO> getEventTicketing(Long eventId);

}
