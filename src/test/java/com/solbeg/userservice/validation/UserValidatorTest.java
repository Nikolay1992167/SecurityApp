package com.solbeg.userservice.validation;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.UniqueEmailException;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_SUBSCRIBER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @InjectMocks
    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldThrowExceptionWhenEmailIsNotUnique() {
        // given
        String userEmail = EMAIL_JOURNALIST;
        User user = UserTestData.getJournalist();
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> userValidator.checkUniqueEmail(userEmail))
                .isInstanceOf(UniqueEmailException.class)
                .hasMessage(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + userEmail);
    }

    @Test
    void shouldNotThrowExceptionWhenEmailIsUnique() {
        // given
        String userEmail = EMAIL_SUBSCRIBER;
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.empty());

        // when, then
        assertThatCode(() -> userValidator.checkUniqueEmail(userEmail)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNotUniqueForDifferentUserId() {
        // given
        String userEmail = EMAIL_JOURNALIST;
        User user = UserTestData.getJournalist();
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> userValidator.checkUniqueEmail(userEmail, ID_SUBSCRIBER))
                .isInstanceOf(UniqueEmailException.class)
                .hasMessage(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + userEmail);
    }

    @Test
    void shouldNotThrowExceptionWhenEmailIsUniqueUserId() {
        // given
        String userEmail = EMAIL_SUBSCRIBER;
        UUID userId = ID_JOURNALIST;
        User user = UserTestData.getJournalist();
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));

        // when, then
        assertThatCode(() -> userValidator.checkUniqueEmail(userEmail, userId)).doesNotThrowAnyException();
    }
}