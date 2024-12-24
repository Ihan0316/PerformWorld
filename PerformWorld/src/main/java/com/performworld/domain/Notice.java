package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "title", nullable = false)
    private String title;  // 공지 제목

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;  // 공지 내용
}
