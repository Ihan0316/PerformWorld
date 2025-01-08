package com.performworld.service.admin;

import com.performworld.dto.admin.SeatDTO;

import java.util.List;

public interface SeatService {
    List<SeatDTO> getAllSeats();  // 모든 좌석 목록 조회
    void updateSectionPrice(String section, Long price);
    List<SeatDTO> getAllSectionsWithPrices();
}
