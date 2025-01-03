package com.performworld.service.admin;

import com.performworld.dto.admin.PagingResponseDTO;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public class PagingService {

    // Page 객체를 받아서, DTO로 변환된 결과와 페이징 정보를 반환하는 메서드
    public static <T, R> PagingResponseDTO<R> toPagingResponse(Page<T> page, Function<T, R> mapper) {
        return new PagingResponseDTO<>(
                page.getContent().stream().map(mapper).toList(),  // Page에서 content를 가져와서 DTO로 변환
                (int) page.getTotalElements(),  // 전체 데이터 개수
                page.getTotalPages(),           // 전체 페이지 개수
                page.getNumber()                // 현재 페이지 번호
        );
    }
}