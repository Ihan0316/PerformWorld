package com.performworld.repository.payment;

import com.performworld.domain.Payment;
import com.performworld.domain.QBooking;
import com.performworld.domain.QSeat;
import com.performworld.dto.ticket.BookingDTO;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class PayCustomRepoImpl extends QuerydslRepositorySupport implements PayCustomRepo {
    public PayCustomRepoImpl() {
        super(Payment.class);
    }

    // 예매된 좌석인지 체크
    @Override
    public List<String> checkBooking(BookingDTO bookingDTO) {
        QBooking booking = QBooking.booking;
        QSeat seat = QSeat.seat;

        return from(booking)
                .join(booking.seats, seat)
                .on(seat.seatId.in(bookingDTO.getSeatIds()))
                .where(booking.eventSchedule.scheduleId.eq(bookingDTO.getScheduleId()))
                .where(booking.status.code.in("Y", "P"))

                .select(seat.seatId)
                .fetch();
    }
}
