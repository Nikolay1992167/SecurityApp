package com.solbeg.userservice.security.jwt;

import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.exception.JwtParsingException;
import com.solbeg.userservice.security.props.JwtProperties;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.util.testdata.UserTestData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.ACCESS_TEST;
import static com.solbeg.userservice.util.initdata.InitData.ACCESS_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.INCORRECT_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TEST;
import static com.solbeg.userservice.util.initdata.InitData.ROLE_NAME_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ROLE_NAME_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.SECRET_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class JwtTokenProviderTest {

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private Key key;

    @BeforeEach
    public void setup() {
        when(jwtProperties.getSecret()).thenReturn(SECRET_TEST);
        when(jwtProperties.getAccess()).thenReturn(ACCESS_TEST);
        when(jwtProperties.getRefresh()).thenReturn(REFRESH_TEST);
        jwtTokenProvider.init();
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Test
    void shouldReturnExpectedCreatedAccessToken() {
        // given
        Role role = new Role();
        role.setName(ROLE_NAME_JOURNALIST);

        // when
        String token = jwtTokenProvider.createAccessToken(ID_JOURNALIST, EMAIL_JOURNALIST, List.of(role));

        // then
        assertNotNull(token);
        assertThat(token).isInstanceOf(String.class);
    }

    @Test
    void shouldReturnExpectedCreatedRefreshToken() {
        // given
        Role role = new Role();
        role.setName(ROLE_NAME_JOURNALIST);

        // when
        String token = jwtTokenProvider.createRefreshToken(ID_JOURNALIST, EMAIL_JOURNALIST);

        // then
        assertNotNull(token);
        assertThat(token).isInstanceOf(String.class);
    }

    @Test
    void shouldReturnExpectedRefreshUserToken() {
        // given
        UUID userId = ID_JOURNALIST;
        String userEmail = EMAIL_JOURNALIST;
        User user = UserTestData.builder()
                .build()
                .getUser();
        when(userService.findById(userId))
                .thenReturn(user);

        // when
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, userEmail);
        JwtResponse jwtResponse = jwtTokenProvider.refreshUserToken(refreshToken);

        // then
        assertThat(jwtResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", userId)
                .hasFieldOrPropertyWithValue("email", userEmail);
    }

    @Test
    void shouldReturnThrowExceptionWhenTokenIsExpired() {
        // given
        Role role = new Role();
        role.setName(ROLE_NAME_SUBSCRIBER);
        Claims claims = Jwts.claims().setSubject(EMAIL_JOURNALIST);
        claims.put("id", ID_JOURNALIST);
        claims.put("roles", Collections.singletonList(role.getName()));
        Instant validity = Instant.now().minus(1, ChronoUnit.HOURS);
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();

        // when, then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
                .isInstanceOf(io.jsonwebtoken.JwtException.class);
    }

    @Test
    void shouldReturnFalseWhenTokenIsExpired() {
        // given
        Role role = new Role();
        role.setName(ROLE_NAME_SUBSCRIBER);
        Claims claims = Jwts.claims().setSubject(EMAIL_JOURNALIST);
        claims.put("id", ID_JOURNALIST);
        claims.put("roles", Collections.singletonList(role.getName()));
        Instant now = Instant.now();
        Instant expired = now.plusSeconds(3600);
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expired))
                .signWith(key)
                .compact();

        // when
        boolean isValid = jwtTokenProvider.validateToken(expiredToken);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnIdWhenTokenIsValid() {
        // given, when
        UUID actualId = jwtTokenProvider.getIdInFormatUUID(ACCESS_TOKEN);

        // then
        assertThat(actualId).isEqualTo(ID_JOURNALIST);
    }

    @Test
    void shouldThrowJwtParsingExceptionWhenTokenIsInvalid() {

        assertThatThrownBy(() -> jwtTokenProvider.getIdInFormatUUID(INCORRECT_TOKEN))
                .isInstanceOf(JwtParsingException.class);
    }
}