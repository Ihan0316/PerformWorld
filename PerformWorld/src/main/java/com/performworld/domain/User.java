package com.performworld.domain;

import com.performworld.dto.user.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
//@ToString(exclude = "roleSet")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id", length = 20)
    private String userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "total_spent", precision = 10, scale = 2)
    private Long totalSpent;

    @ManyToOne
    @JoinColumn(name = "tier_id", referencedColumnName = "tier_id")
    private Tier tier;

    @Column(name = "address1", length = 255)
    private String address1;

    @Column(name = "address2", length = 255)
    private String address2;

    @Column(name = "postcode", length = 20)
    private String postcode;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QnA> qnas;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<>();

    private boolean del;
    private boolean social;



    // 비밀번호 변경
    public void chnUserInfo(String password) {
        this.password = password;
    }

    //권한 추가
    public void addRole(UserRole userRole) {
        this.roleSet.add(userRole);
    }

    // 정보 수정
    public void chnUserInfo(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.email = userDTO.getEmail();
        this.phoneNumber = userDTO.getPhoneNumber();
        this.address1 = userDTO.getAddress1();
        this.address2 = userDTO.getAddress2();
        this.postcode = userDTO.getPostcode();
    }

    // 소비금액 및 등급 변경
    public void chnTotalSpent(Long spent, List<Tier> tiers) {
        this.totalSpent = spent;

        tiers.stream()
                .filter(tier -> (tier.getMinSpent() <= totalSpent) && (totalSpent <= tier.getMaxSpent()))
                .findFirst()
                .ifPresent(tier -> this.tier = tier);
    }
}
