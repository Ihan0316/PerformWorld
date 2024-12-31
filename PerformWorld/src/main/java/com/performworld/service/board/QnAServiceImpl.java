package com.performworld.service.board;

import com.performworld.domain.QnA;
import com.performworld.dto.board.QnADTO;
import com.performworld.dto.board.QnARequestDTO;
import com.performworld.repository.board.QnARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final QnARepository qnaRepository;
    private final ModelMapper modelMapper;

    // 목록 조회
    @Override
    public List<QnADTO> getList(QnARequestDTO qnaRequestDTO) {
        Pageable pageable = PageRequest.of(qnaRequestDTO.getPage(), qnaRequestDTO.getSize()); // Pageable 설정
        Page<QnA> qnaPage = qnaRepository.findAll(pageable); // 페이징된 QnA 목록 조회

        // DTO로 변환하여 반환
        return qnaPage.stream()
                .map(entity -> modelMapper.map(entity, QnADTO.class))
                .collect(Collectors.toList());
    }

    // 등록
    @Override
    public Long createQnA(QnARequestDTO qnaRequestDTO) {
        QnA qna = modelMapper.map(qnaRequestDTO, QnA.class); // DTO -> Entity 변환
        return qnaRepository.save(qna).getQnaId(); // 저장 후 ID 반환
    }

    // 수정
    @Override
    public Long updateQnA(Long qnaId, QnARequestDTO qnaRequestDTO) {
        QnA qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new RuntimeException("QnA not found with ID: " + qnaId));
        modelMapper.map(qnaRequestDTO, qna); // 기존 데이터를 DTO로 업데이트
        return qnaRepository.save(qna).getQnaId(); // 업데이트 후 ID 반환
    }

    // 삭제
    @Override
    public void deleteQnA(Long id) {
        qnaRepository.deleteById(id); // QnA 삭제
    }

    @Override
    public void updateResponse(Long qnaId, String response) {
        QnA qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new RuntimeException("QnA not found with ID: " + qnaId));

        qna.setResponse(response); // 답변 내용 및 날짜 설정
        qnaRepository.save(qna); // 저장
    }

    @Override
    public void respondToQnA(Long qnaId, String response) {
        QnA qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new RuntimeException("QnA not found with ID: " + qnaId));
        qna.setResponse(response);
        qna.setResponseDate(LocalDateTime.now());
        qnaRepository.save(qna);
    }

}





