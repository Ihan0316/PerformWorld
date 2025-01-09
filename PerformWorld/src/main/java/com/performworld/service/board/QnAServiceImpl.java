package com.performworld.service.board;

import com.performworld.domain.Notice;
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

    @Override
    public void updateQna(QnADTO qnADTO) {

        QnA qnA = qnaRepository.findById(qnADTO.getQnaId())
                .orElseThrow(() -> new RuntimeException("해당 Q&A를 찾을 수 없습니다."));

        qnA.updateQnA(qnADTO.getTitle(), qnADTO.getContent(), qnA.getResponse());

        qnaRepository.save(qnA);
    }

    @Override
    public void updateResponseQna(QnADTO qnADTO) {
        QnA qnA = qnaRepository.findById(qnADTO.getQnaId())
                .orElseThrow(() -> new RuntimeException("해당 Q&A를 찾을 수 없습니다."));
        qnA.registRes(qnADTO.getResponse());
        qnaRepository.save(qnA);
    }
}
