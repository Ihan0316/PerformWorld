package com.performworld.repository.systemcode;

import com.performworld.domain.SystemCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemCodeRepository extends JpaRepository<SystemCode,Long> {
    Optional<SystemCode> findByCodeName(String codeName);
}
