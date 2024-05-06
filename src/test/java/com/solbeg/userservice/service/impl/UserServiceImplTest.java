package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.Role;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.entity.User_;
import com.solbeg.userservice.enums.EmailType;
import com.solbeg.userservice.enums.Status;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.InformationChangeStatusUserException;
import com.solbeg.userservice.exception.NoSuchUserEmailException;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.exception.TokenExpirationException;
import com.solbeg.userservice.mapper.UserMapper;
import com.solbeg.userservice.repository.RoleRepository;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.security.jwt.JwtUser;
import com.solbeg.userservice.service.SendingDataService;
import com.solbeg.userservice.service.UserIdentityService;
import com.solbeg.userservice.service.UserTokenService;
import com.solbeg.userservice.util.testdata.JwtUserTestData;
import com.solbeg.userservice.util.testdata.RoleTestData;
import com.solbeg.userservice.util.testdata.UserTestData;
import com.solbeg.userservice.util.testdata.UserTokenTestData;
import com.solbeg.userservice.validation.UserValidator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.solbeg.userservice.util.Constants.ROLE_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST;
import static com.solbeg.userservice.util.initdata.InitData.ROLE_NAME_SUBSCRIBER;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_USERTOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {



    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserTokenService tokenService;

    @Mock
    private SendingDataService sendingDataService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserIdentityService userIdentityService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserMapper userMapper;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Nested
    class RegisterJournalist {

        @Test
        void shouldCheckSuccessRegistration() {
            // given
            UserRegisterRequest registerRequest = UserTestData.getRegisterRequestJournalist();
            User savedUser = UserTestData.getJournalistWithStatusNotActive();
            when(userMapper.fromRequest(registerRequest, Status.NOT_ACTIVE))
                    .thenReturn(savedUser);
            when(roleRepository.findByName(ROLE_JOURNALIST))
                    .thenReturn(RoleTestData.getJournalist());
            when(userRepository.persistAndFlush(savedUser))
                    .thenReturn(savedUser);

            // when
            userService.registerJournalist(registerRequest);

            // then
            verify(userRepository, times(1)).persistAndFlush(any(User.class));
        }
    }

    @Nested
    class RegisterSubscriber {

        @Test
        void shouldCheckSuccessRegistration() {
            // given
            UserRegisterRequest registerRequest = UserTestData.getRegisterRequestSubscriber();
            User savedUser = UserTestData.getSubscriber();
            when(userMapper.fromRequest(registerRequest, Status.ACTIVE))
                    .thenReturn(savedUser);
            when(roleRepository.findByName(ROLE_NAME_SUBSCRIBER))
                    .thenReturn(RoleTestData.getSubscriber());
            when(userRepository.persistAndFlush(any(User.class)))
                    .thenReturn(savedUser);

            // when
            userService.registerSubscriber(registerRequest);

            // then
            verify(userRepository, times(1)).persistAndFlush(any(User.class));
        }
    }

    @Nested
    class GetAllUsers {

        @Test
        void shouldReturnListOfPersonResponse() {
            // given
            int expectedSize = 1;
            List<User> usersList = List.of(UserTestData.getJournalist());
            Page<User> page = new PageImpl<>(usersList);
            when(userRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<UserResponse> actual = userService.getAllUsers(DEFAULT_PAGE_REQUEST_FOR_IT);

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
            Page<UserResponse> actual = userService.getAllUsers(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class FindUserById {

        @Test
        void shouldReturnExpectedUserResponse() {
            // given
            UUID userId = ID_JOURNALIST;
            User user = UserTestData.getJournalist();
            UserResponse expectedResponse = UserTestData.getUserResponse();
            when(userIdentityService.getUserOrThrowException(userId))
                    .thenReturn(user);
            when(userMapper.toResponse(user))
                    .thenReturn(expectedResponse);

            // when
            UserResponse actualResponse = userService.findUserById(userId);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }
    }

    @Nested
    class FindByUserEmail {

        @Test
        void shouldReturnExpectedUser() {
            // given
            String userEmail = EMAIL_JOURNALIST;
            User expectedUser = UserTestData.getJournalist();
            when(userRepository.findByEmailAndStatus(userEmail, Status.ACTIVE))
                    .thenReturn(Optional.of(expectedUser));

            // when
            User actualUser = userService.findActiveUserByEmailOrThrowException(userEmail);

            // then
            assertThat(actualUser).isEqualTo(expectedUser);
        }

        @Test
        void shouldReturnThrowExceptionWhenUserNotFound() {
            // given
            String userEmail = EMAIL_JOURNALIST;
            when(userRepository.findByEmailAndStatus(userEmail, Status.ACTIVE))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.findActiveUserByEmailOrThrowException(userEmail))
                    .isInstanceOf(NoSuchUserEmailException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_EXIST.getMessage() + userEmail);
        }
    }

    @Nested
    class Update {

        @Test
        void shouldReturnExpectedUserResponse() {
            // given
            UUID userId = ID_JOURNALIST;
            UserUpdateRequest updateRequest = UserTestData.getUserUpdateRequest();
            User userInDB = UserTestData.getJournalist();
            User updatedUser = UserTestData.getJournalist();
            UserResponse expectedResponse = UserTestData.getUserResponse();
            when(userIdentityService.getUserOrThrowException(userId))
                    .thenReturn(userInDB);
            doNothing().when(userMapper).update(userInDB, updateRequest, userId);
            when(userRepository.update(userInDB))
                    .thenReturn(updatedUser);
            when(userMapper.toResponse(userInDB))
                    .thenReturn(expectedResponse);

            // when
            UserResponse actualResponse = userService.updateUserById(userId, updateRequest);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }
    }

    @Nested
    class ActivateJournalistAccount {

        @Test
        void shouldActivateUserJournalist() {
            // given
            String tokenUser = TOKEN_USERTOKEN;
            User user = UserTestData.getJournalist();
            UserToken userToken = UserTokenTestData.getUserToken();
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            when(tokenService.getByToken(tokenUser))
                    .thenReturn(userToken);
            doNothing().when(sendingDataService).sendInformation(user, EmailType.USER_WELCOME_EMAIL);
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // when
            userService.activateJournalistAccount(tokenUser);

            // then
            verify(userRepository, times(1)).persist(userCaptor.capture());
            assertThat(userCaptor.getValue())
                    .hasFieldOrPropertyWithValue(User_.EMAIL, user.getEmail())
                    .hasFieldOrPropertyWithValue(User_.PASSWORD, user.getPassword());
            verify(tokenService, times(1)).deleteUserTokenByToken(tokenUser);
        }

        @Test
        public void shouldThrowExceptionWhenUserTokenExpired() {
            // given
            String tokenUser = TOKEN_USERTOKEN;
            UserToken userToken = UserTokenTestData.getExpiredUserToken();

            // when
            when(tokenService.getByToken(tokenUser))
                    .thenReturn(userToken);

            // then
            assertThatThrownBy(() -> userService.activateJournalistAccount(tokenUser))
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
            User user = UserTestData.getJournalist();
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // when
            userService.deactivateUserById(userId);

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
            User user = UserTestData.getAdmin();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));

            // when, then
            assertThatThrownBy(() -> userService.deactivateUserById(userId))
                    .isInstanceOf(InformationChangeStatusUserException.class);
        }

        @Test
        void shouldThrowExceptionWhenUserNotFound() {
            // given
            UUID userId = ID_ADMIN;
            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.deactivateUserById(userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_FOUND.getMessage() + userId);
        }
    }

    @Nested
    class DeleteUser {

        @Test
        void shouldDeletedUser() {
            // given
            UUID userId = ID_JOURNALIST;
            User user = UserTestData.getJournalist();
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // when
            userService.deleteUserById(userId);

            // then
            verify(userRepository, times(1)).persist(user);
            assertThat(user.getStatus()).isEqualTo(Status.DELETED);
        }
    }
}