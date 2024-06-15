package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.mapper.UserMapper;
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.util.testdata.JwtTestData;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserMapper userMapper;

    @Test
    void shouldReturnExpectedJwtResponseWhenLogin() {
        // given
        JwtRequest loginRequest = JwtTestData.getJwtRequest();
        User user = UserTestData.getJournalist();
        JwtResponse expectedResponse = JwtTestData.getJwtResponse();
        when(userService.findActiveUserByEmailOrThrowException(loginRequest.getEmail()))
                .thenReturn(user);
        when(userMapper.toJwtResponse(user))
                .thenReturn(expectedResponse);

        // when
        JwtResponse actualResponse = authService.login(loginRequest);

        // then
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }


    @Test
    void shouldReturnExpectedJwtResponseWhenRefresh() {
        // given
        String refreshToken = REFRESH_TOKEN;
        JwtResponse expectedResponse = JwtTestData.getJwtResponse();
        when(jwtTokenProvider.refreshUserToken(refreshToken))
                .thenReturn(expectedResponse);

        // when
        JwtResponse actualResponse = authService.getRefreshToken(refreshToken);

        // then
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}