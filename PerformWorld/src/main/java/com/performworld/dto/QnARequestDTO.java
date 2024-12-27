package com.performworld.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnARequestDTO {

    private Long qnaId;

    private Long userId; // userId를 Long 타입으로 변경 (String -> Long)

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;

    private String response; // 응답 내용

    private Integer page; // 페이지 번호 (Integer로 수정)

    private Integer size; // 페이지 크기

    // 기본 페이지 값은 1로 설정
    public int getPage() {
        return page != null ? page : 1;
    }

    // 기본 크기 값은 10으로 설정
    public int getSize() {
        return size != null ? size : 10;
    }
}





