package com.performworld.repository.user;

import com.performworld.domain.User;
import com.performworld.dto.user.UserDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);


    // tier 포함하여 조회 (User)
    @EntityGraph(attributePaths = {"tier"})
    Optional<User> findByUserId(String userId);

    // tier 포함하여 내 정보 조회 (UserDTO)
    @Query("SELECT new com.performworld.dto.user.UserDTO(u.userId, u.name, u.email, u.password, u.phoneNumber, u.address1, u.address2, u.postcode, t.tierName) " +
            "FROM User u LEFT JOIN u.tier t WHERE u.userId = :userId")
    UserDTO findUserByUserId(@Param("userId") String userId);

    @EntityGraph(attributePaths = "roleSet")
    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.tier IS NOT NULL")
    Optional<User> getWithRoles(@Param("userId") String userId);
}