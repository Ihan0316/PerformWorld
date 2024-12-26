package com.performworld.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;  // Users 테이블과의 관계

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    private Booking booking;  // Booking 테이블과의 관계

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;  // 후기 내용
}
