package com.performworld.repository.board;

import com.performworld.domain.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findByQuestionContaining(String keyword); // 질문에 특정 단어가 포함된 FAQ 조회
}
