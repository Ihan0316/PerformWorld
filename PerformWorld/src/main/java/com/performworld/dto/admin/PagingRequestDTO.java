package com.performworld.dto.admin;

import lombok.Data;

@Data
public class PagingRequestDTO {
    private int page = 0;  // 기본값: 첫 번째 페이지
    private int size = 10; // 기본값: 페이지당 항목 수
}