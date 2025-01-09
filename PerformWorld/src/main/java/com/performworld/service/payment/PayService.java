package com.performworld.service.payment;

import com.performworld.dto.ticket.BookingDTO;

import java.util.List;

public interface PayService {

    BookingDTO registPayment(BookingDTO bookingDTO);

    List<BookingDTO> getBknList(BookingDTO bookingDTO);

    BookingDTO getBknInfo(Long bookingId);

    BookingDTO cancelPayment(BookingDTO bookingDTO);
}
