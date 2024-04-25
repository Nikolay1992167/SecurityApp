package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.security.jwt.JwtUser;
import com.solbeg.userservice.util.testdata.JwtUserTestData;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserIdentityServiceImplTest {

    @InjectMocks
    private UserIdentityServiceImpl userIdentityService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserRepository userRepository;

    @Nested
    class GetId {

        @Test
        void shouldReturnExpectedId() {
            // given
            UUID expectedId = ID_JOURNALIST;
            JwtUser jwtUser = JwtUserTestData.builder()
                    .build()
                    .getJwtUser();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // when
            UUID actualId = userIdentityService.getUserId();

            //then
            assertThat(actualId).isEqualTo(expectedId);
        }

        @Test
        void shouldReturnNull() {
            // given
            when(authentication.getPrincipal())
                    .thenReturn(new Object());
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // when
            UUID actualId = userIdentityService.getUserId();

            //then

            assertThat(actualId).isNull();
        }
    }

    @Nested
    class GetUserById {

        @Test
        void shouldReturnExpectedValue() {
            // given
            UUID userID = ID_JOURNALIST;
            User expected = UserTestData.builder()
                    .build()
                    .getJournalist();
            when(userRepository.findById(userID))
                    .thenReturn(Optional.ofNullable(expected));

            // when
            User actual = userIdentityService.getUserById(userID);

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
            assertThatThrownBy(() -> userIdentityService.getUserById(userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_FOUND.getMessage() + userId);
        }
    }
}