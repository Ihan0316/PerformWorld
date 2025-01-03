package com.performworld.dto.admin;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingResponseDTO<T> {
    private List<T> content;      // 페이징된 데이터 리스트
    private long totalElements;   // 전체 데이터 개수
    private int totalPages;       // 전체 페이지 개수
    private int currentPage;      // 현재 페이지 번호
}