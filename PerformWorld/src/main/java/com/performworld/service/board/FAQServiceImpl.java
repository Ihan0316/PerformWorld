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

    @Override
    public List<FAQDTO> getAllFAQs() {
        return faqRepository.findAll().stream()
                .map(faq -> modelMapper.map(faq, FAQDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void registerFAQ(FAQDTO faqDTO) {
        FAQ faq = modelMapper.map(faqDTO, FAQ.class);
        faqRepository.save(faq);
    }

    @Override
    public void updateFAQ(FAQDTO faqDTO) {
        FAQ faq = modelMapper.map(faqDTO, FAQ.class);
        faqRepository.save(faq);
    }

    @Override
    public FAQDTO getFAQById(Long faqId) {
        FAQ faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found for id: " + faqId));
        return modelMapper.map(faq, FAQDTO.class);
    }

    @Override
    public void deleteFAQ(Long faqId) {
        faqRepository.deleteById(faqId);
    }

    @Override
    public List<FAQ> searchFAQsByKeyWord(String keyword) {
        return faqRepository.findByQuestionContaining(keyword);
    }
}


