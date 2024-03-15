package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.entity.EmailRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.enums.EmailType;
import com.solbeg.userservice.enums.Status;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.InformationChangeStatusUserException;
import com.solbeg.userservice.exception.NoSuchUserEmailException;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.exception.TokenExpirationException;
import com.solbeg.userservice.exception.UniqueEmailException;
import com.solbeg.userservice.mapper.UserMapperImpl;
import com.solbeg.userservice.repository.RoleRepository;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.service.SendingDataService;
import com.solbeg.userservice.service.UserTokenService;
import com.solbeg.userservice.util.testdata.EmailRequestTestData;
import com.solbeg.userservice.util.testdata.RoleTestData;
import com.solbeg.userservice.util.testdata.UserTestData;
import com.solbeg.userservice.util.testdata.UserTokenTestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.EXPIRED_AT_USERTOKEN;
import static com.solbeg.userservice.util.initdata.InitData.ID_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.NAME_ROLE_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_USERTOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserMapperImpl.class})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserTokenService tokenService;

    @Mock
    private SendingDataService sendingDataService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    class RegisterJournalist {

        @Test
        void shouldCheckSuccessRegistration() {
            // given
            UserRegisterRequest registerRequest = UserTestData.builder()
                    .build()
                    .getRegisterRequestJournalist();
            User expectedUser = UserTestData.builder()
                    .withStatus(Status.NOT_ACTIVE)
                    .build()
                    .getJournalist();
            Role role = RoleTestData.builder()
                    .build()
                    .getEntity();
            when(roleRepository.findByName(NAME_ROLE_JOURNALIST))
                    .thenReturn(role);
            when(userRepository.persistAndFlush(any(User.class)))
                    .thenReturn(expectedUser);

            // when
            userService.registerJournalist(registerRequest);

            // then
            verify(userRepository, times(1)).persistAndFlush(any(User.class));
            verify(tokenService, times(1)).createActivationToken(expectedUser.getId());
        }

        @Test
        void shouldReturnThrowExceptionWhenEmailIsFound() {
            // given
            UserRegisterRequest registerRequest = UserTestData.builder()
                    .build()
                    .getRegisterRequestJournalist();
            User user = UserTestData.builder()
                    .build()
                    .getJournalist();
            when(userRepository.findByEmail(registerRequest.getEmail()))
                    .thenReturn(Optional.of(user));

            // when, then
            assertThatThrownBy(() -> userService.registerJournalist(registerRequest))
                    .isInstanceOf(UniqueEmailException.class)
                    .hasMessageContaining(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + registerRequest.getEmail());
        }
    }

    @Nested
    class RegisterSubscriber {

        @Test
        void shouldCheckSuccessRegistration() {
            // given
            UserRegisterRequest registerRequest = UserTestData.builder()
                    .build()
                    .getRegisterRequestSubscriber();
            User savedUser = UserTestData.builder()
                    .build().getSubscriber();
            when(userRepository.persist(any(User.class)))
                    .thenReturn(savedUser);

            // when
            userService.registerSubscriber(registerRequest);

            // then
            verify(userRepository, times(1)).persist(any(User.class));
        }

        @Test
        void shouldReturnThrowExceptionWhenEmailIsFound() {
            // given
            UserRegisterRequest registerRequest = UserTestData.builder()
                    .build()
                    .getRegisterRequestSubscriber();
            User user = UserTestData.builder()
                    .build()
                    .getSubscriber();
            when(userRepository.findByEmail(registerRequest.getEmail()))
                    .thenReturn(Optional.of(user));

            // when, then
            assertThatThrownBy(() -> userService.registerSubscriber(registerRequest))
                    .isInstanceOf(UniqueEmailException.class)
                    .hasMessageContaining(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + registerRequest.getEmail());
        }
    }

    @Nested
    class FindAll {

        @Test
        void shouldReturnListOfPersonResponse() {
            // given
            int expectedSize = 1;
            List<User> usersList = List.of(UserTestData.builder()
                    .build()
                    .getJournalist());
            Page<User> page = new PageImpl<>(usersList);
            when(userRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<UserResponse> actual = userService.findAll(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldCheckEmpty() {
            // given
            Page<User> page = new PageImpl<>(List.of());
            when(userRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<UserResponse> actual = userService.findAll(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class findUserById {

        @Test
        void shouldReturnExpectedUserResponse() {
            // given
            UUID id = ID_JOURNALIST;
            User user = UserTestData.builder()
                    .build()
                    .getJournalist();
            UserResponse expectedResponse = UserTestData.builder()
                    .build()
                    .getUserResponse();
            when(userRepository.findById(id))
                    .thenReturn(Optional.of(user));

            // when
            UserResponse actualResponse = userService.findUserById(id);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }

        @Test
        void shouldReturnThrowExceptionWhenUserNotFound() {
            // given
            UUID userId = ID_JOURNALIST;
            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.findUserById(userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_FOUND.getMessage() + userId);
        }
    }

    @Nested
    class FindByUserEmail {

        @Test
        void shouldReturnExpectedUser() {
            // given
            String userEmail = EMAIL_JOURNALIST;
            User expectedUser = UserTestData.builder()
                    .build()
                    .getJournalist();
            when(userRepository.findByEmail(userEmail))
                    .thenReturn(Optional.of(expectedUser));

            // when
            Optional<User> actualUser = userService.findByUserEmail(userEmail);

            // then
            assertThat(actualUser).isPresent();
            assertThat(actualUser.get()).isEqualTo(expectedUser);
        }

        @Test
        void shouldReturnThrowExceptionWhenUserNotFound() {
            // given
            String userEmail = EMAIL_JOURNALIST;
            when(userRepository.findByEmail(userEmail))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.findByUserEmail(userEmail))
                    .isInstanceOf(NoSuchUserEmailException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_EXIST.getMessage() + userEmail);
        }
    }

    @Nested
    class FindById {

        @Test
        void shouldReturnExpectedUser() {
            // given
            UUID id = ID_JOURNALIST;
            User expectedUser = UserTestData.builder()
                    .build()
                    .getJournalist();
            when(userRepository.findById(id))
                    .thenReturn(Optional.ofNullable(expectedUser));

            // when
            User actualResponse = userService.findById(id);

            // then
            assertThat(actualResponse).isEqualTo(expectedUser);
        }

        @Test
        void shouldReturnThrowExceptionWhenUserNotFound() {
            // given
            UUID userId = ID_JOURNALIST;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.findById(userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_FOUND.getMessage() + userId);
        }
    }

    @Nested
    class Update {

        @Test
        void shouldReturnExpectedUserResponse() {
            // given
            UUID userId = ID_JOURNALIST;
            UserUpdateRequest updateRequest = UserTestData.builder()
                    .build()
                    .getUserUpdateRequest();
            User userInDB = UserTestData.builder()
                    .build()
                    .getJournalist();
            User updatedUser = UserTestData.builder()
                    .build()
                    .getJournalist();
            UserResponse expectedResponse = UserTestData.builder()
                    .build()
                    .getUserResponse();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(userInDB));
            when(userRepository.persist(userInDB))
                    .thenReturn(updatedUser);

            // when
            UserResponse actualResponse = userService.update(userId, updateRequest);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }

        @Test
        void shouldThrowExceptionWhenUserNotFound() {
            // given
            UUID userId = ID_JOURNALIST;
            UserUpdateRequest updateRequest = UserTestData.builder()
                    .build()
                    .getUserUpdateRequest();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.update(userId, updateRequest))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_FOUND.getMessage() + userId);
        }
    }

    @Nested
    class ActivateJournalistAccount {

        @Test
        void shouldActivateUserJournalist() {
            // given
            String tokenUser = TOKEN_USERTOKEN;
            String tokenAdmin = TOKEN_ADMIN;
            User user = UserTestData.builder()
                    .build()
                    .getJournalist();
            UserToken userToken = UserTokenTestData.builder()
                    .build()
                    .getUserToken();
            EmailRequest emailRequest = EmailRequestTestData.builder()
                    .build()
                    .getEmailRequest();
            when(userRepository.findByUserWhenRegistrationWithToken(tokenUser))
                    .thenReturn(Optional.of(user));
            when(tokenService.getByToken(tokenUser))
                    .thenReturn(userToken);
            when(sendingDataService.getEmailRequest(user, EmailType.USER_WELCOME_EMAIL))
                    .thenReturn(emailRequest);

            // when
            userService.activateJournalistAccount(tokenUser, tokenAdmin);

            // then
            verify(userRepository, times(1)).persist(user);
            verify(tokenService, times(1)).deleteUserToken(tokenUser);
            verify(sendingDataService, times(1)).sendRequestToMailService(any(EmailRequest.class));
        }

        @Test
        void shouldThrowExceptionWhenUserTokenNotFound() {
            // given
            String tokenUser = TOKEN_USERTOKEN;
            // when
            when(userRepository.findByUserWhenRegistrationWithToken(tokenUser))
                    .thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> userService.activateJournalistAccount(tokenUser, TOKEN_ADMIN))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USERTOKEN_NOT_FOUND.getMessage() + tokenUser);
        }

        @Test
        public void shouldThrowExceptionWhenUserTokenExpired() {
            // given
            String tokenUser = TOKEN_USERTOKEN;
            User user = UserTestData.builder()
                    .build()
                    .getJournalist();
            UserToken userToken = UserTokenTestData.builder()
                    .withExpirationAt(EXPIRED_AT_USERTOKEN)
                    .build()
                    .getUserToken();
            // when
            when(userRepository.findByUserWhenRegistrationWithToken(tokenUser))
                    .thenReturn(Optional.of(user));
            when(tokenService.getByToken(tokenUser))
                    .thenReturn(userToken);

            // then
            assertThatThrownBy(() -> userService.activateJournalistAccount(tokenUser, TOKEN_ADMIN))
                    .isInstanceOf(TokenExpirationException.class)
                    .hasMessageContaining(ErrorMessage.TOKEN_EXPIRED.getMessage());
        }
    }

    @Nested
    class DeactivateUser {

        @Test
        void shouldDeactivateUser() {
            // given
            UUID userId = ID_JOURNALIST;
            String token = REFRESH_TOKEN;
            User user = UserTestData.builder()
                    .build()
                    .getJournalist();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));
            when(jwtTokenProvider.getIdInFormatUUID(token))
                    .thenReturn(userId);

            // when
            userService.deactivateUser(userId, token);

            // then
            verify(userRepository, times(1)).persist(user);
            assertThat(user.getStatus()).isEqualTo(Status.NOT_ACTIVE);
        }

        @Test
        void shouldThrowExceptionWhenUserIsAdmin() {
            // given
            UUID userId = ID_ADMIN;
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            User user = UserTestData.builder()
                    .withRolesUser(List.of(adminRole))
                    .build()
                    .getJournalist();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));

            // when, then
            assertThatThrownBy(() -> userService.deactivateUser(userId, TOKEN_ADMIN))
                    .isInstanceOf(InformationChangeStatusUserException.class);
        }

        @Test
        void shouldThrowExceptionWhenUserNotFound() {
            // given
            UUID userId = ID_ADMIN;
            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.deactivateUser(userId, TOKEN_ADMIN))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_FOUND.getMessage() + userId);
        }
    }

    @Nested
    class DeleteUser {

        @Test
        void shouldDeactivateUser() {
            // given
            UUID userId = ID_JOURNALIST;
            String token = REFRESH_TOKEN;
            User user = UserTestData.builder()
                    .build()
                    .getJournalist();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));
            when(jwtTokenProvider.getIdInFormatUUID(token))
                    .thenReturn(userId);

            // when
            userService.deleteUser(userId, token);

            // then
            verify(userRepository, times(1)).persist(user);
            assertThat(user.getStatus()).isEqualTo(Status.DELETED);
        }
    }
}