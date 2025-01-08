package com.performworld.repository.admin;

import com.performworld.domain.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TierRepository extends JpaRepository<Tier, Long>, TierCustomRepo {
}