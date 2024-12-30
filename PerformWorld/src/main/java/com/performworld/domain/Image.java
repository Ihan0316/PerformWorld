package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
@Getter
//@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Event event;  // Events 테이블과의 관계

    @Column(name = "file_path", nullable = false)
    private String filePath;  // 파일 경로

    @Column(name = "is_thumbnail", nullable = false)
    private boolean isThumbnail;  // 썸네일 여부
}
