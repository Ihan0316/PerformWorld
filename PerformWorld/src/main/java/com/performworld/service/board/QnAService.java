package com.performworld.service.board;

import com.performworld.dto.board.QnADTO;
import com.performworld.dto.board.QnARequestDTO;

import java.util.List;

public interface QnAService {
    // 목록 조회
    List<QnADTO> getList(QnARequestDTO qnaRequestDTO);

    // 등록
    Long createQnA(QnARequestDTO qnaRequestDTO);

    // 수정
    Long updateQnA(Long qnaId, QnARequestDTO qnaRequestDTO);

    // 삭제
    void deleteQnA(Long id);

    // 답변 수정
    void updateResponse(Long qnaId, String response);

    // 답변 추가 (응답이 없는 QnA에 답변 추가)
    void respondToQnA(Long qnaId, String response);
}



