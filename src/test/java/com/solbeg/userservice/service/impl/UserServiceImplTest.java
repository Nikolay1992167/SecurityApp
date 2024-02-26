package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserRegisterResponse;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.Status;
import com.solbeg.userservice.exception.InformationChangeStatusUserException;
import com.solbeg.userservice.exception.NoSuchUserEmailException;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.exception.UniqueEmailException;
import com.solbeg.userservice.mapper.UserMapper;
import com.solbeg.userservice.repository.RoleRepository;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.util.testdata.RoleTestData;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.solbeg.userservice.util.initdata.InitData.CREATED_AT_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.CREATED_BY_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.ID_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.ROLE_NAME_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.SUBSCRIBER_FIRST_NAME;
import static com.solbeg.userservice.util.initdata.InitData.SUBSCRIBER_LAST_NAME;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_AT_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.UPDATED_BY_SUBSCRIBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    class RegisterJournalist {

        @Test
        void shouldReturnExpectedUserRegisterResponse() {
            // given
            UserRegisterRequest registerRequest = UserTestData.builder()
                    .build()
                    .getUserRegisterRequest();
            User savedUser = UserTestData.builder()
                    .build()
                    .getUser();
            UserRegisterResponse expectedResponse = UserTestData.builder()
                    .build()
                    .getUserRegisterResponse();
            when(userRepository.persist(any(User.class)))
                    .thenReturn(savedUser);

            // when
            UserRegisterResponse actualResponse = userService.registerJournalist(registerRequest);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }

        @Test
        void shouldReturnThrowExceptionWhenEmailIsNotFound() {
            // given
            UserRegisterRequest registerRequest = UserTestData.builder()
                    .build()
                    .getUserRegisterRequest();
            User user = UserTestData.builder()
                    .build()
                    .getUser();
            when(userRepository.findByEmail(registerRequest.getEmail()))
                    .thenReturn(Optional.of(user));

            // when, then
            assertThatThrownBy(() -> userService.registerJournalist(registerRequest))
                    .isInstanceOf(UniqueEmailException.class);
        }
    }

    @Nested
    class RegisterSubscriber {

        @Test
        void shouldReturnExpectedUserRegisterResponse() {
            // given
            UserRegisterRequest registerRequest = UserTestData.builder()
                    .withFirstName(SUBSCRIBER_FIRST_NAME)
                    .withLastName(SUBSCRIBER_LAST_NAME)
                    .withEmail(EMAIL_SUBSCRIBER)
                    .withPassword(PASSWORD_SUBSCRIBER)
                    .build()
                    .getUserRegisterRequest();
            User savedUser = UserTestData.builder()
                    .withId(ID_SUBSCRIBER)
                    .withCreatedBy(CREATED_BY_SUBSCRIBER)
                    .withUpdatedBy(UPDATED_BY_SUBSCRIBER)
                    .withCreatedAt(CREATED_AT_SUBSCRIBER)
                    .withUpdatedAt(UPDATED_AT_SUBSCRIBER)
                    .withFirstName(SUBSCRIBER_FIRST_NAME)
                    .withLastName(SUBSCRIBER_LAST_NAME)
                    .withEmail(EMAIL_SUBSCRIBER)
                    .withPassword(PASSWORD_SUBSCRIBER)
                    .withStatus(Status.ACTIVE)
                    .withRolesUser(List.of(RoleTestData.builder()
                            .withName(ROLE_NAME_SUBSCRIBER)
                            .build()
                            .getEntity()))
                    .build()
                    .getUser();
            UserRegisterResponse expectedResponse = UserTestData.builder()
                    .withId(ID_SUBSCRIBER)
                    .withFirstName(SUBSCRIBER_FIRST_NAME)
                    .withLastName(SUBSCRIBER_LAST_NAME)
                    .withPassword(PASSWORD_SUBSCRIBER)
                    .withEmail(EMAIL_SUBSCRIBER)
                    .withRoles(List.of(ROLE_NAME_SUBSCRIBER))
                    .withStatus(Status.ACTIVE)
                    .build()
                    .getUserRegisterResponse();
            when(userRepository.persist(any(User.class)))
                    .thenReturn(savedUser);

            // when
            UserRegisterResponse actualResponse = userService.registerSubscriber(registerRequest);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }

        @Test
        void shouldReturnThrowExceptionWhenEmailIsNotFound() {
            // given
            UserRegisterRequest registerRequest = UserTestData.builder()
                    .withFirstName(SUBSCRIBER_FIRST_NAME)
                    .withLastName(SUBSCRIBER_LAST_NAME)
                    .withEmail(EMAIL_SUBSCRIBER)
                    .withPassword(PASSWORD_SUBSCRIBER)
                    .build()
                    .getUserRegisterRequest();
            User user = UserTestData.builder()
                    .withId(ID_SUBSCRIBER)
                    .withCreatedBy(CREATED_BY_SUBSCRIBER)
                    .withUpdatedBy(UPDATED_BY_SUBSCRIBER)
                    .withCreatedAt(CREATED_AT_SUBSCRIBER)
                    .withUpdatedAt(UPDATED_AT_SUBSCRIBER)
                    .withFirstName(SUBSCRIBER_FIRST_NAME)
                    .withLastName(SUBSCRIBER_LAST_NAME)
                    .withEmail(EMAIL_SUBSCRIBER)
                    .withPassword(PASSWORD_SUBSCRIBER)
                    .withStatus(Status.ACTIVE)
                    .withRolesUser(List.of(RoleTestData.builder()
                            .withName(ROLE_NAME_SUBSCRIBER)
                            .build()
                            .getEntity()))
                    .build()
                    .getUser();
            when(userRepository.findByEmail(registerRequest.getEmail()))
                    .thenReturn(Optional.of(user));

            // when, then
            assertThatThrownBy(() -> userService.registerSubscriber(registerRequest))
                    .isInstanceOf(UniqueEmailException.class);
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
                    .getUser());
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
                    .getUser();
            UserResponse expectedResponse = UserTestData.builder()
                    .build()
                    .getUserResponse();
            when(userRepository.findById(id)).thenReturn(Optional.of(user));

            // when
            UserResponse actualResponse = userService.findUserById(id);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }

        @Test
        void shouldReturnThrowExceptionWhenUserNotFound() {
            // given
            UUID userId = ID_JOURNALIST;
            User user = UserTestData.builder()
                    .build()
                    .getUser();
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.findUserById(userId))
                    .isInstanceOf(NotFoundException.class);
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
                    .getUser();
            when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(expectedUser));

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
            when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.findByUserEmail(userEmail))
                    .isInstanceOf(NoSuchUserEmailException.class);
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
                    .getUser();
            when(userRepository.findById(id)).thenReturn(Optional.ofNullable(expectedUser));

            // when
            User actualResponse = userService.findById(id);

            // then
            assertThat(actualResponse).isEqualTo(expectedUser);
        }

        @Test
        void shouldReturnThrowExceptionWhenUserNotFound() {
            // given
            UUID userId = ID_JOURNALIST;
            User user = UserTestData.builder()
                    .build()
                    .getUser();
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.findById(userId))
                    .isInstanceOf(NotFoundException.class);
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
                    .getUser();
            User updatedUser = UserTestData.builder()
                    .build()
                    .getUser();
            UserResponse expectedResponse = UserTestData.builder()
                    .build()
                    .getUserResponse();
            when(userRepository.findById(userId)).thenReturn(Optional.of(userInDB));
            when(userRepository.persist(userInDB)).thenReturn(updatedUser);

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
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.update(userId, updateRequest))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class DeactivateUser {

        @Test
        void shouldDeactivateUser() {
            // given
            UUID userId = UUID.randomUUID();
            String token = REFRESH_TOKEN;
            User user = UserTestData.builder()
                    .build()
                    .getUser();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(jwtTokenProvider.getIdInFormatUUID(token)).thenReturn(userId);

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
                    .getUser();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            // when, then
            assertThatThrownBy(() -> userService.deactivateUser(userId, TOKEN_ADMIN))
                    .isInstanceOf(InformationChangeStatusUserException.class);
        }

        @Test
        void shouldThrowExceptionWhenUserNotFound() {
            // given
            UUID userId = ID_ADMIN;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.deactivateUser(userId, TOKEN_ADMIN))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class DeleteUser {

        @Test
        void shouldDeactivateUser() {
            // given
            UUID userId = UUID.randomUUID();
            String token = REFRESH_TOKEN;
            User user = UserTestData.builder()
                    .build()
                    .getUser();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(jwtTokenProvider.getIdInFormatUUID(token)).thenReturn(userId);

            // when
            userService.deleteUser(userId, token);

            // then
            verify(userRepository, times(1)).persist(user);
            assertThat(user.getStatus()).isEqualTo(Status.DELETED);
        }
    }
}