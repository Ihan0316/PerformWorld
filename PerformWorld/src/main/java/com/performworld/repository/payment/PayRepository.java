package com.performworld.repository.payment;

import com.performworld.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Payment, Long>, PayCustomRepo {
}
