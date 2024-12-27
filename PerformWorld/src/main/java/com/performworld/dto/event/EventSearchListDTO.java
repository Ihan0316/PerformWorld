package com.performworld.dto.event;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "dbs") // 최상위 XML 태그 이름 설정
public class EventSearchListDTO {

    @JacksonXmlElementWrapper(useWrapping = false) // <db> 태그가 배열 형태임을 명시
    @JacksonXmlProperty(localName = "db") // 내부 태그 이름 매핑
    private List<Performance> performances;

    @Data
    public static class Performance {
        @JacksonXmlProperty(localName = "mt20id")
        private String mt20id; // 공연 id

        @JacksonXmlProperty(localName = "prfnm")
        private String prfnm; // 공연명

        @JacksonXmlProperty(localName = "prfpdfrom")
        private String prfpdfrom; // 공연시작일

        @JacksonXmlProperty(localName = "prfpdto")
        private String prfpdto; //공연종료일

        @JacksonXmlProperty(localName = "fcltynm")
        private String fcltynm; //공연장소

        @JacksonXmlProperty(localName = "poster")
        private String poster; //공연포스터 url

        @JacksonXmlProperty(localName = "area")
        private String area;  //공연지역

        @JacksonXmlProperty(localName = "genrenm")
        private String genrenm; // 공연장르

        @JacksonXmlProperty(localName = "openrun")
        private String openrun; //오픈런

        @JacksonXmlProperty(localName = "prfstate")
        private String prfstate; //공연상태
    }
}
