package com.performworld.dto.board;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnARequestDTO {

    private Long qnaId;    // QnA 고유 ID (수정 시 사용)
    private Long userId;   // 유저 ID
    private String title;  // 문의 제목
    private String content; // 문의 내용
    private String response; // 답변 내용 (수정 시 사용)

    // 페이지 번호 반환
    @Getter
    @Builder.Default
    private int page = 0;  // 페이지 번호 (기본값 0)
    // 페이지 크기 반환
    @Getter
    @Builder.Default
    private int size = 10; // 페이지 크기 (기본값 10)
    @Setter
    private String keyword;

}






