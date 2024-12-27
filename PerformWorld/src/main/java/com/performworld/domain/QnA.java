package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qna")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnA extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long qnaId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user; // Users 테이블과의 관계

    @Setter
    @Column(name = "title", nullable = false)
    private String title; // 문의 제목

    @Setter
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // 문의 내용

    @Setter
    @Column(name = "response", columnDefinition = "TEXT")
    private String response; // 답변 내용 (nullable)

    @Setter
    @Column(name = "response_datetime")
    private LocalDateTime responseDatetime; // 답변 날짜 및 시간

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일

    @Setter
    private LocalDateTime updatedAt; // 수정일

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.response != null && this.responseDatetime == null) {
            this.responseDatetime = LocalDateTime.now();
        }
    }
}

