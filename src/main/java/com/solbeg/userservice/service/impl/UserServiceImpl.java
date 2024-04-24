package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.EmailRequest;
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
import com.solbeg.userservice.mapper.UserMapper;
import com.solbeg.userservice.repository.RoleRepository;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.service.SendingDataService;
import com.solbeg.userservice.service.UserIdentityService;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserTokenService userTokenService;
    private final SendingDataService sendingDataService;
    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserIdentityService userIdentityService;

    @Override
    @Transactional
    public void registerJournalist(UserRegisterRequest userRegisterRequest) {
        checkUniqueEmail(userRegisterRequest.getEmail());
        User userToSave = userMapper.fromRequest(userRegisterRequest);
        List<Role> userRoles = new ArrayList<>();
        Role journalistRole = roleRepository.findByName("JOURNALIST");
        userRoles.add(journalistRole);
        userToSave.setRoles(userRoles);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        userToSave.setStatus(Status.NOT_ACTIVE);
        User savedUser = userRepository.persistAndFlush(userToSave);
        UserToken activationToken = userTokenService.createActivationToken(savedUser.getId());
        EmailRequest emailRequest = sendingDataService.getEmailRequest(savedUser, activationToken);
        sendingDataService.sendRequestToMailService(emailRequest);
        log.info("IN registerJournalist user: {} successfully registered", userToSave);
    }

    @Override
    public void registerSubscriber(UserRegisterRequest userRegisterRequest) {
        checkUniqueEmail(userRegisterRequest.getEmail());
        User userToSave = userMapper.fromRequest(userRegisterRequest);
        List<Role> userRoles = new ArrayList<>();
        Role subscriberRole = roleRepository.findByName("SUBSCRIBER");
        userRoles.add(subscriberRole);
        userToSave.setRoles(userRoles);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        userToSave.setStatus(Status.ACTIVE);
        userRepository.persistAndFlush(userToSave);
        log.info("IN registerSubscriber user: {} successfully registered", userToSave);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<UserResponse> responses = userRepository.findAll(pageable)
                .map(userMapper::toResponse);
        log.info("IN findAll - {} users found", responses.stream().count());
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserById(UUID userId) {
        UserResponse userResponse = userRepository.findById(userId)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + userId));
        log.info("IN findUserById - user: {} found by id: {}", userResponse, userId);

        return userResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchUserEmailException(ErrorMessage.USER_NOT_EXIST.getMessage() + userEmail));
        log.info("IN findByUserEmail - user: {} found by email: {}", user, userEmail);
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + userId));
        log.info("IN findById - user: {} found by id: {}", user, userId);
        return user;
    }

    @Override
    @Transactional
    public UserResponse updateUserById(UUID userId, UserUpdateRequest userUpdateRequest) {
        checkUniqueEmail(userUpdateRequest.getEmail());
        User userInDB = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + userId));
        userInDB.setFirstName(userUpdateRequest.getFirstName());
        userInDB.setLastName(userUpdateRequest.getLastName());
        userInDB.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        userInDB.setEmail(userUpdateRequest.getEmail());
        User updatedUser = userRepository.persistAndFlush(userInDB);
        UserResponse userResponse = userMapper.toResponse(updatedUser);
        log.info("IN update - user: {} with id: {}", userResponse, userId);
        return userResponse;
    }

    @Override
    @Transactional
    public void activateJournalistAccount(String userToken) {
        UserToken tokenEntity = userTokenService.getByToken(userToken);
        User user = tokenEntity.getUser();
        if (user != null) {
            LocalDateTime expirationAt = tokenEntity.getExpirationAt();
            if (expirationAt.isBefore(LocalDateTime.now())) {
                EmailRequest emailRequest = sendingDataService.getEmailRequest(user, EmailType.USER_TOKEN_EXPIRATION);
                sendingDataService.sendRequestToMailService(emailRequest);
                throw new TokenExpirationException(ErrorMessage.TOKEN_EXPIRED.getMessage());
            } else {
                UUID uuidAdmin = userIdentityService.getUserId();
                user.setStatus(Status.ACTIVE);
                user.setUpdatedBy(uuidAdmin);
                userRepository.persist(user);
                userTokenService.deleteUserTokenByToken(userToken);
                EmailRequest emailRequest = sendingDataService.getEmailRequest(user, EmailType.USER_WELCOME_EMAIL);
                sendingDataService.sendRequestToMailService(emailRequest);
            }
        } else {
            throw new NotFoundException(ErrorMessage.USERTOKEN_NOT_FOUND.getMessage() + userToken);
        }
        log.info("IN activateJournalistAccount - activated user with userToken: {}", userToken);
    }

    @Override
    @Transactional
    public void deactivateUserById(UUID userId) {
        changeUserStatus(userId, Status.NOT_ACTIVE);
        log.info("IN deactivateUser - user with id: {} changed status: NOT_ACTIVE", userId);
    }

    @Override
    @Transactional
    public void deleteUserById(UUID userId) {
        changeUserStatus(userId, Status.DELETED);
        log.info("IN deleteUser - user with id: {} changed status: DELETED", userId);
    }

    private void checkUniqueEmail(String userEmail) {
        if (userRepository.findByEmail(userEmail).isPresent()) {
            throw new UniqueEmailException(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + userEmail);
        }
    }

    private void changeUserStatus(UUID userId, Status userStatus) {
        User userInDB = userRepository.findById(userId)
                .map(user -> {
                    if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
                        throw new InformationChangeStatusUserException(ErrorMessage.CHANGE_STATUS.getMessage());
                    }
                    return user;
                })
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + userId));
        userInDB.setStatus(userStatus);
        UUID uuidAdmin = userIdentityService.getUserId();
        userInDB.setUpdatedBy(uuidAdmin);
        userRepository.persist(userInDB);
    }
}