package com.performworld.repository;

import com.performworld.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);

    // 사용자 ID로 사용자 조회 (JpaRepository가 기본적으로 제공하므로 이 메서드는 불필요)
    // Optional<User> findById(Long userId); // 중복 제거
}
