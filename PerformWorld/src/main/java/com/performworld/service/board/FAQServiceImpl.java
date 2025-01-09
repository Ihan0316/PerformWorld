package com.performworld.service.board;

import com.performworld.domain.FAQ;
import com.performworld.dto.board.FAQDTO;
import com.performworld.repository.board.FAQRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

    private final FAQRepository faqRepository;
    private final ModelMapper modelMapper;

    // FAQ 목록 조회
    @Override
    public List<FAQDTO> getAllFAQs() {
        return faqRepository.findAll().stream()
                .map(faq -> modelMapper.map(faq, FAQDTO.class))
                .collect(Collectors.toList());
    }

    // FAQ 등록
    @Override
    public void saveFAQ(FAQDTO faqDTO) {
        FAQ faq = FAQ.builder()
                .question(faqDTO.getQuestion())
                .answer(faqDTO.getAnswer())
                .build();
        faqRepository.save(faq);  // DB에 저장
    }

    // FAQ 삭제
    @Override
    public void deleteFAQ(Long faqId) {
        faqRepository.deleteById(faqId);
    }

    @Override
    public void updateFAQ(FAQDTO faqDTO) {
        FAQ faq = FAQ.builder()
                .faqId(faqDTO.getFaqId())
                .question(faqDTO.getQuestion())
                .answer(faqDTO.getAnswer())
                .build();

        faqRepository.save(faq);
    }
}


