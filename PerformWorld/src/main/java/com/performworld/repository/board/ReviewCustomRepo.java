package com.performworld.repository.board;

import com.performworld.dto.board.ReviewDTO;
import com.performworld.dto.ticket.BookingDTO;

import java.util.List;

public interface ReviewCustomRepo {

    List<ReviewDTO> getRvList(ReviewDTO reviewDTO);

    List<BookingDTO> getSeenEvent(String userId);
}
