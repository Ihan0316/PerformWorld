package com.performworld.service.board;

import com.performworld.domain.Notice;
import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.NoticeRequestDTO;
import com.performworld.repository.board.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    //목록 조회(페이징 처리)
    @Override
    public List<NoticeDTO> getList(NoticeDTO noticeDTO) {
        Pageable pageable =Pageable.unpaged();
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        return noticePage.stream()
                .map(entity -> modelMapper.map(entity, NoticeDTO.class))
                .collect(Collectors.toList());
    }

    // 등록
    @Override
    public Long insert(NoticeRequestDTO noticeRequestDTO) {
        Notice notice = modelMapper.map(noticeRequestDTO, Notice.class);
        return noticeRepository.save(notice).getNoticeId();
    }

    // 수정
    @Override
    public Long update(NoticeRequestDTO noticeRequestDTO) {
        // 먼저 기존 데이터를 조회한 후 수정
        Notice existingNotice = noticeRepository.findById(noticeRequestDTO.getNoticeId())
                .orElseThrow(() -> new RuntimeException("Notice not found with ID: " + noticeRequestDTO.getNoticeId()));

        modelMapper.map(noticeRequestDTO, existingNotice);  // DTO를 기존 객체에 맵핑
        return noticeRepository.save(existingNotice).getNoticeId();
    }

    // 삭제
    @Override
    public void delete(Long id) {
        noticeRepository.deleteById(id);
    }

    // 단일 공지사항 조회
    @Override
    public NoticeDTO getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with ID: " + id));
        return modelMapper.map(notice, NoticeDTO.class);
    }
}
