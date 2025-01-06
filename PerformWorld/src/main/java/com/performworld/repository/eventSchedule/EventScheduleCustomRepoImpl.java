package com.performworld.repository.eventSchedule;

import com.performworld.domain.*;
import com.performworld.dto.event.EventScheduleDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventScheduleCustomRepoImpl extends QuerydslRepositorySupport implements EventScheduleCustomRepo{
    public EventScheduleCustomRepoImpl() {
        super(EventSchedule.class);
    }

    // 특정 공연 날짜별 회차 목록 조회 (예매 좌석 정보 포함)
    @Override
    public List<EventScheduleDTO> getScheduleList(EventScheduleDTO scheduleDTO) {
        QEventSchedule eventSchedule = QEventSchedule.eventSchedule;
        QEvent event = QEvent.event;

        LocalDateTime startOfDay = scheduleDTO.getSearchDate().atStartOfDay();  // 00:00:00
        LocalDateTime endOfDay = scheduleDTO.getSearchDate().atTime(23, 59, 59);  // 23:59:59

        List<Tuple> result = from(eventSchedule)
                .leftJoin(eventSchedule.event, event)
                .where(eventSchedule.event.eventId.eq(scheduleDTO.getEventId()))
                .where(eventSchedule.eventDate.between(startOfDay, endOfDay))
                .orderBy(eventSchedule.scheduleId.asc())
                .select(
                        eventSchedule.scheduleId,
                        event.eventId,
                        eventSchedule.eventDate,
                        event.casting
                ).fetch();

        // 회차 정보
        List<EventScheduleDTO> scheduleList = result.stream()
                .map(tuple -> EventScheduleDTO.builder()
                        .scheduleId(tuple.get(eventSchedule.scheduleId))
                        .eventId(scheduleDTO.getEventId())
                        .eventDate(tuple.get(eventSchedule.eventDate))
                        .eventCast(tuple.get(event.casting))
                        .seats(new ArrayList<>())  // 좌석 정보는 나중에 추가
                        .build())
                .collect(Collectors.toList());

        // 예매 좌석 정보
        Map<Long, List<String>> seatMap = getBookedSeats(scheduleDTO.getEventId(), startOfDay, endOfDay);

        // 데이터 합치기
        return scheduleList.stream()
                .map(eventScheduleDTO -> {
                    eventScheduleDTO.setSeats(seatMap.getOrDefault(eventScheduleDTO.getScheduleId(), new ArrayList<>()));
                    return eventScheduleDTO;
                })
                .collect(Collectors.toList());
    }

    private Map<Long, List<String>> getBookedSeats(Long eventId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        QEventSchedule eventSchedule = QEventSchedule.eventSchedule;
        QBooking booking = QBooking.booking;
        QSeat seat = QSeat.seat;

        List<Tuple> reservedSeats = from(booking)
                .leftJoin(booking.eventSchedule, eventSchedule)
                .on(booking.status.code.in("Y", "P"))
                .leftJoin(booking.seats, seat)
                .where(eventSchedule.event.eventId.eq(eventId))
                .where(eventSchedule.eventDate.between(startOfDay, endOfDay))
                .select(eventSchedule.scheduleId, seat.seatId)
                .fetch();

        // scheduleId로 그룹화
        return reservedSeats.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(eventSchedule.scheduleId),
                        Collectors.mapping(tuple -> tuple.get(seat.seatId), Collectors.toList())
                ));
    }
}
