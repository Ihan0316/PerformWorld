package com.performworld.repository.ticket;

import com.performworld.domain.Ticketing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticketing,Long> {
    List<Ticketing> findTicketingByEvent_EventId(Long eventId);


    @Transactional
    @Modifying
    @Query("DELETE FROM Ticketing t WHERE t.event.eventId = :eventId")
    void deleteTicketingByEvent_EventId(@Param("eventId") Long eventId);
}
