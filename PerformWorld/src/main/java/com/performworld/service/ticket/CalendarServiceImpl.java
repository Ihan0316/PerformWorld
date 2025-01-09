package com.performworld.service.ticket;

import com.performworld.dto.ticket.CalendarEventDTO;
import com.performworld.repository.ticket.CalendarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;

    public CalendarServiceImpl(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    @Override
    public List<CalendarEventDTO> findBookedDatesByUserIdAndStatus(String userId, String status) {
        // 예매된 날짜와 제목을 가져오는 예시
        List<CalendarEventDTO> bookedData = calendarRepository.findBookedDatesAndTitlesByUserIdAndStatus(userId, status);

        // 예매된 날짜를 "yyyy-MM-dd HH:mm:ss.SSSSSS" 형식으로 반환
        return bookedData;
    }
}