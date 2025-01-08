package com.performworld.service.board;

import com.performworld.dto.board.QnADTO;
import com.performworld.dto.board.QnARequestDTO;

import java.util.List;

public interface QnAService {

    // QnA 목록 조회
    List<QnADTO> getList(QnADTO qnADTO);

    // QnA 등록
    Long registerQnA(QnARequestDTO qnaRequestDTO);

    // QnA 수정
    Long updateQnA(Long qnaId, QnARequestDTO qnaRequestDTO);

    // QnA 삭제
    void deleteQnA(Long qnaId);

    QnADTO getQnAById(Long id);
}
