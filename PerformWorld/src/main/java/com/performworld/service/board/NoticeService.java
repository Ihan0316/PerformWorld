package com.performworld.service.board;

import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.NoticeRequestDTO;

import java.util.List;

public interface NoticeService {
    // 목록 조회
    List<NoticeDTO> getList(NoticeDTO noticeDTO);

    // 등록
    Long insert(NoticeRequestDTO noticeDTO);

    // 수정
    Long update(NoticeRequestDTO noticeDTO);

    // 삭제
    void delete(Long id);

    // 공지사항 ID로 조회
    NoticeDTO getNoticeById(Long id); // 반환 타입을 Object에서 NoticeDTO로 변경
}


