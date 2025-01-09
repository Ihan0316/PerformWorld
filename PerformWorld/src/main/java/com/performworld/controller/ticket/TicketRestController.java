package com.performworld.controller.ticket;

import com.performworld.domain.Ticketing;
import com.performworld.dto.ticket.TicketingDTO;
import com.performworld.dto.ticket.TicketingInfoDTO;
import com.performworld.dto.ticket.TicketingSaveDTO;
import com.performworld.service.ticket.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/ticketing")
@RequiredArgsConstructor
public class TicketRestController {


    private final TicketService ticketService;

    @GetMapping("/{eventId}")
    public ResponseEntity<List<TicketingSaveDTO>> getTicketingByEventId(@PathVariable Long eventId) {
        try {
            // eventId에 대한 데이터 조회
            List<TicketingSaveDTO> ticketingList = ticketService.findTicketingByEventId(eventId);

            // 데이터가 없을 경우 처리
            if (ticketingList == null || ticketingList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ticketingList);
        } catch (Exception e) {
            // 예외 발생 시 500 에러 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveTicketing(@RequestBody List<TicketingSaveDTO> ticketingData) {
        Long eventID = ticketingData.get(0).getEventId();
        log.info("이벤트id:"+eventID);
        ticketService.deleteTicketingData(eventID);
        ticketService.saveTicketingData(ticketingData);
        return ResponseEntity.ok("{ \"status\": \"success\" }");
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteTicketing(@PathVariable Long eventId) {
        log.info("이벤트id:"+eventId);
        ticketService.deleteTicketingData(eventId);
        return ResponseEntity.ok("{ \"status\": \"success\" }");
    }

    // 모든 티켓 데이터를 가져오는 API 엔드포인트
    @GetMapping("/ticketingInfo")
    public List<TicketingInfoDTO> getAllTickets() {
        log.info("result:"+ticketService.findAllList());
        return ticketService.findAllList(); // 서비스 레이어에서 티켓 리스트 반환
    }
}
