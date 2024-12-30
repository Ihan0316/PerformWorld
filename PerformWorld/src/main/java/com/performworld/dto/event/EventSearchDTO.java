package com.performworld.dto.event;

import lombok.Data;

import java.util.List;

@Data
public class EventSearchDTO {
    private List<Performance> performances;

    @Data
    public static class Performance {
        private String mt20id; // 공연 ID
        private String prfnm;  // 공연 이름
        private String prfpdfrom; // 공연 시작일
        private String prfpdto; // 공연 종료일
        private String area;   // 지역
        private String genrenm; // 장르
        private String poster; // 썸네일 이미지 URL
    }
}
