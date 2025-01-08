package com.performworld.service.board;

import com.performworld.dto.board.QnADTO;

import java.util.List;

public interface QnAService {

    List<QnADTO> getQnas(QnADTO qnADTO);

    void saveQnA(QnADTO qnADTO);

    void deleteQnA(Long qnaId);
}
