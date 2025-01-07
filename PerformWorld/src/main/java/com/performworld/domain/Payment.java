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
    @Column(name = "payment_id")
    private String paymentId;

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    private Booking booking;  // Bookings 테이블과의 관계

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;  // 결제 방법

    @Column(name = "payment_amount", nullable = false)
    private Integer paymentAmount;  // 결제 금액

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;  // 결제 일시

    @Column(name = "email", nullable = false)
    private String email;  // 결과 전송 이메일

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "code")
    private SystemCode status;  // 결제 상태

    // 취소 상태로 변경
    public void chnStatus(SystemCode chnStatus) {
        this.status = chnStatus;
    }
}
