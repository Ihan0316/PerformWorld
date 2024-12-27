package com.performworld.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnAResponseDTO {
    private Long qnaId; // QnA 고유 ID
    private Long userId; // 유저 ID
    private String title; // 문의 제목
    private String content; // 문의 내용
    private String response; // 관리자 답변 (nullable)
    private LocalDateTime createdAt; // 등록일
    private LocalDateTime updatedAt; // 수정일
    private LocalDateTime responseDatetime; // 답변 날짜 (nullable)
}

