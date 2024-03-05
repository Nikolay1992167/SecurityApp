package com.solbeg.userservice.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @InjectMocks
    private ApplicationConfig config;

    @Test
    void shouldReturnExpectedInstanceOfBCryptPasswordEncoder() {
        // given
        Class<BCryptPasswordEncoder> expecteClass = BCryptPasswordEncoder.class;

        // when
        PasswordEncoder actualClass = config.passwordEncoder();

        // then
        assertThat(actualClass).isInstanceOf(expecteClass);
    }

    @Test
    void testShouldReturnAuthenticationManagerFromAuthenticationConfiguration() throws Exception {
        // given
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager expectedValue = mock(AuthenticationManager.class);

        doReturn(expectedValue)
                .when(configuration)
                .getAuthenticationManager();

        // when
        AuthenticationManager actualValue = config.authenticationManager(configuration);

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnExpectedInstanceOfSecurityFilterChain() throws Exception {
        // given
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        HttpSecurity httpSecurityWhen = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        DefaultSecurityFilterChain expectedValue = mock(DefaultSecurityFilterChain.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.authorizeHttpRequests(any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.exceptionHandling(any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.sessionManagement(any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.addFilterBefore(any(), any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.build()).thenReturn(expectedValue);

        // when
        SecurityFilterChain actualValue = config.securityFilterChain(httpSecurity);

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }
}