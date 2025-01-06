package com.performworld.repository.ticket;

import com.performworld.domain.Ticketing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticketing,Long> {
    List<Ticketing> findByEvent_EventId(Long eventId);


}
