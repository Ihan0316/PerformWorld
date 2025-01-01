package com.performworld.dto.admin;

import com.performworld.domain.Tier;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@ToString
public class TierDTO {

    private Long tierId;
    private String tierName;
    private Long minSpent;
    private Long maxSpent;
    private Long discountRate;

    // 생성자에서 유효성 검사 추가
    public TierDTO(Long tierId, String tierName, Long minSpent, Long maxSpent, Long discountRate) {
        if (minSpent >= maxSpent) {
            throw new IllegalArgumentException("Min Spent should be less than Max Spent");
        }
        this.tierId = tierId;
        this.tierName = tierName;
        this.minSpent = minSpent;
        this.maxSpent = maxSpent;
        this.discountRate = discountRate;
    }

    // 엔티티를 DTO로 변환하는 메소드
    public static TierDTO fromEntity(Tier tier) {
        return new TierDTO(
                tier.getTierId(),
                tier.getTierName(),
                tier.getMinSpent(),
                tier.getMaxSpent(),
                tier.getDiscountRate()
        );
    }

    // DTO에서 엔티티로 변환하는 메소드 (optional)
    public Tier toEntity() {
        // tierId는 신규 생성 시 null로 처리되므로 필드 값이 새로 지정된다고 가정
        return Tier.builder()
                .tierId(null)  // tierId는 DB에서 자동 생성되므로 null로 처리
                .tierName(this.tierName)
                .minSpent(this.minSpent)
                .maxSpent(this.maxSpent)
                .discountRate(this.discountRate)
                .build();
    }
}