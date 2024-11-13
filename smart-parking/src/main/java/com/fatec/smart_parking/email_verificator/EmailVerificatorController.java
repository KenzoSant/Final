package com.fatec.smart_parking.email_verificator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/email-checker")
public class EmailVerificatorController {

    @Autowired
    private EmailVerificatorService emailVerificatorService;;

    @PostMapping
    public ResponseEntity create(@RequestBody EmailVerificatorDTO dto){
        this.emailVerificatorService.create(dto.email());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{token}")
    public ResponseEntity check(@PathVariable String token){
        this.emailVerificatorService.checkToken(token);
        return ResponseEntity.ok().build();
    }
}
