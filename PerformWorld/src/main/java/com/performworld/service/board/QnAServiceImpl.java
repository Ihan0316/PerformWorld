package com.performworld.service.board;

import com.performworld.domain.QnA;
import com.performworld.dto.board.QnADTO;
import com.performworld.dto.board.QnARequestDTO;

import com.performworld.repository.board.QnARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final QnARepository qnaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<QnADTO> getList(QnADTO qnADTO) {
        return qnaRepository.getQnaList(qnADTO);
    }

    //등록
    @Override
    @Transactional
    public Long registerQnA(QnARequestDTO qnaRequestDTO) {
        log.info("Registering new QnA : {}", qnaRequestDTO);
        QnA qna = modelMapper.map(qnaRequestDTO, QnA.class);
        QnA savedQnA = qnaRepository.save(qna);
        return savedQnA.getQnaId();
    }

    //수정
    @Override
    @Transactional
    public Long updateQnA(Long qnaId, QnARequestDTO qnaRequestDTO) {
        log.info("Updating QnA with ID : {}", qnaId);

        // findById와 orElseThrow를 분리하여 가독성 향상
        QnA existingQnA = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("QnA with ID " + qnaId + " not found"));

        // updateQnA 메서드를 호출하여 제목과 내용을 업데이트
        existingQnA.updateQnA(qnaRequestDTO.getTitle(), qnaRequestDTO.getContent());

        // 업데이트된 엔티티 저장
        QnA updatedQnA = qnaRepository.save(existingQnA);

        return updatedQnA.getQnaId();
    }

    @Override
    @Transactional
    public void deleteQnA(Long qnaId) {
        log.info("Deleting QnA with ID : {}", qnaId);
        if (!qnaRepository.existsById(qnaId)) {
            throw new IllegalArgumentException("QnA with ID " + qnaId + " not found");
        }
        qnaRepository.deleteById(qnaId);
    }

    @Override
    public QnADTO getQnAById(Long id) {
        return null;
    }
}
