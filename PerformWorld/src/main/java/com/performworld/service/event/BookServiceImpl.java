package com.performworld.service.event;

import com.performworld.dto.ticket.TicketingDTO;
import com.performworld.repository.event.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    // 오픈된 티켓팅 목록 조회 (예매 페이지용)
    @Override
    public List<TicketingDTO> getEventTicketing(Long eventId) {
        return bookRepository.getEventTicketing(eventId);
    }

}
