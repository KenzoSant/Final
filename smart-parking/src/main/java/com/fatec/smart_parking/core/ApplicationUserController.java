package com.fatec.smart_parking.core;

import com.fatec.smart_parking.core.authentication.EmailResponseDTO;
import com.fatec.smart_parking.core.authentication.LoginDTO;
import com.fatec.smart_parking.core.authentication.LoginResponseDTO;
import com.fatec.smart_parking.core.authentication.RegisterDTO;
import com.fatec.smart_parking.core.config.TokenService;
import com.fatec.smart_parking.core.email.EmailService;
import com.fatec.smart_parking.core.listener.EmailSentEventDTO;
import com.fatec.smart_parking.email_verificator.EmailVerificatorService;
import com.fatec.smart_parking.user.User;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.fatec.smart_parking.core.config.RabbitMqConfig.EMAIL_SENT_QUEUE;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class ApplicationUserController{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private EmailVerificatorService emailVerificatorService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private final RabbitTemplate rabbitTemplate;



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO loginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(),loginDTO.password());
        this.emailVerificatorService.isVerified(loginDTO.email());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO){
        this.applicationUserService.create(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestBody @Valid EmailSentEventDTO email){
        rabbitTemplate.convertAndSend(EMAIL_SENT_QUEUE, email);
        EmailResponseDTO response = new EmailResponseDTO(
                "Password reset email sent.",
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                LocalDateTime.now()
        );
        return  ResponseEntity.ok(response);
    }
}