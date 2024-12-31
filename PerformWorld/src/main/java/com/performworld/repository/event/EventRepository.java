package com.performworld.repository.event;

import com.performworld.domain.Event;
import com.performworld.dto.event.EventSavedListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findByTitle(String title);

    @Query("SELECT new com.performworld.dto.event.EventSavedListDTO(e.eventId, e.title, e.prfpdfrom, e.prfpdto, e.location, sc.codeName, i.filePath) " +
            "FROM Event e " +
            "JOIN e.category sc " +
            "LEFT JOIN e.images i ON i.isThumbnail = true")
    List<EventSavedListDTO> findAllWithThumbnailAndCategory();
}
