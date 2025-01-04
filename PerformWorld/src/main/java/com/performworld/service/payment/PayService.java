package com.performworld.service.payment;

import com.performworld.dto.ticket.BookingDTO;

public interface PayService {

    BookingDTO registPayment(BookingDTO bookingDTO);
}
