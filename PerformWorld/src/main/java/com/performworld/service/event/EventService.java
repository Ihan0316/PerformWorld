package com.performworld.service.event;

import com.performworld.dto.event.EventDTO;
import com.performworld.dto.event.EventSearchListDTO;
import java.util.List;

public interface EventService {

    EventSearchListDTO getPerformances(String stdate, String eddate, String shprfnm, String signgucode, int Page, int Size);
    String getEventDetails(String eventID);
    void saveEvent(String eventXml);
    List<EventDTO> getAllEvents();
    void deleteEvent(Long eventId);

}
