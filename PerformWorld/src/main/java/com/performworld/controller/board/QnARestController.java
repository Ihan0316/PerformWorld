package com.performworld.controller.board;

import com.performworld.dto.board.QnADTO;
import com.performworld.dto.board.QnARequestDTO;
import com.performworld.service.board.QnAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnARestController {

    private final QnAService qnaService;

    // QnA 목록 조회
    @PostMapping("/getQnAList")
    public ResponseEntity<List<QnADTO>> getList(@RequestBody QnARequestDTO qnaRequestDTO) {
        log.info(qnaRequestDTO);
        List<QnADTO> qnaList = qnaService.getList(qnaRequestDTO);
        return ResponseEntity.ok(qnaList);
    }

    // QnA 등록
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody QnARequestDTO qnaRequestDTO) {
        log.info(qnaRequestDTO);

        Long qnaId = qnaService.createQnA(qnaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(qnaId);
    }

    // QnA 수정
    @PutMapping("/update")
    public ResponseEntity<Long> update(@RequestBody QnARequestDTO qnaRequestDTO) {
        log.info(qnaRequestDTO);
        Long qnaId = qnaService.updateQnA(qnaRequestDTO.getQnaId(), qnaRequestDTO);
        return ResponseEntity.ok(qnaId);
    }

    // QnA 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info(id);
        qnaService.deleteQnA(id);
        return ResponseEntity.noContent().build();
    }

    // QnA 답변 등록
    @PostMapping("/respond")
    public ResponseEntity<Void> respond(@RequestParam Long qnaId, @RequestParam String response) {
        qnaService.respondToQnA(qnaId, response);
        return ResponseEntity.ok().build();
    }
}




