package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.JwtParsingException;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.security.props.JwtProperties;
import com.solbeg.userservice.util.testdata.UserTestData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.ACCESS_TEST;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.INCORRECT_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TEST;
import static com.solbeg.userservice.util.initdata.InitData.SECRET_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserIdentityServiceImplTest {

    @InjectMocks
    private UserIdentityServiceImpl userIdentityService;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private UserRepository userRepository;

    private Key key;

    @BeforeEach
    public void setup() {
        when(jwtProperties.getSecret()).thenReturn(SECRET_TEST);
        when(jwtProperties.getAccess()).thenReturn(ACCESS_TEST);
        when(jwtProperties.getRefresh()).thenReturn(REFRESH_TEST);
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Nested
    class GetIdInFormatUUID {

        @Test
        @SneakyThrows
        void shouldReturnExpectedUUIDWhenTokenIsValid() {
            // given
            UUID expectedId = ID_JOURNALIST;
            String expectedUsername = EMAIL_JOURNALIST;
            Claims claims = Jwts.claims().setSubject(expectedUsername);
            claims.put("id", expectedId);
            Instant validity = Instant.now()
                    .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(Date.from(validity))
                    .signWith(key)
                    .compact();

            // when
            UUID actualId = userIdentityService.getIdInFormatUUID(token);

            // then
            assertThat(actualId).isEqualTo(expectedId);
        }

        @Test
        void shouldThrowJwtParsingExceptionWhenTokenIsIncorrect() {
            assertThatThrownBy(() -> userIdentityService.getIdInFormatUUID(INCORRECT_TOKEN))
                    .isInstanceOf(JwtParsingException.class)
                    .hasMessageContaining(ErrorMessage.ERROR_PARSING.getMessage());
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