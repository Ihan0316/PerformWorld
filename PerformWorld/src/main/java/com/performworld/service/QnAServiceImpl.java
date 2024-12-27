package com.performworld.service;

import com.performworld.domain.QnA;
import com.performworld.domain.User;
import com.performworld.dto.QnADTO;
import com.performworld.dto.QnARequestDTO;
import com.performworld.repository.QnARepository;
import com.performworld.repository.UserRepository;
import com.performworld.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final QnARepository qnaRepository;
    private final UserRepository userRepository;

    // 페이지 처리된 QnA 조회
    @Override
    public List<QnADTO> getAllQnAs(QnARequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());
        Page<QnA> qnaPage = qnaRepository.findAll(pageable);
        return qnaPage.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 QnA 조회
    @Override
    public QnADTO getQnAById(Long id) {
        return qnaRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("QnA not found with id: " + id));
    }

    // QnA 생성
    @Override
    public QnADTO createQnA(QnARequestDTO requestDTO) {
        Optional<User> userOptional = userRepository.findById(requestDTO.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + requestDTO.getUserId()); // 예외 처리
        }
        QnA qna = QnA.builder()
                .user(userOptional.get())
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .build();
        QnA savedQnA = qnaRepository.save(qna);
        return convertToDTO(savedQnA);
    }

    @Override
    public QnADTO updateQnA(Long id, QnARequestDTO requestDTO) {
        // QnA가 존재하는지 확인
        Optional<QnA> qnaOptional = qnaRepository.findById(id);
        if (!qnaOptional.isPresent()) {
            throw new RuntimeException("QnA not found with id: " + id); // QnA가 존재하지 않으면 예외 처리
        }

        // QnA 객체 가져오기
        QnA qna = qnaOptional.get();

        // 제목과 내용만 수정하려는 것이 아니라,
        // 필요하다면 다른 수정 가능한 항목도 처리할 수 있도록 확장 가능
        boolean isUpdated = false;

        // 제목 변경이 있다면
        if (!requestDTO.getTitle().equals(qna.getTitle())) {
            qna.setTitle(requestDTO.getTitle());
            isUpdated = true;
        }

        // 내용 변경이 있다면
        if (!requestDTO.getContent().equals(qna.getContent())) {
            qna.setContent(requestDTO.getContent());
            isUpdated = true;
        }

        // 업데이트가 발생했다면 저장
        if (isUpdated) {
            qna.setUpdatedAt(java.time.LocalDateTime.now()); // 수정일 갱신
            QnA updatedQnA = qnaRepository.save(qna);
            return convertToDTO(updatedQnA);
        }

        // 변경된 내용이 없으면 그대로 반환
        return convertToDTO(qna); // 기존의 QnA 객체를 DTO로 변환하여 반환
    }

    // QnA 답변 추가 (관리자용)
    @Override
    public QnADTO respondToQnA(Long id, String response) {
        // 관리자 확인 로직
        if (!isAdmin()) {
            throw new RuntimeException("Only administrators can respond to QnA");
        }

        Optional<QnA> qnaOptional = qnaRepository.findById(id);
        if (qnaOptional.isPresent()) {
            QnA qna = qnaOptional.get();
            qna.setResponse(response);
            qna.setResponseDatetime(LocalDateTime.now());
            QnA updatedQnA = qnaRepository.save(qna);
            return convertToDTO(updatedQnA);
        }
        return null;
    }

    // QnA 삭제
    @Override
    public void deleteQnA(Long id) {
        Optional<QnA> qnaOptional = qnaRepository.findById(id);
        if (qnaOptional.isPresent()) {
            qnaRepository.delete(qnaOptional.get());
        } else {
            // 예외 처리: 삭제할 QnA가 존재하지 않는 경우
        }
    }

    // QnA 객체를 DTO로 변환
    private QnADTO convertToDTO(QnA qna) {
        return QnADTO.builder()
                .qnaId(qna.getQnaId())
                .userId(qna.getUser().getUserId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .response(qna.getResponse())
                .responseDatetime(qna.getResponseDatetime())
                .createdAt(qna.getCreatedAt())
                .updatedAt(qna.getUpdatedAt())
                .build();
    }

    // 관리자 확인 로직
    private boolean isAdmin() {
        // 실제 관리자인지 확인하는 로직 (예: Spring Security 활용)
        return true; // 임시 true
    }
}

