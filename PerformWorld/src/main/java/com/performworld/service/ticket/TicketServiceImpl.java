package com.performworld.service.ticket;

import com.performworld.domain.Event;
import com.performworld.domain.Ticketing;
import com.performworld.dto.ticket.TicketingDTO;
import com.performworld.dto.ticket.TicketingSaveDTO;
import com.performworld.repository.event.EventRepository;
import com.performworld.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    @Override
    public List<TicketingDTO> findByEventID(Long eventId) {
        return ticketRepository.findByEvent_EventId(eventId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TicketingDTO convertToDto(Ticketing ticketing) {
        return TicketingDTO.builder()
                .eventId(ticketing.getEvent().getEventId())
                .openDatetime(ticketing.getOpenDatetime())
                .eventPeriodStart(ticketing.getEventPeriodStart())
                .eventPeriodEnd(ticketing.getEventPeriodEnd())
                .build();
    }

    @Override
    public void saveTicketingData(List<TicketingSaveDTO> ticketingData) {
        for (TicketingSaveDTO ticketingDTO : ticketingData) {
            log.info("serviceimpl에서 :"+ticketingDTO);
            Event event = eventRepository.findById(ticketingDTO.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found with ID: " + ticketingDTO.getEventId()));

            Ticketing ticketing = Ticketing.builder()
                    .event(event)
                    .openDatetime(ticketingDTO.getOpenDatetime())
                    .eventPeriodStart(ticketingDTO.getEventPeriodStart())
                    .eventPeriodEnd(ticketingDTO.getEventPeriodEnd())
                    .build();

            // Ticketing 엔티티 저장
            ticketRepository.save(ticketing);
        }
    }
}
