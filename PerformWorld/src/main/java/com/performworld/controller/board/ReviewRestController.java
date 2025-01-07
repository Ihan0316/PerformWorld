package com.performworld.controller.board;

import com.performworld.dto.board.ReviewDTO;
import com.performworld.dto.ticket.BookingDTO;
import com.performworld.service.board.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewRestController {

    private final ReviewService reviewService;

    // 목록 조회
    @PostMapping("/getRvList")
    public List<ReviewDTO> getRvList(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.getRvList(reviewDTO);
    }

    // 후기 작성 가능한 예매정보 조회
    @PostMapping("/getSeenEvent")
    public List<BookingDTO> getSeenEvent(@RequestBody BookingDTO bookingDTO) {
        return reviewService.getSeenEvent(bookingDTO.getUserId());
    }

    // 후기 등록
    @PostMapping()
    public ResponseEntity<Void> registReview(@RequestBody ReviewDTO reviewDTO) {
        reviewService.registReview(reviewDTO);
        return ResponseEntity.ok().build();
    }

    // 후기 수정
    @PutMapping()
    public ResponseEntity<Void> updateReview(@RequestBody ReviewDTO reviewDTO) {
        reviewService.updateReview(reviewDTO);
        return ResponseEntity.ok().build();
    }

    // 후기 삭제
    @DeleteMapping()
    public ResponseEntity<Void> deleteReview(@RequestBody ReviewDTO reviewDTO) {
        reviewService.deleteReview(reviewDTO.getReviewId());
        return ResponseEntity.ok().build();
    }
}
