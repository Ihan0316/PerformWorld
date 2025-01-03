package com.performworld.repository.admin;

import com.performworld.domain.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TierRepository extends JpaRepository<Tier, Long>, TierCustomRepo {
    boolean existsByTierName(String tierName); // 티어 중복 확인
}