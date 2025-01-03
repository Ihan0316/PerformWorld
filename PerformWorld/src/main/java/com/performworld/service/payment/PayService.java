package com.performworld.service.payment;

import com.performworld.dto.ticket.BookingDTO;

public interface PayService {

    void registPayment(BookingDTO bookingDTO);
}
