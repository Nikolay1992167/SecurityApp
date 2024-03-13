package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.EmailRequest;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.entity.User_;
import com.solbeg.userservice.enums.EmailType;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.SendDataException;
import com.solbeg.userservice.service.SendingDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendDataServiceImpl implements SendingDataService {

    private final WebClient webClient;

    public void sendRequestToMailService(EmailRequest emailRequest) {

        try {
            webClient.post()
                    .uri("/api/v1/send/email")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(emailRequest), EmailRequest.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("User data successfully sent to mail-service.");
        } catch (WebClientResponseException exception) {
            throw new SendDataException(ErrorMessage.ERROR_SEND_DATA.getMessage() + exception.getMessage());
        }
    }

    @Override
    public Map<String, String> getActivationData(User user, String token) {
        String baseUrl = "http://localhost:8081/api/v1/users/activation?userToken=";
        Map<String, String> data = new HashMap<>();
        data.put(User_.FIRST_NAME, user.getFirstName());
        data.put(User_.LAST_NAME, user.getLastName());
        data.put("activationLink", baseUrl + token);
        return data;
    }

    @Override
    public Map<String, String> getWelcomeMessageData(User user) {
        Map<String, String> data = new HashMap<>();
        data.put(User_.FIRST_NAME, user.getFirstName());
        data.put(User_.LAST_NAME, user.getLastName());
        return data;
    }

    @Override
    public EmailRequest getEmailRequest(User user, UserToken activationToken) {
        return EmailRequest.builder()
                .emailType(EmailType.USER_ACTIVATE)
                .toEmail(user.getEmail())
                .data(getActivationData(user, activationToken.getToken()))
                .build();
    }

    @Override
    public EmailRequest getEmailRequest(User user, EmailType emailType) {
        return EmailRequest.builder()
                .emailType(emailType)
                .toEmail(user.getEmail())
                .data(getWelcomeMessageData(user))
                .build();
    }
}