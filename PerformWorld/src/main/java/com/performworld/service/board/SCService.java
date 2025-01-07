package com.performworld.service.board;

import com.performworld.domain.Notice;
import com.performworld.dto.board.NoticeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SCService {

    List<NoticeDTO> getNotice(); // 공지사항 전체 목록
}
