package com.performworld.repository.event;

import com.performworld.domain.QEvent;
import com.performworld.domain.QTicketing;
import com.performworld.domain.Ticketing;
import com.performworld.dto.ticket.TicketingDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BookCustomRepoImpl extends QuerydslRepositorySupport implements BookCustomRepo {
    public BookCustomRepoImpl() {
        super(Ticketing.class);
    }

    // 해당 공연, 현 시간부터 5분 이내의 티켓팅 일정 조회
    @Override
    public List<TicketingDTO> findRecentTicketing(Long eventId) {
        QTicketing ticketing = QTicketing.ticketing;
        QEvent event = QEvent.event;

        // 엔티티 객체 생성 없이 바로 Tuple로 받아오기
        List<Tuple> result = from(ticketing)
                .leftJoin(ticketing.event, event)
                .where(ticketing.event.eventId.eq(eventId))
                .where(ticketing.openDatetime.between(
                        LocalDateTime.now(), LocalDateTime.now().plusMinutes(5)))

                .select(
                        ticketing.ticketingId
                        , ticketing.openDatetime
                        , ticketing.eventPeriodStart
                        , ticketing.eventPeriodEnd
                        , event.eventId
                        , event.title
                ).fetch();

        // DTO로 변환
        return result.stream().map(tuple -> TicketingDTO.builder()
                        .ticketingId(tuple.get(ticketing.ticketingId))
                        .openDatetime(tuple.get(ticketing.openDatetime))
                        .eventPeriodStart(tuple.get(ticketing.eventPeriodStart))
                        .eventPeriodEnd(tuple.get(ticketing.eventPeriodEnd))
                        .eventId(tuple.get(event.eventId))
                        .eventName(tuple.get(event.title))
                        .build())
                .collect(Collectors.toList());
    }

    // 해당 공연, 모든 티켓팅 조회
    @Override
    public List<TicketingDTO> getEventTicketing(Long eventId) {
        QTicketing ticketing = QTicketing.ticketing;
        QEvent event = QEvent.event;

        // 엔티티 객체 생성 없이 바로 Tuple로 받아오기
        List<Tuple> result = from(ticketing)
                .leftJoin(ticketing.event, event)
                .where(ticketing.event.eventId.eq(eventId))
                .orderBy(ticketing.ticketingId.asc())

                .select(
                        ticketing.ticketingId
                        , ticketing.openDatetime
                        , ticketing.eventPeriodStart
                        , ticketing.eventPeriodEnd
                        , event.eventId
                        , event.title
                ).fetch();

        // DTO로 변환
        return result.stream().map(tuple -> TicketingDTO.builder()
                        .ticketingId(tuple.get(ticketing.ticketingId))
                        .openDatetime(tuple.get(ticketing.openDatetime))
                        .eventPeriodStart(tuple.get(ticketing.eventPeriodStart))
                        .eventPeriodEnd(tuple.get(ticketing.eventPeriodEnd))
                        .eventId(tuple.get(event.eventId))
                        .eventName(tuple.get(event.title))
                        .build())
                .collect(Collectors.toList());
    }
}
