package com.performworld.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnADTO {
    private Long qnaId;
    private Long userId; // userId만 포함

    private String title; // 문의 제목
    private String content; // 문의 내용
    private String response; // 답변 내용 (nullable)
    private LocalDateTime responseDatetime; // 답변 날짜 및 시간

    private LocalDateTime createdAt; // 등록일
    private LocalDateTime updatedAt; // 수정일
}

