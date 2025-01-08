package com.performworld.repository.board;

import com.performworld.dto.board.QnADTO;

import java.util.List;

public interface BoardCustomRepo {

    List<QnADTO> getQnaList(QnADTO qnadto);
}
