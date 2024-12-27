package com.performworld.repository;

import com.performworld.domain.QnA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnARepository extends JpaRepository<QnA, Long> {
    // 제목으로 검색
    List<QnA> findByTitleContaining(String title);

    // 유저 ID로 검색
    List<QnA> findByUserId(Long userId);
}

