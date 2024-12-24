package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "qna")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnA extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long qnaId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;  // Users 테이블과의 관계

    @Column(name = "title", nullable = false)
    private String title;  // 문의 제목

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;  // 문의 내용

    @Column(name = "response", columnDefinition = "TEXT")
    private String response;  // 답변 내용 (nullable)

    @Column(name = "response_date")
    private LocalDateTime responseDate;  // 답변 날짜 (nullable)

}
