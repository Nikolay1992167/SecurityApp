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
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.service.SendingDataService;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public void registerJournalist(UserRegisterRequest request) {
        checkUniqueEmail(request.getEmail());
        User userToSave = userMapper.fromRequest(request);
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
    public void registerSubscriber(UserRegisterRequest request) {
        checkUniqueEmail(request.getEmail());
        User userToSave = userMapper.fromRequest(request);
        List<Role> userRoles = new ArrayList<>();
        Role subscriberRole = roleRepository.findByName("SUBSCRIBER");
        userRoles.add(subscriberRole);
        userToSave.setRoles(userRoles);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        userToSave.setStatus(Status.ACTIVE);
        userRepository.persist(userToSave);
        log.info("IN registerSubscriber user: {} successfully registered", userToSave);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<UserResponse> responses = userRepository.findAll(pageable)
                .map(userMapper::toResponse);
        log.info("IN findAll - {} users found", responses.stream().count());
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserById(UUID uuid) {
        UserResponse userResponse = userRepository.findById(uuid)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + uuid));
        log.info("IN findUserById - user: {} found by id: {}", userResponse, uuid);

        return userResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchUserEmailException(ErrorMessage.USER_NOT_EXIST.getMessage() + email));
        log.info("IN findByUserEmail - user: {} found by email: {}", user, email);
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + id));
        log.info("IN findById - user: {} found by id: {}", user, id);
        return user;
    }

    @Override
    @Transactional
    public UserResponse update(UUID uuid, UserUpdateRequest updateRequest) {
        checkUniqueEmail(updateRequest.getEmail());
        User userInDB = userRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + uuid));
        userInDB.setFirstName(updateRequest.getFirstName());
        userInDB.setLastName(updateRequest.getLastName());
        userInDB.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        userInDB.setEmail(updateRequest.getEmail());
        User updatedUser = userRepository.persist(userInDB);
        UserResponse userResponse = userMapper.toResponse(updatedUser);
        log.info("IN update - user: {} with id: {}", userResponse, uuid);
        return userResponse;
    }

    @Override
    @Transactional
    public void activateJournalistAccount(String userToken, String tokenAdmin) {
        userRepository.findByUserWhenRegistrationWithToken(userToken).ifPresentOrElse(
                user -> {
                    LocalDateTime expirationAt = userTokenService.getByToken(userToken).getExpirationAt();
                    if (expirationAt.isBefore(LocalDateTime.now())) {
                        EmailRequest emailRequest = sendingDataService.getEmailRequest(user, EmailType.USER_TOKEN_EXPIRATION);
                        sendingDataService.sendRequestToMailService(emailRequest);
                        throw new TokenExpirationException(ErrorMessage.TOKEN_EXPIRED.getMessage());
                    } else {
                        UUID uuidAdmin = jwtTokenProvider.getIdInFormatUUID(tokenAdmin);
                        user.setStatus(Status.ACTIVE);
                        user.setUpdatedBy(uuidAdmin);
                        userRepository.persist(user);
                        userTokenService.deleteUserToken(userToken);
                        EmailRequest emailRequest = sendingDataService.getEmailRequest(user, EmailType.USER_WELCOME_EMAIL);
                        sendingDataService.sendRequestToMailService(emailRequest);
                    }
                },
                () -> {
                    throw new NotFoundException(ErrorMessage.USERTOKEN_NOT_FOUND.getMessage() + userToken);
                });
        log.info("IN activateJournalistAccount - activated user with userToken: {}", userToken);
    }

    @Override
    @Transactional
    public void deactivateUser(UUID id, String token) {
        changeUserStatus(id, token, Status.NOT_ACTIVE);
        log.info("IN deactivateUser - user with id: {} changed status: NOT_ACTIVE", id);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id, String token) {
        changeUserStatus(id, token, Status.DELETED);
        log.info("IN deleteUser - user with id: {} changed status: DELETED", id);
    }


    private void checkUniqueEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UniqueEmailException(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + email);
        }
    }

    private void changeUserStatus(UUID id, String tokenAdmin, Status status) {
        User userInDB = userRepository.findById(id)
                .map(user -> {
                    if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
                        throw new InformationChangeStatusUserException(ErrorMessage.CHANGE_STATUS.getMessage());
                    }
                    return user;
                })
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + id));
        userInDB.setStatus(status);
        UUID uuidAdmin = jwtTokenProvider.getIdInFormatUUID(tokenAdmin);
        userInDB.setUpdatedBy(uuidAdmin);
        userRepository.persist(userInDB);
    }
}