package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;
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
import com.solbeg.userservice.service.SendingDataService;
import com.solbeg.userservice.service.UserIdentityService;
import com.solbeg.userservice.service.UserService;
import com.solbeg.userservice.service.UserTokenService;
import com.solbeg.userservice.util.AuthUtil;
import com.solbeg.userservice.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.solbeg.userservice.util.Constants.ROLE_JOURNALIST;
import static com.solbeg.userservice.util.Constants.ROLE_SUBSCRIBER;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserIdentityService userIdentityService;
    private final UserTokenService userTokenService;
    private final SendingDataService sendingDataService;

//    @Override
//    @Transactional(readOnly = true)
//    public UserResponse getUser() {
//        UUID userId = AuthUtil.getUserId();
//        UserResponse userResponse = userRepository.findById(userId).map(userMapper::toResponse)
//                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + userId));
//        log.info("IN findUserByToken - user: {} found by id: {}", userResponse, userId);
//        return userResponse;
//    }

    @Override
    @Transactional
    public void registerJournalist(UserRegisterRequest userRegisterRequest) {
        userValidator.checkUniqueEmail(userRegisterRequest.getEmail());

        User user = userMapper.fromRequest(userRegisterRequest, Status.NOT_ACTIVE);
        user.setRoles(List.of(roleRepository.findByName(ROLE_JOURNALIST)));

        user = userRepository.persistAndFlush(user);

        sendingDataService.sendRequestForActivationUser(user);
    }

    @Override
    public void registerSubscriber(UserRegisterRequest userRegisterRequest) {
        userValidator.checkUniqueEmail(userRegisterRequest.getEmail());

        User userToSave = userMapper.fromRequest(userRegisterRequest, Status.ACTIVE);
        userToSave.setRoles(List.of(roleRepository.findByName(ROLE_SUBSCRIBER)));

        userRepository.persistAndFlush(userToSave);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserById(UUID userId) {
        return userMapper.toResponse(userIdentityService.getUserOrThrowException(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public User findActiveUserByEmailOrThrowException(String userEmail) {
        return userRepository.findByEmailAndStatus(userEmail, Status.ACTIVE)
                .orElseThrow(() -> new NoSuchUserEmailException(ErrorMessage.USER_NOT_EXIST.getMessage() + userEmail));
    }


    @Override
    @Transactional
    public UserResponse updateUserById(UUID userId, UserUpdateRequest userUpdateRequest) {
        userValidator.checkUniqueEmail(userUpdateRequest.getEmail(), userId);

        User user = userIdentityService.getUserOrThrowException(userId);

        userMapper.update(user, userUpdateRequest, userId);

        user = userRepository.persistAndFlush(user);

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void activateJournalistAccount(String userToken) {
        UserToken tokenEntity = userTokenService.getByToken(userToken);
        User user = tokenEntity.getUser();

        if (tokenEntity.getExpirationAt().isBefore(LocalDateTime.now())) {
            sendingDataService.sendInformation(user, EmailType.USER_TOKEN_EXPIRATION);
            throw new TokenExpirationException(ErrorMessage.TOKEN_EXPIRED.getMessage());
        }

        user.setStatus(Status.ACTIVE);
        user.setUpdatedBy(AuthUtil.getUserId());

        userRepository.persist(user);

        userTokenService.deleteUserTokenByToken(userToken);
        sendingDataService.sendInformation(user, EmailType.USER_WELCOME_EMAIL);
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
        UUID uuidAdmin = AuthUtil.getUserId();
        userInDB.setUpdatedBy(uuidAdmin);
        userRepository.persist(userInDB);
    }
}