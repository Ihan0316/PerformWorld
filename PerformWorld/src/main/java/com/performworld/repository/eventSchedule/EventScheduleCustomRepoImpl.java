package com.performworld.repository.eventSchedule;

import com.performworld.domain.EventSchedule;
import com.performworld.domain.QEvent;
import com.performworld.domain.QEventSchedule;
import com.performworld.dto.event.EventScheduleDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventScheduleCustomRepoImpl extends QuerydslRepositorySupport implements EventScheduleCustomRepo{
    public EventScheduleCustomRepoImpl() {
        super(EventSchedule.class);
    }

    // 특정 공연 날짜별 회차 목록 조회
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
                        eventSchedule.scheduleId
                        , event.eventId
                        , eventSchedule.eventDate
                        , event.casting
                ).fetch();
        return result.stream().map(tuple -> EventScheduleDTO.builder()
                        .scheduleId(tuple.get(eventSchedule.scheduleId))
                        .eventId(tuple.get(event.eventId))
                        .eventDate(tuple.get(eventSchedule.eventDate))
                        .eventCast(tuple.get(event.casting))
                        .build())
                .collect(Collectors.toList());
    }
}
