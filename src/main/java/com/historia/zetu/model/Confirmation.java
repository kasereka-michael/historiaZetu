package com.historia.zetu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@Entity
@Table(name = "Confirmations")
public class Confirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otp;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(nullable = false, name = "user_id")
    private Users users;

    // Constructor to generate OTP and set other values
    public Confirmation(Users users) {
        Random random = new Random();
        this.users = users;
        this.createdDate = LocalDateTime.now();
        this.otp = String.format("%06d", random.nextInt(1_000_000)); ;
    }

    public Confirmation() {

    }
}