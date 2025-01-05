package com.performworld.domain;

import com.performworld.repository.image.ImageRepository;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "events")
@Getter
@ToString(exclude = "images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "mt20id", unique = true, nullable = false)
    private String mt20id; // 공연 코드

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "code")
    private SystemCode category;  // SystemCode 테이블과의 연관

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name="prfpdfrom", length = 255)
    private String prfpdfrom; //공연 시작일

    @Column(name="prfpdto", length = 255)
    private String prfpdto; // 공연 종료일

    @Column(name="casting", length = 255)
    private String casting;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "luntime")
    private Integer luntime;  // 공연 시간 (분 단위)

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> images = new ArrayList<>();  // Images 테이블과의 연관

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EventSchedule> eventSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Ticketing> ticketings = new ArrayList<>();
}
