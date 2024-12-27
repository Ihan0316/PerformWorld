package com.performworld.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeRequestDTO {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;  // 공지사항 제목

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;  // 공지사항 내용
}


