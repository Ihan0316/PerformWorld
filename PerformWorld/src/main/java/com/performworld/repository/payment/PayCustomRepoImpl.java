package com.performworld.repository.payment;

import com.performworld.domain.Payment;
import com.performworld.domain.QBooking;
import com.performworld.domain.QPayment;
import com.performworld.dto.ticket.BookingDTO;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class PayCustomRepoImpl extends QuerydslRepositorySupport implements PayCustomRepo {
    public PayCustomRepoImpl() {
        super(Payment.class);
    }

    // 예매 공연 등록
    @Override
    public void registPayment(BookingDTO bookingDTO) {
        QBooking booking = QBooking.booking;
        QPayment payment = QPayment.payment;


    }
}
