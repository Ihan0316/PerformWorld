package com.performworld.repository.board;

import com.performworld.domain.QnA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnA, Long>, BoardCustomRepo {
    void findByQnaId(Long qnaId);

}



