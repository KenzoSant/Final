package com.fatec.smart_parking.email_verificator;

import com.fatec.smart_parking.core.email.EmailService;
import com.fatec.smart_parking.core.exception.EmailNotVerifiedException;
import com.fatec.smart_parking.core.exception.TokenInvalidException;
import com.fatec.smart_parking.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificatorService {

    @Autowired
    private EmailVerificatorRepository emailVerificatorRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public EmailVerificatorDTO create(String email){
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(3);
        EmailVerificator emailVerificator = new EmailVerificator(email,token,expirationTime,now);
        this.emailVerificatorRepository.save(emailVerificator);
        return sendEmailVerificationLink(email,token);
    }

    public void checkToken(String token){
        Optional<EmailVerificator> optEmail = this.emailVerificatorRepository.findByToken(token);
        if(optEmail.isPresent()){
            optEmail.get().setVerified(Boolean.TRUE);
            this.emailVerificatorRepository.save(optEmail.get());
        }else{
            throw new TokenInvalidException();
        }
    }

    public boolean isVerified (String email){
        Optional<EmailVerificator> optEmail = this.emailVerificatorRepository.findByEmailAndVerifiedTrue(email);
        if(optEmail.isPresent()){
            return true;
        }
        throw new EmailNotVerifiedException();
    }


    public EmailVerificatorDTO sendEmailVerificationLink(String email, String token) {
            String verificationLink = "http://localhost:8080/api/v1/email-checker/" + token;

            this.emailService.sendHtmlMessage(
                    email,
                    "Verifique seu endereço de e-mail",
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"pt-BR\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                            "    <title>Verificação de E-mail</title>\n" +
                            "    <style>\n" +
                            "        body {\n" +
                            "            font-family: Arial, sans-serif;\n" +
                            "            background-color: #f4f4f4;\n" +
                            "            padding: 20px;\n" +
                            "        }\n" +
                            "        .container {\n" +
                            "            background-color: #ffffff;\n" +
                            "            padding: 20px;\n" +
                            "            border-radius: 5px;\n" +
                            "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                            "            max-width: 600px;\n" +
                            "            margin: auto;\n" +
                            "        }\n" +
                            "        h1 {\n" +
                            "            color: #333;\n" +
                            "        }\n" +
                            "        p {\n" +
                            "            font-size: 16px;\n" +
                            "            line-height: 1.5;\n" +
                            "            color: #666;\n" +
                            "        }\n" +
                            "        .button {\n" +
                            "            display: inline-block;\n" +
                            "            padding: 10px 20px;\n" +
                            "            margin-top: 20px;\n" +
                            "            background-color: #007bff;\n" +
                            "            color: #ffffff;\n" +
                            "            text-decoration: none;\n" +
                            "            border-radius: 5px;\n" +
                            "        }\n" +
                            "        .footer {\n" +
                            "            margin-top: 20px;\n" +
                            "            font-size: 12px;\n" +
                            "            color: #999;\n" +
                            "            text-align: center;\n" +
                            "        }\n" +
                            "    </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "    <div class=\"container\">\n" +
                            "        <h1>Verificação de E-mail</h1>\n" +
                            "        <p>Olá,</p>\n" +
                            "        <p>Obrigado por se registrar na nossa plataforma. Para finalizar seu cadastro e ativar sua conta, por favor, clique no link abaixo:</p>\n" +
                            "        <a href=\"" + verificationLink +"\" style=\"color: #fff; text-decoration: none;\" class=\"button\">Verificar E-mail</a>\n" +
                            "        <p>Se você não se registrou em nossa plataforma, ignore este e-mail.</p>\n" +
                            "        <p>Atenciosamente,<br>A equipe do Smart Parking</p>\n" +
                            "    </div>\n" +
                            "    <div class=\"footer\">\n" +
                            "        <p>© 2024 Smart Parking. Todos os direitos reservados.</p>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>\n"
            );


        EmailVerificatorDTO emailResponseDTO = new EmailVerificatorDTO(
                "E-mail de verificação enviado para " + email
        );

        return emailResponseDTO;
    }


}
