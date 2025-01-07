package com.performworld.service.board;

import com.performworld.domain.FAQ;
import com.performworld.dto.board.FAQDTO;
import com.performworld.dto.board.FaqSaveDTO;

import java.util.List;

public interface FAQService {

    // FAQ 목록 조회
    List<FAQDTO> getAllFAQs();

    // FAQ 등록
    void registerFAQ(FAQDTO faqDTO);

    // FAQ 수정
    void updateFAQ(FAQDTO faqDTO);

    // FAQ 상세조회
    FAQDTO getFAQById(Long faqId);

    // FAQ 삭제
    void deleteFAQ(Long faqId);

    List<FAQ> searchFAQsByKeyWord(String keyword);

    // FAQ 저장 메서드
    void saveFAQ(FaqSaveDTO faqSaveDTO);

}
