package com.performworld.service.board;

import com.performworld.domain.Notice;
import com.performworld.dto.board.*;
import com.performworld.repository.board.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<NoticeDTO> getAllNotices(NoticeDTO noticeDTO) {
        // 전체 공지사항 조회
        return noticeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeDTO> getNotices() {
        return noticeRepository.findAll()
                .stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public NoticeDTO getNoticeById(Long id) {
        // 특정 ID로 공지사항 조회
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id)); // 예외 처리
        return convertToDTO(notice);
    }

    @Override
    public NoticeDTO updateNotice(Long id, NoticeRequestDTO noticeDTO) {
        // 기존 공지사항 수정
        Notice existingNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id)); // 예외 처리

        existingNotice.updateNotice(noticeDTO.getTitle(), noticeDTO.getContent());

        Notice updatedNotice = noticeRepository.save(existingNotice);
        return convertToDTO(updatedNotice);
    }

    @Override
    public void deleteNotice(Long id) {
        // 공지사항 삭제 (예외를 통해 처리)
        if (!isAdmin()) {
            throw new RuntimeException("Only administrators can delete notices.");
        }

        // 공지사항이 존재하는지 확인 후 삭제
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id)); // 예외 처리
        noticeRepository.delete(notice);
    }

    @Override
    public void saveNotice(NoticeSaveDTO noticeSaveDTO) {
        Notice notice = Notice.builder()
                .title(noticeSaveDTO.getTitle())
                .content(noticeSaveDTO.getContent())
                .build();
        noticeRepository.save(notice);  // DB에 저장
    }


    @Override
    public NoticeResponseDTO registerNotice(NoticeRequestDTO noticeRequestDTO) {
        // DTO에서 Entity로 변환 후 저장
        Notice notice = Notice.builder()
                .title(noticeRequestDTO.getTitle())
                .content(noticeRequestDTO.getContent())
                .build();

        Notice savedNotice = noticeRepository.save(notice);

        // 저장된 Notice를 ResponseDTO로 변환하여 반환
        return convertToResponseDTO(savedNotice);
    }

    private NoticeResponseDTO convertToResponseDTO(Notice notice) {
        return NoticeResponseDTO.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .build();
    }

    // DTO -> Entity 변환
    private NoticeDTO convertToDTO(Notice notice) {
        return modelMapper.map(notice, NoticeDTO.class);
    }

    // Entity -> DTO 변환
    private Notice convertToEntity(NoticeDTO noticeDTO) {
        return modelMapper.map(noticeDTO, Notice.class);
    }

    // 관리자 확인 로직 (Spring Security 사용 권장)
    private boolean isAdmin() {
        // 관리자 확인 로직 (예: Spring Security 활용)
        return true; // 임시 true
    }
}


