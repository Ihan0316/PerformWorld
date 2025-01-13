package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;  // Users 테이블과의 관계

    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "schedule_id")
    private EventSchedule eventSchedule;  // EventSchedules 테이블과의 관계

    @ManyToMany
    @JoinTable(
            name = "booking_seat",  // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private List<Seat> seats;  // Seats 테이블과의 관계

    @Column(name = "is_delivery", nullable = false)
    private boolean isDelivery;  // 배송 여부

    @Column(name = "address", length = 100)
    private String address;  // 배송지

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;  // 총 예매 금액

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "code")
    private SystemCode status;  // 예매 상태

    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Review reviews;

    // 취소 상태로 변경
    public void chnStatus(SystemCode chnStatus) {
        this.status = chnStatus;
    }
}
