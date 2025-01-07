package com.performworld.service.board;

import com.performworld.domain.Booking;
import com.performworld.domain.Review;
import com.performworld.domain.User;
import com.performworld.dto.board.ReviewDTO;
import com.performworld.dto.ticket.BookingDTO;
import com.performworld.repository.board.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    // 후기 목록 조회
    @Override
    public List<ReviewDTO> getRvList(ReviewDTO reviewDTO) {
        return reviewRepository.getRvList(reviewDTO);
    }

    // 후기 작성 가능한 예매정보 조회
    @Override
    public List<BookingDTO> getSeenEvent(String userId) {
        return reviewRepository.getSeenEvent(userId);
    }

    // 후기 등록
    @Override
    public void registReview(ReviewDTO reviewDTO) {
        reviewRepository.save(Review.builder()
                        .user(User.builder().userId(reviewDTO.getUserId()).build())
                        .booking(Booking.builder().bookingId(reviewDTO.getBookingId()).build())
                        .content(reviewDTO.getContent())
                .build());
    }

    // 후기 수정
    @Override
    public void updateReview(ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewDTO.getReviewId()).orElseThrow();
        review.chnContent(reviewDTO.getContent());
        reviewRepository.save(review);
    }

    // 후기 삭제
    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
