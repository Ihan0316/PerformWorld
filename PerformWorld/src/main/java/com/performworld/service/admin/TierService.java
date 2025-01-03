package com.performworld.service.admin;

import com.performworld.dto.admin.TierDTO;
import com.performworld.domain.Tier;

import java.util.List;

public interface TierService {

    List<TierDTO> getAllTiers(); // 전체 티어 목록 조회

    TierDTO addTier(TierDTO tierDTO); // 티어 등록

    TierDTO updateTier(Long tierId, TierDTO tierDTO);

    void deleteTierById(Long tierId); // 삭제 메서드 추가

    TierDTO getTierById(Long tierId);
}
