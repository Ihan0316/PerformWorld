package com.performworld.service.board;

import com.performworld.dto.board.*;

import java.util.List;

public interface NoticeService {
    // 전체 공지사항 조회
    List<NoticeDTO> getAllNotices(NoticeDTO noticeDTO);

    List<NoticeDTO> getNotices();

    // 공지사항 단건 조회
    NoticeDTO getNoticeById(Long id);

    // 공지사항 등록
    NoticeResponseDTO registerNotice(NoticeRequestDTO noticeRequestDTO);

    // 공지사항 수정
    NoticeDTO updateNotice(Long id, NoticeRequestDTO noticeDTO);

    // 공지사항 삭제
    void deleteNotice(Long id);

    void saveNotice(NoticeSaveDTO noticeSaveDTO);
}


