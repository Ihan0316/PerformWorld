package com.performworld.service;

import com.performworld.dto.QnADTO;
import com.performworld.dto.QnARequestDTO;

import java.util.List;

public interface QnAService {

    // 페이지 처리된 QnA 조회
    List<QnADTO> getAllQnAs(QnARequestDTO pageRequestDTO);

    // 특정 QnA 조회
    QnADTO getQnAById(Long id);

    // QnA 생성
    QnADTO createQnA(QnARequestDTO requestDTO);

    // QnA 수정 (id와 DTO를 받아서 수정)
    QnADTO updateQnA(Long id, QnARequestDTO requestDTO);

    // QnA 답변 추가 (관리자용)
    QnADTO respondToQnA(Long id, String response);

    // QnA 삭제
    void deleteQnA(Long id);
}

