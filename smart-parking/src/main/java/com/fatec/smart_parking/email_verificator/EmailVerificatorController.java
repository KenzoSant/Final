package com.fatec.smart_parking.email_verificator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView check(@PathVariable String token) {
        boolean tokenValid = this.emailVerificatorService.checkToken(token);

        ModelAndView modelAndView = new ModelAndView();

        if (tokenValid) {
            modelAndView.setViewName("emailVerificado");
            modelAndView.addObject("message", "Seu email foi verificado com sucesso!");
        } else {
            modelAndView.setViewName("erroVerificacao");
            modelAndView.addObject("message", "O token de verificação é inválido ou expirou.");
        }

        return modelAndView;
    }
}
