package com.performworld.service.board;

import com.performworld.dto.board.FAQDTO;

import java.util.List;

public interface FAQService {

    List<FAQDTO> getAllFAQs();

    void saveFAQ(FAQDTO faqDTO);

    void deleteFAQ(Long faqId);

    void updateFAQ(FAQDTO faqDTO);
}
