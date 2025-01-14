package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event_schedules")
@Getter
//@ToString(exclude = "event")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Event event;  // Events 테이블과의 연관

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;  // 공연 날짜

    @OneToMany(mappedBy = "eventSchedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
}
