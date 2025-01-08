package com.performworld.service.board;

import com.performworld.dto.board.*;

import java.util.List;

public interface NoticeService {

    List<NoticeDTO> getNotices(NoticeDTO noticeDTO);

    void saveNotice(NoticeDTO noticeDTO);

    void deleteNotice(Long noticeId);
}


