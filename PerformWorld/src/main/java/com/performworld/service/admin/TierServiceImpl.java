package com.performworld.service.admin;

import com.performworld.domain.Tier;
import com.performworld.dto.admin.TierDTO;
import com.performworld.repository.admin.TierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TierServiceImpl implements TierService {

    private final TierRepository tierRepository;

    // 전체 티어 목록 조회
    @Override
    public List<TierDTO> getAllTiers() {
        List<Tier> tiers = tierRepository.findAll();
        List<TierDTO> tierDTOs = tiers.stream()
                .map(TierDTO::fromEntity)
                .collect(Collectors.toList());
        return tierDTOs;
    }

    // 티어 등록
    @Override
    public TierDTO addTier(TierDTO tierDTO) {
        // 유효성 검사는 컨트롤러에서 처리
        Tier tier = tierDTO.toEntity();
        return TierDTO.fromEntity(tierRepository.save(tier));
    }
}