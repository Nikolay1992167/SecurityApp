package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserIdentityServiceImplTest {

    @InjectMocks
    private UserIdentityServiceImpl userIdentityService;

    @Mock
    private UserRepository userRepository;


    @Test
    void shouldReturnExpectedValue() {
        // given
        UUID userID = ID_JOURNALIST;
        User expected = UserTestData.getJournalist();
        when(userRepository.findUserFetchRolesById(userID))
                .thenReturn(Optional.ofNullable(expected));

        // when
        User actual = userIdentityService.getUserOrThrowException(userID);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnThrowExceptionWhenUserNotFound() {
        // given
        UUID userId = ID_JOURNALIST;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userIdentityService.getUserOrThrowException(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(ErrorMessage.USER_NOT_FOUND.getMessage() + userId);
    }
}