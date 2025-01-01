package com.performworld.service.admin;

import com.performworld.dto.admin.TierDTO;
import com.performworld.domain.Tier;

import java.util.List;

public interface TierService {

    List<TierDTO> getAllTiers(); // 전체 티어 목록 조회

    TierDTO addTier(TierDTO tierDTO); // 티어 등록

    Tier getTierById(Long tierId); // tierId로 Tier 조회 (추가된 메서드)
}