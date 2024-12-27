package com.performworld.service;

import com.performworld.dto.NoticeDTO;
import com.performworld.dto.NoticeRequestDTO;
import com.performworld.dto.NoticeResponseDTO;

import java.util.List;

public interface NoticeService {
    List<NoticeDTO> getAllNotices();  // 전체 공지사항 조회
    NoticeDTO getNoticeById(Long id);  // 공지사항 단건 조회
    NoticeResponseDTO createNotice(NoticeRequestDTO noticeRequestDTO);  // 공지사항 등록 (RequestDTO로 데이터 받음, ResponseDTO로 응답)
    NoticeDTO updateNotice(Long id, NoticeDTO noticeDTO);  // 공지사항 수정
    boolean deleteNotice(Long id);  // 공지사항 삭제 (예외를 통해 처리)
}


