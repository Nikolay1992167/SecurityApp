package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.enums.TokenType;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.repository.UserTokenRepository;
import com.solbeg.userservice.util.testdata.UserTokenTestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_USERTOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserTokenServiceImplTest {

    @InjectMocks
    private UserTokenServiceImpl userTokenService;

    @Mock
    private UserTokenRepository tokenRepository;

    @Nested
    class CreateActivationToken {

        @Test
        void shouldReturnExpectedValue() {
            // given
            UUID userId = ID_JOURNALIST;
            UserToken expected = UserTokenTestData.builder()
                    .build()
                    .getUserToken();
            when(tokenRepository.persist(any(UserToken.class)))
                    .thenReturn(expected);

            // when
            UserToken actual = userTokenService.createActivationToken(userId);

            // then
            assertThat(actual.getCreatedBy()).isEqualTo(userId);
            assertThat(actual.getTokenType()).isEqualTo(TokenType.ACTIVATION);
            assertThat(actual.getExpirationAt()).isAfter(LocalDateTime.now());
        }
    }

    @Nested
    class GetByToken {

        @Test
        void shouldReturnExpectedUserToken() {
            // given
            String tokenUser = TOKEN_USERTOKEN;
            UserToken expected = UserTokenTestData.builder()
                    .build()
                    .getUserToken();
            when(tokenRepository.findByToken(tokenUser))
                    .thenReturn(Optional.ofNullable(expected));

            // when
            UserToken actual = userTokenService.getByToken(tokenUser);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldReturnThrowExceptionWhenUserTokenNotFound() {
            // given
            String tokenUser = TOKEN_USERTOKEN;
            when(tokenRepository.findByToken(tokenUser))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userTokenService.getByToken(tokenUser))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USERTOKEN_NOT_FOUND.getMessage() + tokenUser);
        }
    }

    @Nested
    class GetAll {

        @Test
        void shouldReturnListOfUserToken() {
            // given
            int expectedSize = 1;
            List<UserToken> userTokens = List.of(UserTokenTestData.builder()
                    .build()
                    .getUserToken());
            Page<UserToken> page = new PageImpl<>(userTokens);
            when(tokenRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<UserToken> actual = userTokenService.getAll(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldCheckEmpty() {
            // given
            Page<UserToken> page = new PageImpl<>(List.of());
            when(tokenRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<UserToken> actual = userTokenService.getAll(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class DeleteUserToken {

        @Test
        void shouldDeleteUserTokenByToken() {
            // given
            String token = TOKEN_USERTOKEN;
            UserToken userToken = UserTokenTestData.builder()
                    .build()
                    .getUserToken();
            when(tokenRepository.findByToken(token))
                    .thenReturn(Optional.of(userToken));

            // when
            userTokenService.deleteUserToken(token);

            // then
            verify(tokenRepository, times(1)).findByToken(token);
            verify(tokenRepository, times(1)).deleteByToken(token);
        }

        @Test
        void shouldReturnThrowIfUserTokenNotExistWithToken() {
            // given
            String token = TOKEN_USERTOKEN;

            // when, then
            assertThatThrownBy(() -> userTokenService.deleteUserToken(token))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USERTOKEN_NOT_FOUND.getMessage() + token);
        }
    }

    @Test
    void shouldCallMethodDeleteOldUserTokens() {
        // given, when
        userTokenService.deleteOldUserTokens();

        // then
        verify(tokenRepository, times(1)).deleteByExpirationAtBefore(any(LocalDateTime.class));
    }
}