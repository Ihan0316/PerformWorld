package com.performworld.dto.event;

import com.performworld.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventSavedListDTO {
    private Long eventId;
    private String title;
    private String prfpdfrom;
    private String prfpdto;
    private String location;
    private String genre;
    private String thumbnailUrl;

    public EventSavedListDTO(Event event) {
        this.eventId = event.getEventId();
        this.title = event.getTitle();
        this.prfpdfrom = event.getPrfpdfrom();
        this.prfpdto = event.getPrfpdto();
        this.location = event.getLocation();
        this.genre = event.getCategory() != null ? event.getCategory().getCode() : null; // 카테고리 정보 처리
        this.thumbnailUrl = event.getImages() != null && !event.getImages().isEmpty() ? event.getImages().get(0).getFilePath() : null; // 첫 번째 이미지의 파일 경로
    }

}
