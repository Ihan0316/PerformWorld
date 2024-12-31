package com.performworld.service.event;

import com.performworld.dto.event.EventDTO;
import com.performworld.dto.event.EventSavedListDTO;
import com.performworld.dto.event.EventSearchListDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EventService {

    EventSearchListDTO getPerformances(String stdate, String eddate, String shprfnm, String signgucode, int Page, int Size);
    String getEventDetails(String eventID);
    void saveEvent(String eventXml);
    List<EventDTO> getAllEvents();
    void deleteEvent(Long eventId);
    List<EventSavedListDTO> getAllEventsWithThumbnails();

    Page<EventSavedListDTO> getSavedEventList(int page, int size);

}
