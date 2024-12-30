package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "event_schedules")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Event event;  // Events 테이블과의 연관

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;  // 공연 날짜

    @Column(name = "event_cast", length = 100)
    private String eventCast;  // 공연 출연진

}
