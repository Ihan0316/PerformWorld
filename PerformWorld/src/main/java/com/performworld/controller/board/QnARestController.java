package com.performworld.controller.board;

import com.performworld.dto.board.QnADTO;
import com.performworld.dto.board.QnARequestDTO;
import com.performworld.service.board.QnAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnARestController {

    private final QnAService qnaService;

    // QnA 목록 조회 (페이지 처리)
    @PostMapping("/list")
    public List<QnADTO> getQnAList(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching QnA list for API");
        QnARequestDTO requestDTO = new QnARequestDTO();
        requestDTO.setPage(page);
        requestDTO.setSize(size);
        return qnaService.getList(requestDTO);
    }

    // QnA 등록
    @PostMapping("/register")
    public Long registerQnA(@RequestBody QnARequestDTO qnaRequestDTO) {
        return qnaService.registerQnA(qnaRequestDTO);
    }

    // QnA 수정
    @PutMapping("/update/{id}")
    public Long updateQnA(@PathVariable Long id, @RequestBody QnARequestDTO qnaRequestDTO) {
        return qnaService.updateQnA(id, qnaRequestDTO);
    }

    // QnA 삭제
    @DeleteMapping("/delete/{id}")
    public void deleteQnA(@PathVariable Long id) {
        qnaService.deleteQnA(id);
    }
}


