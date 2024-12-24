package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @OneToOne
    @JoinColumn(name = "category", referencedColumnName = "code")
    private SystemCode category;  // SystemCode 테이블과의 연관

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "luntime")
    private Integer luntime;  // 공연 시간 (분 단위)

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "image_id")
    private Image image;  // Images 테이블과의 연관
}
