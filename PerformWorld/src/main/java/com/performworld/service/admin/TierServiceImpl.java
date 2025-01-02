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
        // 유효성 검사 (minSpent가 maxSpent보다 큰지 체크)
        if (tierDTO.getMinSpent() >= tierDTO.getMaxSpent()) {
            throw new IllegalArgumentException("Min Spent should be less than Max Spent");
        }

        // DTO를 엔티티로 변환하여 DB에 저장
        Tier tier = tierDTO.toEntity();
        Tier savedTier = tierRepository.save(tier);

        // 저장된 엔티티를 DTO로 다시 변환하여 반환
        return TierDTO.fromEntity(savedTier);
    }

    @Override
    public Tier getTierById(Long tierId) {
        return tierRepository.findById(tierId)
                .orElseThrow(() -> new IllegalArgumentException("Tier not found with id: " + tierId));
    }
}