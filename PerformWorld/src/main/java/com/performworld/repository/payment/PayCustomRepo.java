package com.performworld.repository.payment;

import com.performworld.dto.ticket.BookingDTO;

import java.util.List;

public interface PayCustomRepo {

    List<String> checkBooking(BookingDTO bookingDTO);

    List<BookingDTO> getBknList(BookingDTO bookingDTO);

    BookingDTO getBknInfo(Long bookingId);
}
