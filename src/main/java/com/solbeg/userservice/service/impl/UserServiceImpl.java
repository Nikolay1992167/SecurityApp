package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.UserAuthenticationRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.dto.response.DeleteResponse;
import com.solbeg.userservice.dto.response.TokenValidationResponse;
import com.solbeg.userservice.dto.response.UserResponse;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.Role;
import com.solbeg.userservice.exception.DeleteUserException;
import com.solbeg.userservice.exception.NoSuchUserEmailException;
import com.solbeg.userservice.exception.UniqueEmailException;
import com.solbeg.userservice.mapper.UserMapper;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.service.JwtService;
import com.solbeg.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    @Transactional
    public UserResponse registerJournalist(UserRegisterRequest request) {
        return registerUser(request, Role.JOURNALIST);
    }

    @Override
    @Transactional
    public UserResponse registerSubscriber(UserRegisterRequest request) {
        return registerUser(request, Role.SUBSCRIBER);
    }

    @Override
    public UserResponse authenticate(UserAuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchUserEmailException("User with email " + request.getEmail() + " is not exist"));
        String jwtToken = jwtService.generateToken(user);
        return userMapper.toResponse(user, jwtToken, jwtService.extractExpiration(jwtToken).toString());
    }

    @Override
    public TokenValidationResponse tokenValidationCheck(String token) {
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);
        String roles = jwtService.extractClaim(jwt, claims -> claims.get("roles")).toString();
        String role = roles.lines()
                .map(s -> s.substring(s.indexOf("=") + 1, s.length() - 2))
                .findFirst()
                .orElse("");
        return TokenValidationResponse.builder()
                .role(role)
                .email(email)
                .build();
    }

    @Override
    @Transactional
    public UserResponse updateByToken(UserUpdateRequest request, String token) {
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new NoSuchUserEmailException("There is no user with email " + email + " to update"));
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User updatedUser = repository.persist(user);
        return userMapper.toResponse(updatedUser, jwt, jwtService.extractExpiration(jwt).toString());
    }

    @Override
    @Transactional
    public DeleteResponse deleteByToken(String token) {
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);
        repository.findByEmail(email)
                .ifPresentOrElse(
                        user -> {
                            if (user.getRole().equals(Role.ADMIN)) {
                                throw new DeleteUserException("You cannot delete a user with the ADMIN role!");
                            }
                            repository.delete(user);
                        },
                        () -> {
                            throw new NoSuchUserEmailException("There is no user with email " + email + " to delete");
                        }
                );
        return DeleteResponse.builder()
                .message("User with email " + email + " was successfully deleted")
                .build();
    }

    private UserResponse registerUser(UserRegisterRequest request, Role role) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new UniqueEmailException("Email " + request.getEmail()
                                           + " is occupied! Another user is already registered by this email!");
        }
        User userToSave = userMapper.fromRequest(request);
        userToSave.setRole(role);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        repository.persist(userToSave);
        String jwtToken = jwtService.generateToken(userToSave);
        return userMapper.toResponse(userToSave, jwtToken, jwtService.extractExpiration(jwtToken).toString());
    }
}