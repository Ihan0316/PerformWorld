package com.performworld.repository.payment;

import com.performworld.domain.*;
import com.performworld.dto.ticket.BookingDTO;
import com.performworld.dto.ticket.PaymentDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    // 예매 내역 조회
    @Override
    public List<BookingDTO> getBknList(BookingDTO bookingDTO) {
        QBooking booking = QBooking.booking;
        QEventSchedule eventSchedule = QEventSchedule.eventSchedule;
        QEvent event = QEvent.event;

        // 검색조건
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(booking.user.userId.eq(bookingDTO.getUserId()));
        if(bookingDTO.getStatus() != null) {
            if("1".equals(bookingDTO.getStatus())) {
                builder.and(booking.status.code.in("Y", "P"));
            }
            if("0".equals(bookingDTO.getStatus())) {
                builder.and(booking.status.code.eq("N"));
            }
        }

        List<Tuple> result = from(booking)
                .leftJoin(booking.eventSchedule, eventSchedule)
                .leftJoin(eventSchedule.event, event)
                .where(builder)
                .orderBy(booking.bookingId.desc())

                .select(
                        booking.bookingId
                        , booking.user.userId
                        , booking.status.codeName
                        , booking.regDate
                        , event.title
                        , event.location
                        , eventSchedule.eventDate
                )
                .fetch();

        return result.stream().map(tuple -> BookingDTO.builder()
                        .bookingId(tuple.get(booking.bookingId))
                        .userId(tuple.get(booking.user.userId))
                        .status(tuple.get(booking.status.codeName))
                        .regDate(tuple.get(booking.regDate))
                        .eventInfo(tuple.get(event.title))
                        .eventLocation(tuple.get(event.location))
                        .eventDate(tuple.get(eventSchedule.eventDate).toString())
                        .build())
                .collect(Collectors.toList());
    }

    // 예매 상세 조회
    @Override
    public BookingDTO getBknInfo(Long bookingId) {
        QPayment payment = QPayment.payment;
        QBooking booking = QBooking.booking;
        QSeat seat = QSeat.seat;
        QEvent event = QEvent.event;
        QEventSchedule eventSchedule = QEventSchedule.eventSchedule;

        // 조회
        List<Tuple> result = from(payment)
                .leftJoin(payment.booking, booking)
                .leftJoin(booking.seats, seat)
                .leftJoin(booking.eventSchedule, eventSchedule)
                .leftJoin(eventSchedule.event, event)
                .where(booking.bookingId.eq(bookingId))

                .select(
                        booking.bookingId
                        , booking.user.userId
                        , booking.user.name
                        , seat.seatId
                        , booking.isDelivery
                        , booking.address
                        , booking.status.codeName
                        , booking.regDate
                        , event.title
                        , event.location
                        , event.casting
                        , eventSchedule.scheduleId
                        , eventSchedule.eventDate
                        , payment.paymentId
                        , payment.paymentDate
                        , payment.paymentAmount
                        , payment.paymentMethod
                )
                .fetch();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");

        // DTO 세팅
        Tuple firseTuple = result.get(0);
        BookingDTO newBookingDTO = BookingDTO.builder()
                .bookingId(firseTuple.get(booking.bookingId))
                .userId(firseTuple.get(booking.user.userId))
                .userName(firseTuple.get(booking.user.name))
                .scheduleId(firseTuple.get(eventSchedule.scheduleId))
                .isDelivery(firseTuple.get(booking.isDelivery))
                .address(firseTuple.get(booking.address))
                .status(firseTuple.get(booking.status.codeName))
                .regDate(firseTuple.get(booking.regDate))
                .eventInfo(firseTuple.get(event.title))
                .eventLocation(firseTuple.get(event.location))
                .eventDate(firseTuple.get(eventSchedule.eventDate).format(formatter))
                .eventCast(firseTuple.get(event.casting))
                .seatIds(new ArrayList<>())
                .build();

        // Payment 추가
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .paymentId(firseTuple.get(payment.paymentId))
                .bookingId(firseTuple.get(booking.bookingId))
                .paymentAmount(firseTuple.get(payment.paymentAmount))
                .paymentMethod(firseTuple.get(payment.paymentMethod))
                .paymentDate(firseTuple.get(payment.paymentDate))
                .build();
        newBookingDTO.setPayment(paymentDTO);

        // Seat 추가
        for (Tuple tuple : result) {
            String seatId = tuple.get(seat.seatId);
            if (seatId != null) {
                newBookingDTO.getSeatIds().add(seatId);
            }
        }

        return newBookingDTO;
    }
}
