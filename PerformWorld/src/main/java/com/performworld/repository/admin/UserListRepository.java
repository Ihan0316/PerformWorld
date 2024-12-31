package com.performworld.repository.admin;

import com.performworld.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserListRepository extends JpaRepository<User, String> {
}