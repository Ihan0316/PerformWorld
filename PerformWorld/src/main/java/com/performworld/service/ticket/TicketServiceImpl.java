package com.performworld.service.ticket;

import com.performworld.domain.Event;
import com.performworld.domain.Ticketing;
import com.performworld.dto.ticket.TicketingDTO;
import com.performworld.dto.ticket.TicketingInfoDTO;
import com.performworld.dto.ticket.TicketingSaveDTO;
import com.performworld.repository.event.EventRepository;
import com.performworld.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    @Override
    public List<TicketingSaveDTO> findTicketingByEventId(Long eventId) {
        return ticketRepository.findTicketingByEvent_EventId(eventId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TicketingSaveDTO convertToDto(Ticketing ticketing) {
        return TicketingSaveDTO.builder()
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

    @Override
    public void deleteTicketingData(Long eventId) {
        ticketRepository.deleteTicketingByEvent_EventId(eventId);
    }

    @Override
    public List<TicketingInfoDTO> findAllList() {
        return ticketRepository.findAll().stream()
                .map(ticket -> TicketingInfoDTO.builder()
                        .eventName(ticket.getEvent().getTitle())  // 티켓의 제목을 eventName으로 매핑
                        .genre(ticket.getEvent().getCategory().getCodeName())  // 티켓의 장르를 genre으로 매핑
                        .openDatetime(ticket.getOpenDatetime())  // openDateTime을 LocalDateTime으로 변환
                        .eventPeriodStart(ticket.getEventPeriodStart())  // startDate를 LocalDate로 변환
                        .eventPeriodEnd(ticket.getEventPeriodEnd())  // endDate를 LocalDate로 변환
                        .build())
                .collect(Collectors.toList());  // 변환된 DTO들을 리스트로 반환
    }
}
