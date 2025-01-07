package com.performworld.service.board;

import com.performworld.dto.board.FAQDTO;
import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.QnADTO;

import java.util.List;

public interface SCService {

    List<NoticeDTO> getNotice(NoticeDTO noticeDTO); // 공지사항 전체 목록

    List<FAQDTO> getFaq(FAQDTO faqDTO);

    List<QnADTO> getQna(QnADTO qnaDTO);
}
