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
import com.solbeg.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public UserRegisterResponse registerJournalist(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UniqueEmailException("Email " + request.getEmail()
                                           + " is occupied! Another user is already registered by this email!");
        }
        User userToSave = userMapper.fromRequest(request);
        List<Role> userRoles = new ArrayList<>();
        Role journalistRole = roleRepository.findByName("JOURNALIST");
        userRoles.add(journalistRole);
        userToSave.setRoles(userRoles);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        User savedUser = userRepository.persist(userToSave);
        UserRegisterResponse userResponse = userMapper.toRegisterResponse(savedUser);
        log.info("IN registerJournalist user: {} successfully registered", userToSave);
        return userResponse;
    }

    @Override
    public UserRegisterResponse registerSubscriber(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UniqueEmailException("Email " + request.getEmail()
                                           + " is occupied! Another user is already registered by this email!");
        }
        User userToSave = userMapper.fromRequest(request);
        List<Role> userRoles = new ArrayList<>();
        Role subscriberRole = roleRepository.findByName("SUBSCRIBER");
        userRoles.add(subscriberRole);
        userToSave.setRoles(userRoles);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        User savedUser = userRepository.persist(userToSave);
        UserRegisterResponse userResponse = userMapper.toRegisterResponse(savedUser);
        log.info("IN registerSubscriber user: {} successfully registered", userToSave);
        return userResponse;
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
    public UserResponse findUserById(UUID uuid) {
        UserResponse userResponse = userRepository.findById(uuid)
                .map(userMapper::toResponse)
                .orElseThrow(() -> NotFoundException.of(User.class, uuid));
        log.info("IN findUserById - user: {} found by id: {}", userResponse, uuid);

        return userResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchUserEmailException("User with email " + email + " is not exist"));
        log.info("IN findByUserEmail - user: {} found by email: {}", user, email);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(User.class, id));
        log.info("IN findById - user: {} found by id: {}", user, id);
        return user;
    }

    @Override
    public UserResponse update(UUID uuid, UserUpdateRequest updateRequest) {
        User userToUpdate = userMapper.fromUpdateRequest(updateRequest);
        User userInDB = userRepository.findById(uuid)
                .orElseThrow(() -> NotFoundException.of(User.class, uuid));
        userToUpdate.setId(uuid);
        userToUpdate.setCreatedAt(userInDB.getCreatedAt());
        userToUpdate.setUpdatedAt(userInDB.getUpdatedAt());
        userToUpdate.setCreatedBy(userInDB.getCreatedBy());
        userToUpdate.setUpdatedBy(userInDB.getUpdatedBy());
        userToUpdate.setStatus(userInDB.getStatus());
        userToUpdate.setRoles(userInDB.getRoles());
        User updatedUser = userRepository.persist(userToUpdate);
        UserResponse userResponse = userMapper.toResponse(updatedUser);
        log.info("IN update - user: {} with id: {}", userResponse, uuid);
        return userResponse;
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

    private void changeUserStatus(UUID id, String token, Status status) {
        User userInDB = userRepository.findById(id)
                .map(user -> {
                    if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
                        throw new InformationChangeStatusUserException("You cannot change the status of a user with the ADMIN role.");
                    }
                    return user;
                })
                .orElseThrow(() -> NotFoundException.of(User.class, id));
        userInDB.setStatus(status);
        UUID uuid = jwtTokenProvider.getIdInFormatUUID(token);
        userInDB.setUpdatedBy(uuid);
        userRepository.persist(userInDB);
    }
}