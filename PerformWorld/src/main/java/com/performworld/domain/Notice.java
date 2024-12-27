package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;  // 공지사항 ID

    @Column(nullable = false, length = 255)
    private String title;  // 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;  // 내용

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성 시간

    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정 시간

    // 생성될 때, createdAt과 updatedAt을 설정
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // 업데이트 될 때, updatedAt만 갱신
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // title과 content는 setter를 사용하여 값을 설정할 수 있도록 수정
    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("Title cannot be empty");
        }
    }

    public void setContent(String content) {
        if (content != null && !content.trim().isEmpty()) {
            this.content = content;
        } else {
            throw new IllegalArgumentException("Content cannot be empty");
        }
    }
}