package com.performworld.dto.tier;

import com.performworld.domain.Tier;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TierDTO {

    private Long tierId;
    private String tierName;
    private Long minSpent;
    private Long maxSpent;
    private Long discountRate;

    // 엔티티를 DTO로 변환하는 메소드
    public static TierDTO fromEntity(Tier tier) {
        // minSpent가 maxSpent보다 크면 예외를 던질 수 있도록 추가
        if (tier.getMinSpent() >= tier.getMaxSpent()) {
            throw new IllegalArgumentException("Min Spent should be less than Max Spent");
        }

        return TierDTO.builder()
                .tierId(tier.getTierId())
                .tierName(tier.getTierName())
                .minSpent(tier.getMinSpent())
                .maxSpent(tier.getMaxSpent())
                .discountRate(tier.getDiscountRate())
                .build();
    }

    // DTO에서 엔티티로 변환하는 메소드 (optional)
    public Tier toEntity() {
        return Tier.builder()
                .tierId(this.tierId)
                .tierName(this.tierName)
                .minSpent(this.minSpent)
                .maxSpent(this.maxSpent)
                .discountRate(this.discountRate)
                .build();
    }
}
