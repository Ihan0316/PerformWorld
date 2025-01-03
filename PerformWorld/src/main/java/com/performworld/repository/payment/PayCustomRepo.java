package com.performworld.repository.payment;

import com.performworld.dto.ticket.BookingDTO;

public interface PayCustomRepo {

    void registPayment(BookingDTO bookingDTO);
}
