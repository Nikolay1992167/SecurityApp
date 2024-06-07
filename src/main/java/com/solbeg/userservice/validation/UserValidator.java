package com.solbeg.userservice.validation;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.UniqueEmailException;
import com.solbeg.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void checkUniqueEmail(String userEmail) {
        if (userRepository.findByEmail(userEmail).isPresent()) {
            throw new UniqueEmailException(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + userEmail);
        }
    }

    public void checkUniqueEmail(String userEmail, UUID userId) {
        Optional<User> existingUserByEmail = userRepository.findByEmail(userEmail);
        if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getId().equals(userId)) {
            throw new UniqueEmailException(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + userEmail);
        }
    }
}
