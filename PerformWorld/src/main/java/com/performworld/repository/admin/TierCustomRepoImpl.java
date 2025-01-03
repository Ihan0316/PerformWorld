package com.performworld.repository.admin;

import com.performworld.domain.*;
import com.performworld.dto.admin.TierDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class TierCustomRepoImpl extends QuerydslRepositorySupport implements TierCustomRepo {
    public TierCustomRepoImpl() {
        super(Tier.class);
    }

    // 특정 회원의 등급 조회
    @Override
    public TierDTO getUserTier(String userId) {
        QTier tier = QTier.tier;
        QUser user = QUser.user;
        TierDTO tierDTO = new TierDTO();

        // 엔티티 객체 생성 없이 바로 Tuple로 받아오기
        Tuple result = from(user)
                .leftJoin(user.tier, tier)
                .where(user.userId.eq(userId))
                .select(
                        tier.tierId,
                        tier.tierName,
                        tier.discountRate,
                        user.userId
                )
                .fetchOne();

        // DTO로 변환
        if (result != null) {
            tierDTO = TierDTO.builder()
                    .tierId(result.get(tier.tierId))
                    .tierName(result.get(tier.tierName))
                    .discountRate(result.get(tier.discountRate))
                    .userId(result.get(user.userId))
                    .build();
        }

        return tierDTO;
    }
}
