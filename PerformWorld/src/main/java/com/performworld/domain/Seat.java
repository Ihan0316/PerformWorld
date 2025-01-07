package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "seats")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends BaseEntity{


    @Id
    @Column(name = "seat_id", length = 10)
    private String seatId;  // 좌석 ID (예: A11, C20)

    @Column(name = "section", nullable = false, length = 100)
    private String section;  // 좌석 구역 (예: VIP, A석 등)

    @Column(name = "price", nullable = false)
    private Long price;  // 좌석 가격

    @ManyToMany(mappedBy = "seats", fetch = FetchType.LAZY)
    private List<Booking> bookings;
}
