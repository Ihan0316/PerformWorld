package com.performworld.controller.ticket;

import com.performworld.domain.Ticketing;
import com.performworld.dto.ticket.TicketingDTO;
import com.performworld.dto.ticket.TicketingSaveDTO;
import com.performworld.service.ticket.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/ticketing")
@RequiredArgsConstructor
public class TicketRestController {


    private final TicketService ticketService;

    @GetMapping("/{eventID}")
    public ResponseEntity<List<TicketingDTO>> getTicketingByEventId(@PathVariable Long eventId) {
        List<TicketingDTO> ticketingList = ticketService.findByEventID(eventId);
        return ResponseEntity.ok(ticketingList);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveTicketing(@RequestBody List<TicketingSaveDTO> ticketingData) {
        log.info("컨트롤러로 넘어온 list: " + ticketingData);  // 데이터가 제대로 넘어오는지 확인
        ticketService.saveTicketingData(ticketingData);
        return ResponseEntity.ok("{ \"status\": \"success\" }");
    }

}
