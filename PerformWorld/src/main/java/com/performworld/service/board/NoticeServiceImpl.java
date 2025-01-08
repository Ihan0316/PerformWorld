package com.performworld.service.board;

import com.performworld.domain.Notice;
import com.performworld.dto.board.*;
import com.performworld.repository.board.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    // 공지사항 목록 조회
    @Override
    public List<NoticeDTO> getNotices(NoticeDTO noticeDTO) {
        return noticeRepository.getNoticeList(noticeDTO);
    }

    // 공지사항 등록
    @Override
    public void saveNotice(NoticeDTO noticeDTO) {
        Notice notice = Notice.builder()
                .title(noticeDTO.getTitle())
                .content(noticeDTO.getContent())
                .build();
        noticeRepository.save(notice);
    }

    // 공지사항 삭제
    @Override
    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }
}
