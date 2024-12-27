package com.performworld.service.tier;

import com.performworld.domain.Tier;
import com.performworld.dto.tier.TierDTO;
import com.performworld.repository.tier.TierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TierService {

    private final TierRepository tierRepository;

    // 전체 티어 목록 조회
    public List<TierDTO> getAllTiers() {
        List<Tier> tiers = tierRepository.findAll();
        List<TierDTO> tierDTOs = tiers.stream()
                .map(TierDTO::fromEntity)
                .collect(Collectors.toList());

        // 리스트의 크기만 먼저 출력하는 방식으로 개선
        log.info("TierService getAllTiers - Total Tiers: " + tierDTOs.size());

        // 특정 항목 몇 개를 출력하거나, 리스트의 일부만 출력
        tierDTOs.stream().limit(5).forEach(tier -> log.info("TierDTO: {}", tier));

        return tierDTOs;
    }

    // 특정 티어 조회
    public TierDTO getTierById(Long tierId) {
        Tier tier = tierRepository.findById(tierId)
                .orElseThrow(() -> new IllegalArgumentException("Tier not found with id: " + tierId));
        return TierDTO.fromEntity(tier);
    }

    // 티어 생성
    public TierDTO addTier(TierDTO tierDTO) {
        // 유효성 검사, 비즈니스 로직 추가 가능
        if (tierDTO.getMinSpent() >= tierDTO.getMaxSpent()) {
            throw new IllegalArgumentException("Min Spent should be less than Max Spent");
        }

        // Tier 객체 생성
        Tier tier = Tier.builder()
                .tierName(tierDTO.getTierName())
                .minSpent(tierDTO.getMinSpent())
                .maxSpent(tierDTO.getMaxSpent())
                .discountRate(tierDTO.getDiscountRate())
                .build();

        // DB에 저장
        Tier savedTier = tierRepository.save(tier);

        // 저장된 티어를 DTO로 변환하여 반환
        return TierDTO.fromEntity(savedTier);
    }

//    // 티어 업데이트
//    public TierDTO updateTier(Long tierId, TierDTO tierDTO) {
//        Tier tier = tierRepository.findById(tierId)
//                .orElseThrow(() -> new IllegalArgumentException("Tier not found with id: " + tierId));
//
//        tier.setTierName(tierDTO.getTierName());
//        tier.setMinSpent(tierDTO.getMinSpent());
//        tier.setMaxSpent(tierDTO.getMaxSpent());
//        tier.setDiscountRate(tierDTO.getDiscountRate());
//
//        Tier updatedTier = tierRepository.save(tier);
//        return TierDTO.fromEntity(updatedTier);
//    }

    // 티어 삭제
    public void deleteTier(Long tierId) {
        Tier tier = tierRepository.findById(tierId)
                .orElseThrow(() -> new IllegalArgumentException("Tier not found with id: " + tierId));
        tierRepository.delete(tier);
    }
}
