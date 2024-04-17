package com.solbeg.userservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.JwtParsingException;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.repository.UserRepository;
import com.solbeg.userservice.service.UserIdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserIdentityServiceImpl implements UserIdentityService {
    private final UserRepository userRepository;

    @Override
    public UUID getIdInFormatUUID(String token) {
        String[] parts = token.split("\\.");
        String payload = parts[1];
        String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> payloadMap;
        try {
            payloadMap = objectMapper.readValue(decodedPayload, Map.class);
        } catch (JsonProcessingException e) {
            throw new JwtParsingException(ErrorMessage.ERROR_PARSING.getMessage(), e);
        }
        return UUID.fromString((String) payloadMap.get("id"));
    }

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + userId));
    }
}