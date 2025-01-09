package com.performworld.service.board;

import com.performworld.domain.QnA;
import com.performworld.domain.User;
import com.performworld.dto.board.QnADTO;

import com.performworld.repository.board.QnARepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final QnARepository qnaRepository;

    // Qna 목록 조회
    @Override
    public List<QnADTO> getQnas(QnADTO qnADTO) {
        return qnaRepository.getQnaList(qnADTO);
    }

    // Qna 등록
    @Override
    public void saveQnA(QnADTO qnADTO) {
        QnA qna = QnA.builder()
                .title(qnADTO.getTitle())
                .content(qnADTO.getContent())
                .user(User.builder().userId(qnADTO.getUserId()).build())
                .build();
        qnaRepository.save(qna);
    }

    // Qna 삭제
    @Override
    public void deleteQnA(Long qnaId) {
        qnaRepository.deleteById(qnaId);
    }
}
