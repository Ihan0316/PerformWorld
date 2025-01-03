package com.performworld.repository.eventSchedule;

import com.performworld.domain.EventSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventScheduleRepository  extends JpaRepository<EventSchedule, Long>, EventScheduleCustomRepo {
}
