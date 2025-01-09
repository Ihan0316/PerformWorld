package com.performworld.service.admin;

import com.performworld.domain.Tier;
import com.performworld.dto.admin.TierDTO;
import com.performworld.repository.admin.TierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        return tiers.stream()
                .map(TierDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 티어 등록
    @Override
    public TierDTO addTier(TierDTO tierDTO) {
        Tier tier = tierDTO.toEntity();
        return TierDTO.fromEntity(tierRepository.save(tier));
    }

    // 회원 등급 조회
    @Override
    public TierDTO getUserTier(String userId) {
        return tierRepository.getUserTier(userId);
    }

    // 특정 tierId에 해당하는 Tier 조회
    @Override
    public TierDTO getTierById(Long tierId) {
        Optional<Tier> tierOptional = tierRepository.findById(tierId);
        if (tierOptional.isPresent()) {
            return TierDTO.fromEntity(tierOptional.get());
        } else {
            throw new IllegalArgumentException("존재하지 않는 Tier입니다.");
        }
    }
}