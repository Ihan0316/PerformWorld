package com.performworld.service.board;

import com.performworld.dto.board.ReviewDTO;
import com.performworld.dto.ticket.BookingDTO;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getRvList(ReviewDTO reviewDTO);

    List<BookingDTO> getSeenEvent(String userId);

    void registReview(ReviewDTO reviewDTO);

    void updateReview(ReviewDTO reviewDTO);

    void deleteReview(Long reviewId);
}
