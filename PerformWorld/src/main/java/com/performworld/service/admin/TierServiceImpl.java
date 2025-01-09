package com.performworld.service.admin;

import com.performworld.domain.Tier;
import com.performworld.domain.User;
import com.performworld.dto.admin.TierDTO;
import com.performworld.repository.admin.TierRepository;
import com.performworld.repository.admin.UserListRepository;
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
    private final UserListRepository userListRepository;

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

        // 새로운 티어를 저장
        Tier savedTier = tierRepository.save(tier);

        // 모든 사용자 가져오기
        List<User> users = userListRepository.findAll();

        // 각 사용자의 티어를 업데이트
        for (User user : users) {
            // 사용자 도메인을 수정하지 않고, 티어 정보를 업데이트
            user.chnTotalSpent(user.getTotalSpent(), tierRepository.findAll());
        }

        // 모든 사용자 정보를 저장
        userListRepository.saveAll(users);

        return TierDTO.fromEntity(savedTier);
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