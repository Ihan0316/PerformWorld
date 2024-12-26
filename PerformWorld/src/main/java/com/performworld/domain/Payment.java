package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    private Booking booking;  // Bookings 테이블과의 관계

    @OneToOne
    @JoinColumn(name = "payment_method", referencedColumnName = "code")
    private SystemCode paymentMethod;  // 결제 방법 (예: 카드, 현금 등)

    @Column(name = "payment_amount", nullable = false)
    private Long paymentAmount;  // 결제 금액

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;  // 결제 일시

    @OneToOne
    @JoinColumn(name = "status", referencedColumnName = "code")
    private SystemCode status;  // 결제 상태 (예: 결제 완료, 취소 등)

}
