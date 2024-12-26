package com.performworld.repository.user;

import com.performworld.domain.User;
import com.performworld.dto.user.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    // 이메일로 사용자 조회 (회원가입 시 사용)
    Optional<User> findByEmail(String email);

    // tier 포함하여 내 정보 조회
    @Query("SELECT new com.performworld.dto.user.UserDto(u.userId, u.name, u.email, u.password, u.phoneNumber, u.address1, u.address2, u.postcode, t.tierName) " +
            "FROM User u LEFT JOIN u.tier t WHERE u.userId = :userId")
    UserDto findUserByUserId(@Param("userId") String userId);
}