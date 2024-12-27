package com.performworld.service.event;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.performworld.dto.event.EventSearchDTO;
import com.performworld.dto.event.EventSearchListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Log4j2
@RequiredArgsConstructor
public class EventService {
    @Value("${performdata.api.url}")
    private String apiUrl;

    @Value("${performdata.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public EventSearchListDTO getPerformances(String stdate, String eddate, String shprfnm, String signgucode, int Page, int Size) throws Exception {
        log.info("API 호출을 시작합니다...");

        // 파라미터를 URL에 안전하게 추가
        String url = String.format("%s?service=%s&stdate=%s&eddate=%s&cpage=%d&rows=%d&signgucode=%s&shprfnm=%s",
                apiUrl, apiKey, stdate, eddate, Page,Size,signgucode, shprfnm);

        log.info("API 호출 URL: {}", url);

        try {
            // API 호출 및 응답 받기
            String xmlResponse = restTemplate.getForObject(url, String.class);


            // 응답 XML -> DTO 변환
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(xmlResponse, EventSearchListDTO.class);
        } catch (Exception e) {
            log.error("API 호출 또는 데이터 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("공공데이터 API 호출 실패", e);
        }
    }
}
