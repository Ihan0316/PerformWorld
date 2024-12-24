package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "faq")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FAQ extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long faqId;

    @Column(name = "question", nullable = false)
    private String question;  // 질문

    @Column(name = "answer", columnDefinition = "TEXT", nullable = false)
    private String answer;  // 답변
}
