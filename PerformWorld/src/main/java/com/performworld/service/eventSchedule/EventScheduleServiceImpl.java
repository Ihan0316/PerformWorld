package com.performworld.service.eventSchedule;

import com.performworld.domain.Event;
import com.performworld.domain.EventSchedule;
import com.performworld.repository.event.EventRepository;
import com.performworld.repository.eventSchedule.EventScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventScheduleServiceImpl implements EventScheduleService {
    private final EventScheduleRepository eventScheduleRepository;
    private final EventRepository eventRepository;

    @Override
    public void saveSchedulesFromXml(String xmlData) {
        try {
            // XML 파싱
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

            // 필드 추출
            String prfpdfrom = doc.getElementsByTagName("prfpdfrom").item(0).getTextContent();
            String prfpdto = doc.getElementsByTagName("prfpdto").item(0).getTextContent();
            String dtguidance = doc.getElementsByTagName("dtguidance").item(0).getTextContent();
            String mt20id = doc.getElementsByTagName("mt20id").item(0).getTextContent();

            // Event 조회
            Event event = eventRepository.findByMt20id(mt20id)
                    .orElseThrow(() -> new RuntimeException("Event not found for mt20id: " + mt20id));
            log.info("Event found: {}", event);

            // 날짜 및 주간 일정 파싱
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate startDate = parseDate(prfpdfrom, formatter);
            LocalDate endDate = parseDate(prfpdto, formatter);
            Map<DayOfWeek, List<LocalTime>> weeklySchedule = parseGuidance(dtguidance);

            // 스케줄 생성
            List<EventSchedule> schedules = new ArrayList<>();
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                if (weeklySchedule.containsKey(dayOfWeek)) {
                    for (LocalTime time : weeklySchedule.get(dayOfWeek)) {
                        schedules.add(EventSchedule.builder()
                                .event(event) // Event 설정
                                .eventDate(LocalDateTime.of(date, time))
                                .build());
                    }
                }
            }

            // 스케줄 저장
            eventScheduleRepository.saveAll(schedules);
            log.info("{} schedules saved", schedules.size());

        } catch (Exception e) {
            log.error("Error saving event schedules: {}", e.getMessage(), e);
            throw new RuntimeException("이벤트 스케쥴 저장 중 에러 발생 " + e.getMessage(), e);
        }
    }

    private LocalDate parseDate(String dateStr, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            log.error("Error parsing date: {}", dateStr, e);
            throw new IllegalArgumentException("Invalid date format: " + dateStr);
        }
    }

    private Map<DayOfWeek, List<LocalTime>> parseGuidance(String guidance) {
        Map<DayOfWeek, List<LocalTime>> scheduleMap = new HashMap<>();

        log.info("dtguidance: {}", guidance);

        // dtguidance를 콤마로 분리
        String[] daySchedules = guidance.split(",");
        for (String daySchedule : daySchedules) {
            // 공백 제거
            daySchedule = daySchedule.trim();

            // 괄호가 포함된 경우만 처리
            if (daySchedule.contains("(")) {
                // 요일 범위 및 시간 분리
                String[] parts = daySchedule.split("\\(");
                if (parts.length < 2) {
                    log.error("Invalid dtguidance format, missing times: {}", daySchedule);
                    continue;  // 유효하지 않은 형식은 건너뜁니다
                }

                String days = parts[0].trim();  // 요일 범위 (예: "화요일 ~ 금요일")
                String times = parts[1].replaceAll("\\)$", "").trim();  // 마지막 괄호 제거 후 시간들 (예: "14:00, 15:30, 17:00")

                log.info("시간들: {}", times);

                // 시간들이 쉼표로 구분되기 때문에 쉼표로 분리하고 각 시간을 Trim하여 공백을 제거
                List<LocalTime> timesList = parseTimes(times);  // 시간 파싱 로직을 별도 메서드로 분리

                // 요일 범위에서 각 요일을 추출
                List<DayOfWeek> daysOfWeek = parseDays(days);

                // 각 요일에 대해 해당 시간 리스트 추가
                for (DayOfWeek dayOfWeek : daysOfWeek) {
                    scheduleMap.put(dayOfWeek, timesList);
                }
            } else {
                log.warn("Invalid day schedule without time: {}", daySchedule);
            }
        }
        return scheduleMap;
    }




    private List<DayOfWeek> parseDays(String days) {
        List<DayOfWeek> dayList = new ArrayList<>();
        String[] dayRange = days.split("~");

        if (dayRange.length == 1) {
            // 하나의 요일인 경우, 그 요일만 추가
            dayList.add(getDayOfWeek(dayRange[0].trim()));
        } else if (dayRange.length == 2) {
            // 요일 범위인 경우, 첫 번째 요일부터 마지막 요일까지 추가
            DayOfWeek startDay = getDayOfWeek(dayRange[0].trim());
            DayOfWeek endDay = getDayOfWeek(dayRange[1].trim());

            // 요일 순서가 올바르게 처리되도록 하기 위해 `nextDay()` 함수를 호출합니다.
            for (DayOfWeek day = startDay; day != endDay; day = nextDay(day)) {
                dayList.add(day);
            }
            dayList.add(endDay); // 마지막 요일도 추가
        } else {
            throw new IllegalArgumentException("Invalid day range format: " + days);
        }
        return dayList;
    }

    private DayOfWeek nextDay(DayOfWeek current) {
        // current.ordinal()은 0부터 시작하므로, 순환 범위 내에서 +1 후 % 7로 계산
        int nextOrdinal = (current.ordinal() + 1) % 7;
        return DayOfWeek.of(nextOrdinal + 1); // 1부터 7까지 DayOfWeek는 1-based
    }

    // 요일을 DayOfWeek로 변환하는 메서드
    private DayOfWeek getDayOfWeek(String day) {
        switch (day) {
            case "월요일": return DayOfWeek.MONDAY;
            case "화요일": return DayOfWeek.TUESDAY;
            case "수요일": return DayOfWeek.WEDNESDAY;
            case "목요일": return DayOfWeek.THURSDAY;
            case "금요일": return DayOfWeek.FRIDAY;
            case "토요일": return DayOfWeek.SATURDAY;
            case "일요일": return DayOfWeek.SUNDAY;
            default: throw new IllegalArgumentException("알 수 없는 요일: " + day);
        }
    }

    private List<LocalTime> parseTimes(String timeString) {
        List<LocalTime> times = new ArrayList<>();

        // 시간 문자열에서 괄호를 제거하는 정규식 사용
        timeString = timeString.replaceAll("[()]", "");  // 괄호 제거

        // 시간 문자열을 콤마로 분리하여 각각 LocalTime으로 변환
        String[] timeArray = timeString.split(",");
        for (String time : timeArray) {
            time = time.trim();  // 앞뒤 공백 제거

            if (!time.isEmpty()) {
                try {
                    log.info("파싱 전 시간: {}", time); // 파싱 전 시간 출력 (디버깅용)
                    times.add(LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")));
                } catch (DateTimeParseException e) {
                    log.error("시간 파싱 오류: {}", time, e);  // 오류 발생 시 로그
                    throw new IllegalArgumentException("잘못된 시간 형식: " + time);
                }
            }
        }
        return times;
    }
}
