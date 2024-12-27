package com.performworld.service.event;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.performworld.dto.event.EventSearchDTO;
import com.performworld.dto.event.EventSearchListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class EventService {
    @Value("${performdata.api.url}")
    private String apiUrl;

    @Value("${performdata.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public EventSearchDTO searchEvents(String performName, String startDate, String endDate, String locationCode) throws Exception {
        // 검색 파라미터를 URL에 맞게 조합
        String url = String.format("%s?serviceKey=%s&stdate=%s&eddate=%s&shprfnm=%s&signgucode=%s",
                apiUrl, apiKey, startDate, endDate, performName, locationCode);

        // XML 응답을 받아서 DTO로 변환
        String xmlResponse = restTemplate.getForObject(url, String.class);
        XmlMapper xmlMapper = new XmlMapper();
        EventSearchListDTO eventSearchListDTO = xmlMapper.readValue(xmlResponse, EventSearchListDTO.class);

        // EventSearchDTO로 변환하여 반환
//        EventSearchDTO eventSearchDTO = new EventSearchDTO();
//        eventSearchDTO.setPerformances(eventSearchListDTO.getPerformances());
//        return eventSearchDTO;

        // EventSearchListDTO에서 Performance 리스트를 가져와 EventSearchDTO로 변환
        List<EventSearchListDTO.Performance> eventPerformances = eventSearchListDTO.getPerformances();
        List<EventSearchDTO.Performance> eventSearchDTOPerformances = new ArrayList<>();

// Performance 객체들을 변환하여 eventSearchDTOPerformances 리스트에 추가
        for (EventSearchListDTO.Performance performance : eventPerformances) {
            EventSearchDTO.Performance dtoPerformance = new EventSearchDTO.Performance();

            // 필요한 속성들을 변환하여 dtoPerformance에 설정
            dtoPerformance.setPoster(performance.getPoster());
            dtoPerformance.setMt20id(performance.getMt20id());
            dtoPerformance.setPrfnm(performance.getPrfnm());
            dtoPerformance.setPrfpdfrom(performance.getPrfpdfrom());
            dtoPerformance.setPrfpdto(performance.getPrfpdto());
            dtoPerformance.setArea(performance.getArea());
            dtoPerformance.setGenrenm(performance.getGenrenm());

            // 변환된 객체를 리스트에 추가
            eventSearchDTOPerformances.add(dtoPerformance);
        }

// EventSearchDTO에 변환된 데이터를 설정
        EventSearchDTO eventSearchDTO = new EventSearchDTO();
        eventSearchDTO.setPerformances(eventSearchDTOPerformances);

// 변환된 EventSearchDTO 반환
        return eventSearchDTO;
    }

    public EventSearchListDTO getPerformances(String stdate, String eddate, String shprfnm, String signgucode) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        log.info("apiUrl"+apiUrl);
        log.info("apiKey"+apiKey);
        log.info("shprfnm"+shprfnm);

        String url = apiUrl + "?service=" + apiKey
                + "&stdate=" + stdate
                + "&eddate=" + eddate
                + "&cpage=1"
                + "&rows=10"
                + "&signgucode=" + signgucode
                + "&shprfnm=" + shprfnm;

        log.info("API 호출 URL: {}", url);

        try {
            // API 호출 및 응답 받기
            String xmlResponse = restTemplate.getForObject(url, String.class);

            // 응답 XML -> DTO 변환
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(xmlResponse, EventSearchListDTO.class);

        } catch (Exception e) {
            log.error("API 호출 또는 데이터 파싱 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("공공데이터 API 호출 실패", e);
        }
    }
}
