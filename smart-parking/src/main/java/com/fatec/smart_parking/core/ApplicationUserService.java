package com.fatec.smart_parking.core;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import com.fatec.smart_parking.client.ClientService;
import com.fatec.smart_parking.core.authentication.EmailResponseDTO;
import com.fatec.smart_parking.core.authentication.RegisterDTO;
import com.fatec.smart_parking.core.email.EmailService;
import com.fatec.smart_parking.core.listener.EmailSentEventDTO;
import com.fatec.smart_parking.email_verificator.EmailVerificatorService;
import com.fatec.smart_parking.user.User;
import com.fatec.smart_parking.user.UserDTO;
import com.fatec.smart_parking.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmailService emailService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int PASSWORD_LENGTH = 12;
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(randomIndex));
        }
        return password.toString();
    }

    @Override
    public User loadUserByUsername(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new UsernameNotFoundException(email);
    }

    public UserDTO create(RegisterDTO registerDTO) {
        return this.clientService.create(registerDTO);
    }


    public EmailResponseDTO resetPassword(EmailSentEventDTO emailSentEventDTO){
      Optional<User> optionalUser = userRepository.findByEmail(emailSentEventDTO.email());
        if(optionalUser.isPresent()){
            String newPassword = generateRandomPassword();
            String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
            optionalUser.get().setPassword(encryptedPassword);
            this.userRepository.save(optionalUser.get());
            this.emailService.sendHtmlMessage(emailSentEventDTO.email(),"Sua senha foi redefinida",
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"pt-BR\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                            "    <title>Senha Gerada</title>\n" +
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
                            "        .password {\n" +
                            "            font-weight: bold;\n" +
                            "            font-size: 18px;\n" +
                            "            color: #007bff;\n" +
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
                            "        <h1>Senha Gerada</h1>\n" +
                            "        <p>Olá,</p>\n" +
                            "        <p>Uma nova senha foi gerada para você:</p>\n" +
                            "        <p class=\"password\">"+newPassword+"</p>\n" +
                            "        <p>Use esta senha para acessar sua conta. Você pode alterá-la a qualquer momento nas configurações da sua conta.</p>\n" +
                            "        <p>Se precisar de ajuda, não hesite em entrar em contato.</p>\n" +
                            "        <p>Atenciosamente,<br>A equipe do Smart Parking</p>\n" +
                            "    </div>\n" +
                            "    <div class=\"footer\">\n" +
                            "        <p>© 2024 Smart Parking. Todos os direitos reservados.</p>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>\n");
        }

        EmailResponseDTO emailResponseDTO = new EmailResponseDTO(
                "E-mail successfully sent to "+emailSentEventDTO.email(),
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                LocalDateTime.now()
        );
    return emailResponseDTO;

    }


}
