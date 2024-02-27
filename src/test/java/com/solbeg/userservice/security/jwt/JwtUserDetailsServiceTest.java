package com.solbeg.userservice.security.jwt;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private UserService userService;

    @Test
    void shouldLoadUserByUsername() {
        // given
        String emailUser = EMAIL_JOURNALIST;
        User user = UserTestData.builder()
                .build()
                .getUser();
        JwtUser expectedJwtUser = JwtUserFactory.create(user);
        when(userService.findByUserEmail(emailUser))
                .thenReturn(Optional.of(user));

        // when
        UserDetails actualJwtUser = jwtUserDetailsService.loadUserByUsername(emailUser);

        // then
        assertThat(actualJwtUser).isEqualTo(expectedJwtUser);
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        // given
        String emailUser = EMAIL_JOURNALIST;
        when(userService.findByUserEmail(emailUser))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> jwtUserDetailsService.loadUserByUsername(emailUser))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}