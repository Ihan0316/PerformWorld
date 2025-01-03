package com.performworld.dto.board;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequestDTO {

    // noticeId를 반환하는 메서드 추가
    @Getter
    private Long noticeId; // 공지 ID (수정 시 필요)
    private String title; // 공지 제목
    private String content; // 공지 내용

}




