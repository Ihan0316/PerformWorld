package com.performworld.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDTO {
    private Long noticeId; // 공지사항 고유 ID
    private String title;  // 공지 제목
    private String content; // 공지 내용
    private LocalDateTime createdAt; // 등록일
    private LocalDateTime updatedAt; // 수정일
}


