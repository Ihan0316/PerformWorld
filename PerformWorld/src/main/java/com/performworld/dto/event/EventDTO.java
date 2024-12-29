package com.performworld.dto.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    private Long eventId;       // Event ID
    private String category;    // Category 코드 (SystemCode 테이블 참조)
    private String title;       // 공연/전시 제목
    private String location;    // 장소
    private Integer luntime;    // 공연 시간 (분 단위)
    private String imagePath;   // 이미지 경로 또는 URL
}
