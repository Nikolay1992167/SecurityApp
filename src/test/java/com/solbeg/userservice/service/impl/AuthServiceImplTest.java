package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.exception.NoSuchUserEmailException;
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.util.testdata.JwtData;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

import static com.solbeg.userservice.util.initdata.InitData.ACCESS_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    class Login {

        @Test
        void shouldReturnExpectedJwtResponse() {
            // given
            JwtRequest loginRequest = JwtData.builder()
                    .build()
                    .getJwtRequest();
            User user = UserTestData.builder()
                    .build()
                    .getUser();
            JwtResponse expectedResponse = JwtData.builder()
                    .build()
                    .getJwtResponse();

            when(userService.findByUserEmail(loginRequest.getEmail()))
                    .thenReturn(Optional.of(user));
            when(jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRoles()))
                    .thenReturn(ACCESS_TOKEN);
            when(jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail()))
                    .thenReturn(REFRESH_TOKEN);

            // when
            JwtResponse actualResponse = authService.login(loginRequest);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }

        @Test
        void shouldReturnThrowExceptionWhenEmailIsNotFound() {
            // given
            JwtRequest loginRequest = JwtData.builder()
                    .build()
                    .getJwtRequest();
            when(userService.findByUserEmail(loginRequest.getEmail()))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isExactlyInstanceOf(NoSuchUserEmailException.class);
        }
    }

    @Nested
    class Refresh {

        @Test
        void shouldReturnExpectedJwtResponse() {
            // given
            String refreshToken = REFRESH_TOKEN;
            JwtResponse expectedResponse = JwtData.builder()
                    .build()
                    .getJwtResponse();
            when(jwtTokenProvider.refreshUserToken(refreshToken))
                    .thenReturn(expectedResponse);

            // when
            JwtResponse actualResponse = authService.refresh(refreshToken);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }
    }
}