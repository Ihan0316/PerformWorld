package com.performworld.repository.user;

import com.performworld.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 조회 (회원가입 시 사용)
    Optional<User> findByEmail(String email);
}