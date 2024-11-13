package com.fatec.smart_parking.email_verificator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificatorRepository extends JpaRepository<EmailVerificator, Long> {

    Optional<EmailVerificator> findByToken(String token);

    Optional<EmailVerificator> findByEmailAndVerifiedTrue(String email);
}
