package com.performworld.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "persistent_logins")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class PersistentLogin {

    @Column(nullable = false, length = 64)
    private String username;

    @Id
    @Column(nullable = false, length = 64)
    private String series;

    @Column(nullable = false, length = 64)
    private String token;

    @Column(name = "last_used", nullable = false)
    private LocalDateTime lastUsed;
}