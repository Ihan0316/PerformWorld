package com.performworld.service.event;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.performworld.domain.Event;
import com.performworld.domain.Image;
import com.performworld.domain.SystemCode;
import com.performworld.dto.event.EventDTO;
import com.performworld.dto.event.EventSearchListDTO;
import com.performworld.repository.event.EventRepository;
import com.performworld.repository.systemcode.SystemCodeRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Log4j2
@RequiredArgsConstructor
public class EventService {
    @Value("${performdata.api.url}")
    private String apiUrl;

    @Value("${performdata.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    private final EventRepository eventRepository;
    private final SystemCodeRepository systemCodeRepository;

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


    public String getEventDetails(String eventID) {
        // 상세 조회 API 호출
        String url = String.format("%s/%s?service=%s", apiUrl, eventID, apiKey);
        return restTemplate.getForObject(url, String.class);
    }

    @Transactional
    public void saveEvent(String eventXml) {

        // XML 데이터를 DTO로 변환
        EventDTO eventDTO = parseXmlToEventDTO(eventXml);

        // DTO를 엔티티로 변환
        Event event = convertToEntity(eventDTO);
        log.info("saveEvent Service단"+event);
        // 데이터베이스에 저장
        eventRepository.save(event);
    }

    private EventDTO parseXmlToEventDTO(String eventXml) {
        try {
            // JAXBContext 생성
            JAXBContext jaxbContext = JAXBContext.newInstance(EventDTO.class);

            // Unmarshaller를 사용해 XML 데이터를 EventDTO로 변환
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (EventDTO) unmarshaller.unmarshal(new StringReader(eventXml));
        } catch (Exception e) {
            throw new RuntimeException("XML 데이터를 EventDTO로 변환하는 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private Event convertToEntity(EventDTO dto) {
        // SystemCode 조회 (카테고리 및 지역)
        log.info("장르:"+dto.getGenreName());
        log.info("코드:"+dto.getLocation());
        SystemCode category = systemCodeRepository.findByCodeName(dto.getGenreName())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 코드가 존재하지 않습니다: " + dto.getGenreName()));
        SystemCode location = systemCodeRepository.findByCodeName(dto.getLocation())
                .orElseThrow(() -> new IllegalArgumentException("지역 코드가 존재하지 않습니다: " + dto.getLocation()));

        // 공연 시간(분 단위) 변환
        int runtimeMinutes = parseRuntimeToMinutes(dto.getRuntime());

        // Event 엔티티 생성
        Event event = Event.builder()
                .category(category)
                .title(dto.getTitle())
                .casting(dto.getCasting())
                .location(location.getCodeName())
                .luntime(runtimeMinutes)
                .image(new ArrayList<>()) // 이미지는 아래에서 추가
                .build();

        String posterUrl = dto.getPoster();  // poster는 EventDTO에서 이미 매핑됨
        if (posterUrl != null && !posterUrl.isEmpty()) {
            Image image = Image.builder()
                    .filePath(posterUrl)
                    .isThumbnail(true) // 썸네일로 설정
                    .event(event) // 연관 관계 설정
                    .build();
            event.getImage().add(image);
        }

        // styurls 추출 (상세보기 이미지들)
        List<String> imageUrls = dto.getImageUrls();  // styurls는 EventDTO에서 매핑됨
        for (String imageUrl : imageUrls) {
            Image image = Image.builder()
                    .filePath(imageUrl)
                    .isThumbnail(false) // 썸네일이 아니므로 false
                    .event(event) // 연관 관계 설정
                    .build();
            event.getImage().add(image);
        }
        log.info("save이벤트에 넘어온 데이터"+event);
        return event;
    }


    private int parseRuntimeToMinutes(String prfruntime) {
        if (prfruntime == null || prfruntime.isEmpty()) return 0;

        // "시간"과 "분"을 포함하는 경우 (예: "1시간 40분")
        Matcher matcher = Pattern.compile("(\\d+)시간\\s*(\\d*)분?").matcher(prfruntime);
        if (matcher.find()) {
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = matcher.group(2).isEmpty() ? 0 : Integer.parseInt(matcher.group(2));
            return hours * 60 + minutes;
        }

        // "시간"이 없이 "분"만 있는 경우 (예: "40분")
        matcher = Pattern.compile("(\\d+)분?").matcher(prfruntime);
        if (matcher.find()) {
            int minutes = Integer.parseInt(matcher.group(1));
            return minutes;
        } else {
            log.warn("공연 시간 형식이 올바르지 않습니다: {}", prfruntime);
            return 0;
        }
    }
}
