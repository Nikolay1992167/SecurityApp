package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.mapper.UserMapper;
import com.solbeg.userservice.security.jwt.JwtUser;
import com.solbeg.userservice.service.UserIdentityService;
import com.solbeg.userservice.util.testdata.JwtUserTestData;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private UserIdentityService userIdentityService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Test
    void shouldReturnExpectedUserResponse() {
        // given
        UUID userId = ID_JOURNALIST;
        User user = UserTestData.getJournalist();
        UserResponse expected = UserTestData.getUserResponse();
        JwtUser jwtUser = JwtUserTestData.getJwtUser();
        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        when(userIdentityService.getUserOrThrowException(userId))
                .thenReturn(user);
        when(userMapper.toResponse(user))
                .thenReturn(expected);
        SecurityContextHolder.setContext(securityContext);

        // when
        UserResponse actual = accountService.getUserData();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}