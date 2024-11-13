package com.fatec.smart_parking.email_verificator;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verificator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class EmailVerificator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expiration_date;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    private Boolean verified = false;

    public EmailVerificator(String email, String token, LocalDateTime expiration_date, LocalDateTime created_at) {
        this.email = email;
        this.token = token;
        this.expiration_date = expiration_date;
        this.created_at = created_at;
    }
}
