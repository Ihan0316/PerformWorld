package com.performworld.repository.payment;

import com.performworld.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayRepository extends JpaRepository<Payment, Long>, PayCustomRepo {

    Optional<Payment> findByBooking_BookingId(Long bookingId);
}
