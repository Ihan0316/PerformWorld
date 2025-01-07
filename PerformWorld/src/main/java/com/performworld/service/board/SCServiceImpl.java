package com.performworld.service.board;

import com.performworld.dto.board.FAQDTO;
import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.QnADTO;
import com.performworld.repository.board.FAQRepository;
import com.performworld.repository.board.NoticeRepository;
import com.performworld.repository.board.QnARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class SCServiceImpl implements SCService {

    private final NoticeRepository noticeRepository;
    private final FAQRepository faqRepository;
    private final QnARepository qnARepository;
    private final ModelMapper modelMapper;

    @Override
    public List<NoticeDTO> getNotice(NoticeDTO noticeDTO) {
        return noticeRepository.findAll().stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FAQDTO> getFaq(FAQDTO faqDTO) {
        return faqRepository.findAll().stream()
                .map(faq -> modelMapper.map(faq, FAQDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<QnADTO> getQna(QnADTO qnaDTO) {
        return qnARepository.findAll().stream()
                .map(qna -> modelMapper.map(qna, QnADTO.class))
                .collect(Collectors.toList());
    }
}

