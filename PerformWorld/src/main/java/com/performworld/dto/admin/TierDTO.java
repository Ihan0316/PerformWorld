package com.performworld.dto.admin;

import com.performworld.domain.Tier;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TierDTO {

    private Long tierId;

    @NotNull(message = "Tier 이름은 필수입니다.")
    private String tierName;

    @Min(value = 0, message = "최소 금액은 0 이상이어야 합니다.")
    private Long minSpent;

    @Min(value = 0, message = "최대 금액은 0 이상이어야 합니다.")
    private Long maxSpent;

    @DecimalMin(value = "0", message = "할인율은 0 이상이어야 합니다.")
    @DecimalMax(value = "100", message = "할인율은 100 이하이어야 합니다.")
    private Long discountRate;

    private String userId;

    // 생성자에서 유효성 검사 추가
    public TierDTO(Long tierId, String tierName, Long minSpent, Long maxSpent, Long discountRate) {
        if (minSpent >= maxSpent) {
            throw new IllegalArgumentException("Min Spent 는 Max Spent 보다 작아야 됩니다.");
        }
        this.tierId = tierId;
        this.tierName = tierName;
        this.minSpent = minSpent;
        this.maxSpent = maxSpent;
        this.discountRate = discountRate;
    }

    // Tier 엔티티에서 정보를 받아오는 생성자
    public TierDTO(Tier updatedTier) {
        this.tierName = updatedTier.getTierName();
        this.minSpent = updatedTier.getMinSpent();
        this.maxSpent = updatedTier.getMaxSpent();
        this.discountRate = updatedTier.getDiscountRate();
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