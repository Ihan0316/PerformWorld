package com.performworld.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long reviewId;
    private String userId;
    private Long bookingId;
    private String eventInfo;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime regDate;
    private String srhEvent;  // 공연명 검색용
    private String srhUserId;  // 작성자 검색용
}
