package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tiers")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tier extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private Long tierId;

    @Column(name = "tier_name", nullable = false, length = 50)
    private String tierName;

    @Column(name = "min_spent", precision = 10, scale = 2)
    private Long minSpent;

    @Column(name = "max_spent", precision = 10, scale = 2)
    private Long maxSpent;

    @Column(name = "discount_rate", precision = 5, scale = 2)
    private Long discountRate;
}

