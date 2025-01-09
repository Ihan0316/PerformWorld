package com.performworld.repository.event;

import com.performworld.domain.Event;
import com.performworld.dto.event.EventDTO;
import com.performworld.dto.event.EventSavedListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findByTitle(String title);

    @Query("SELECT new com.performworld.dto.event.EventSavedListDTO(e.eventId, e.title, e.prfpdfrom, e.prfpdto, e.location, sc.codeName, " +
            "   (SELECT i.filePath FROM e.images i WHERE i.isThumbnail = true ORDER BY i.imageId LIMIT 1)) " +
            "FROM Event e " +
            "JOIN e.category sc " +
            "LEFT JOIN e.images i")
    Page<EventSavedListDTO> findAllWithThumbnailAndCategory(Pageable pageable);

    // 제목과 카테고리로 이벤트 검색 (Event 엔티티 반환)
    List<Event> findByTitleContainingAndCategory_Code(String title, String genre);

    // 제목으로 이벤트 검색 (Event 엔티티 반환)
    List<Event> findByTitleContaining(String title);

    // 목록 카테고리 검색
    List<Event> findByCategory_Code(String genre);

    // 모든 이벤트 반환 (Event 엔티티 반환)
    List<Event> findAll();

    Optional<Event> findByMt20id(String externalId);


}



