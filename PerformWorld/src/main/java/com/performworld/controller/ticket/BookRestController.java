package com.performworld.controller.ticket;

import com.performworld.dto.admin.SeatDTO;
import com.performworld.dto.admin.TierDTO;
import com.performworld.dto.ticket.TicketingDTO;
import com.performworld.service.admin.SeatService;
import com.performworld.service.admin.TierService;
import com.performworld.service.event.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;
    private final SeatService seatService;
    private final TierService tierService;

    // 5분 내로 시작되는 특정 공연 티켓팅 조회
    @PostMapping("/getRecentTicketing")
    public List<TicketingDTO> getRecentTicketing(@RequestBody TicketingDTO ticketingDTO) {
        log.info(ticketingDTO);
        return bookService.findRecentTicketing(ticketingDTO.getEventId());
    }

    // 특정 공연의 오픈된 모든 티켓팅 조회
    @PostMapping("/getEventTicketing")
    public List<TicketingDTO> getEventTicketing(@RequestBody TicketingDTO ticketingDTO) {
        log.info(ticketingDTO);
        return bookService.getEventTicketing(ticketingDTO.getEventId());
    }

    // 좌석 목록 조회
    @PostMapping("/getSeatList")
    public List<SeatDTO> getSeatList() {
        return seatService.getAllSeats();
    }

    // 등급 정보 조회
    @PostMapping("/getUserTier")
    public TierDTO getUserTier(@RequestBody TierDTO tierDTO) {
        return tierService.getUserTier(tierDTO.getUserId());
    }
}
