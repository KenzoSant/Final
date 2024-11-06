package com.fatec.smart_parking.user;

import com.fatec.smart_parking.core.exception.EmailAlreadyTakenException;
import com.fatec.smart_parking.core.exception.EmailNotValidException;
import com.fatec.smart_parking.core.validation.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserValidator {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EmailValidator emailValidator;

    public void checkEmailExists(String email) {
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyTakenException();
        }
    }

    public void checkEmailValidity(String email) {
        if (!(emailValidator.checkMailPattern(email))) {
            throw new EmailNotValidException();
        }
    }
}
