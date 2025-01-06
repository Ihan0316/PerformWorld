package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "ticketing")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticketing extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticketing_id")
    private Long ticketingId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Event event;  // Events 테이블과의 관계

    @Column(name = "open_datetime", nullable = false)
    private LocalDateTime openDatetime;  // 티켓팅 오픈 일시

    @Column(name = "event_period_start", nullable = false)
    private LocalDate eventPeriodStart;  // 오픈 공연 회차 시작일

    @Column(name = "event_period_end", nullable = false)
    private LocalDate eventPeriodEnd;  // 오픈 공연 회차 종료일
}
