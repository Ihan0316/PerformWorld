package com.performworld.service.event;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.performworld.domain.Event;
import com.performworld.domain.Image;
import com.performworld.domain.SystemCode;
import com.performworld.dto.event.EventDTO;
import com.performworld.dto.event.EventSavedListDTO;
import com.performworld.dto.event.EventSearchListDTO;
import com.performworld.repository.event.EventRepository;
import com.performworld.repository.image.ImageRepository;
import com.performworld.repository.systemcode.SystemCodeRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
    @Value("${performdata.api.url}")
    private String apiUrl;

    @Value("${performdata.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final EventRepository eventRepository;
    private final SystemCodeRepository systemCodeRepository;
    private final ImageRepository imageRepository;

    @Override
    public EventSearchListDTO getPerformances(String stdate, String eddate, String shprfnm, String signgucode, String genre, int Page, int Size) {
        log.info("API 호출을 시작합니다...");

        String url = String.format("%s?service=%s&stdate=%s&eddate=%s&cpage=%d&rows=%d&signgucode=%s&shcate=%s&shprfnm=%s",
                apiUrl, apiKey, stdate, eddate, Page, Size, signgucode,genre, shprfnm);

        log.info("API 호출 URL: {}", url);

        try {
            String xmlResponse = restTemplate.getForObject(url, String.class);

            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(xmlResponse, EventSearchListDTO.class);
        } catch (Exception e) {
            log.error("API 호출 또는 데이터 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("공공데이터 API 호출 실패", e);
        }
    }

    @Override
    public String getEventDetails(String eventID) {
        String url = String.format("%s/%s?service=%s", apiUrl, eventID, apiKey);
        return restTemplate.getForObject(url, String.class);
    }

    @Override
    @Transactional
    public void saveEvent(String eventXml) {
        EventDTO eventDTO = parseXmlToEventDTO(eventXml);
        Event event = convertToEntity(eventDTO);

        Optional<Event> existingEvent = eventRepository.findByTitle(event.getTitle());
        if (existingEvent.isPresent()) {
            throw new DuplicateEventException("이미 존재하는 공연 제목입니다");
        }

        log.info("saveEvent Service단" + event);
        eventRepository.save(event);
    }

    private EventDTO parseXmlToEventDTO(String eventXml) {
        try {
            eventXml = eventXml.replaceAll("&(?!amp;|lt;|gt;|quot;)(?![a-zA-Z#0-9]{1,10};)", "&amp;");

            JAXBContext jaxbContext = JAXBContext.newInstance(EventDTO.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (EventDTO) unmarshaller.unmarshal(new StringReader(eventXml));
        } catch (Exception e) {
            log.error("XML parsing error: {}", eventXml);
            throw new RuntimeException("XML 데이터를 EventDTO로 변환하는 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private Event convertToEntity(EventDTO dto) {
        SystemCode category = systemCodeRepository.findByCodeName(dto.getGenreName())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 코드가 존재하지 않습니다: " + dto.getGenreName()));

        int runtimeMinutes = parseRuntimeToMinutes(dto.getRuntime());

        Event event = Event.builder()
                .mt20id(dto.getMt20id())
                .category(category)
                .title(dto.getTitle())
                .prfpdfrom(dto.getPrfpdfrom())
                .prfpdto(dto.getPrfpdto())
                .casting(dto.getCasting())
                .location(dto.getFcltynm())
                .luntime(runtimeMinutes)
                .images(new ArrayList<>())
                .build();

        String posterUrl = dto.getPoster();
        if (posterUrl != null && !posterUrl.isEmpty()) {
            Image image = Image.builder()
                    .filePath(posterUrl)
                    .isThumbnail(true)
                    .event(event)
                    .build();
            event.getImages().add(image);
        }

        List<String> imageUrls = dto.getImageUrls();
        for (String imageUrl : imageUrls) {
            Image image = Image.builder()
                    .filePath(imageUrl)
                    .isThumbnail(false)
                    .event(event)
                    .build();
            event.getImages().add(image);
        }

        log.info("save이벤트에 넘어온 데이터" + event);
        return event;
    }

    private int parseRuntimeToMinutes(String prfruntime) {
        if (prfruntime == null || prfruntime.isEmpty()) return 0;

        Matcher matcher = Pattern.compile("(\\d+)시간\\s*(\\d*)분?").matcher(prfruntime);
        if (matcher.find()) {
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = matcher.group(2).isEmpty() ? 0 : Integer.parseInt(matcher.group(2));
            return hours * 60 + minutes;
        }

        matcher = Pattern.compile("(\\d+)분?").matcher(prfruntime);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            log.warn("공연 시간 형식이 올바르지 않습니다: {}", prfruntime);
            return 0;
        }
    }

    public class DuplicateEventException extends RuntimeException {
        public DuplicateEventException(String message) {
            super(message);
        }
    }

    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private EventDTO convertToDTO(Event event) {
        EventDTO.EventDTOBuilder eventDTOBuilder = EventDTO.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .prfpdfrom(event.getPrfpdfrom())
                .prfpdto(event.getPrfpdto())
                .casting(event.getCasting())
                .fcltynm(event.getLocation())
                .runtime(String.valueOf(event.getLuntime()));


        Optional<SystemCode> systemCode = systemCodeRepository.findByCode(event.getCategory().getCode());
        systemCode.ifPresent(code -> eventDTOBuilder.genreName(code.getCodeName()));

        Optional<Image> thumbnailImage = imageRepository.findByEventEventIdAndIsThumbnailTrue(event.getEventId()).stream().findFirst();
        thumbnailImage.ifPresent(image -> eventDTOBuilder.poster(image.getFilePath()));

        List<String> imageUrls = imageRepository.findByEventEventId(event.getEventId()).stream()
                .map(Image::getFilePath)
                .collect(Collectors.toList());
        eventDTOBuilder.imageUrls(imageUrls);

        return eventDTOBuilder.build();
    }

    @Override
    @Transactional
    public void deleteEvents(List<Long> eventIds) {
        eventRepository.deleteAllById(eventIds); // 여러 이벤트 ID를 받아서 삭제
    }

    @Override
    public List<EventSavedListDTO> getSavedEventList(String title, String genre) {
        List<Event> eventList;

        // title과 genre 조건에 따라 쿼리 실행
        if (title != null && !title.isEmpty() && genre != null && !genre.isEmpty()) {
            eventList = eventRepository.findByTitleContainingAndCategory_Code(title, genre);
        } else if (title != null && !title.isEmpty()) {
            eventList = eventRepository.findByTitleContaining(title);
        } else if (genre != null && !genre.isEmpty()) {
            eventList = eventRepository.findByCategory_Code(genre);
        } else {
            eventList = eventRepository.findAll(); // 조건 없이 모든 이벤트 반환
        }

        // ModelMapper를 사용하여 Event 엔티티를 EventSavedListDTO로 변환
        ModelMapper modelMapper = new ModelMapper();
        List<EventSavedListDTO> eventSavedListDTOList = eventList.stream().map(event -> {
            EventSavedListDTO dto = modelMapper.map(event, EventSavedListDTO.class);
            // genre 값 설정 (카테고리 코드명)
            dto.setGenre(event.getCategory() != null ? event.getCategory().getCodeName() : null);
            // thumbnailUrl 설정 (썸네일 이미지)
            dto.setThumbnailUrl(event.getImages() != null && !event.getImages().isEmpty() ?
                    event.getImages().stream().filter(Image::isThumbnail).findFirst().map(Image::getFilePath).orElse(null) :
                    null);
            return dto;
        }).collect(Collectors.toList());

        return eventSavedListDTOList;
    }

    // 목록에 포스트 이미지 조회
    @Override
    public EventDTO getOneEvents(Long eventId) {
        Event events = eventRepository.findById(eventId).orElseThrow();
        return convertToDTO(events);
    }

    // 상세 페이지에 상세이미지 조회
    @Override
    public List<String> getDtlImages(Long eventId) {
        return imageRepository.findByEventEventIdAndIsThumbnailFalse(eventId).stream()
                .map(Image::getFilePath)
                .collect(Collectors.toList());
    }

    // 각 카테고리 기능
    @Override
    public List<EventDTO> getEventList(String genre) {
        List<Event> events = eventRepository.findByCategory_Code(genre);
        System.out.println("Events retrieved from database: " + events);

        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}
