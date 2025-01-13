package com.performworld.repository.board;

import com.performworld.domain.*;
import com.performworld.dto.board.ReviewDTO;
import com.performworld.dto.ticket.BookingDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewCustomRepoImpl extends QuerydslRepositorySupport implements ReviewCustomRepo {
    public ReviewCustomRepoImpl() {
        super(Review.class);
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");

    // 후기 목록 조회
    @Override
    public List<ReviewDTO> getRvList(ReviewDTO reviewDTO) {
        QReview review = QReview.review;
        QBooking booking = QBooking.booking;
        QEventSchedule eventSchedule = QEventSchedule.eventSchedule;
        QEvent event = QEvent.event;

        BooleanBuilder builder = new BooleanBuilder();
        // 검색조건
        if(reviewDTO.getSrhEvent() != null && !reviewDTO.getSrhEvent().isEmpty()) {
            builder.and(event.title.contains(reviewDTO.getSrhEvent()));
        }
        if(reviewDTO.getSrhUserId() != null && !reviewDTO.getSrhUserId().isEmpty()) {
            builder.and(review.user.userId.eq(reviewDTO.getSrhUserId()));
        }

        List<Tuple> result = from(review)
                .join(review.booking, booking)
                .join(booking.eventSchedule, eventSchedule)
                .join(eventSchedule.event, event)
                .where(builder)
                .orderBy(review.reviewId.desc())

                .select(
                        review.reviewId
                        , review.user.userId
                        , booking.bookingId
                        , event.title
                        , eventSchedule.eventDate
                        , review.content
                        , review.regDate
                ).fetch();

        return result.stream().map(tuple -> ReviewDTO.builder()
                        .reviewId(tuple.get(review.reviewId))
                        .userId(tuple.get(review.user.userId))
                        .bookingId(tuple.get(booking.bookingId))
                        .eventInfo(tuple.get(event.title) + " : " + tuple.get(eventSchedule.eventDate).format(formatter))
                        .content(tuple.get(review.content))
                        .regDate(tuple.get(review.regDate))
                        .build())
                .collect(Collectors.toList());
    }

    // 후기 작성 가능한 예매정보 조회
    @Override
    public List<BookingDTO> getSeenEvent(String userId) {
        QReview review = QReview.review;
        QBooking booking = QBooking.booking;
        QEventSchedule eventSchedule = QEventSchedule.eventSchedule;
        QEvent event = QEvent.event;

        List<Tuple> result = from(review)
                .rightJoin(review.booking, booking)
                .join(booking.eventSchedule, eventSchedule)
                .join(eventSchedule.event, event)
                .where(booking.user.userId.eq(userId))
                .where(booking.status.code.eq("Y"))
                .where(eventSchedule.eventDate.before(LocalDateTime.now()))
                .where(review.reviewId.isNull())
                .where()

                .select(
                        booking.bookingId
                        , booking.user.userId
                        , eventSchedule.scheduleId
                        , event.title
                        , eventSchedule.eventDate
                ).fetch();

        return result.stream().map(tuple -> BookingDTO.builder()
                        .bookingId(tuple.get(booking.bookingId))
                        .userId(tuple.get(booking.user.userId))
                        .scheduleId(tuple.get(eventSchedule.scheduleId))
                        .eventInfo(tuple.get(event.title) + " : " + tuple.get(eventSchedule.eventDate).format(formatter))
                        .build())
                .collect(Collectors.toList());
    }
}
