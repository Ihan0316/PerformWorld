package com.performworld.repository.tier;

import com.performworld.domain.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TierRepository extends JpaRepository<Tier, Long> {
    // 필요한 쿼리 메소드 추가 가능
}
