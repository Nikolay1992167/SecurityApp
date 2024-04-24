package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.EmailRequest;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;

import com.solbeg.userservice.enums.EmailType;
import com.solbeg.userservice.exception.SendDataException;
import com.solbeg.userservice.service.SendingDataService;
import com.solbeg.userservice.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.solbeg.userservice.util.Constants.FIRST_NAME;
import static com.solbeg.userservice.util.Constants.LAST_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendDataServiceImpl implements SendingDataService {

    private final WebClient webClient;
    private final UserTokenService userTokenService;

    @Override
    public void sendRequestForActivationUser(User user) {
        UserToken activationToken = userTokenService.createActivationToken(user.getId());
        EmailRequest emailRequest = getEmailRequest(user, activationToken);
        sendData(emailRequest);
    }

    @Override
    public void sendInformation(User user, EmailType emailType) {
        EmailRequest emailRequest = null;
        switch (emailType){
            case USER_TOKEN_EXPIRATION -> emailRequest = getEmailRequest(user, EmailType.USER_TOKEN_EXPIRATION);
            case USER_WELCOME_EMAIL -> emailRequest = getEmailRequest(user, EmailType.USER_WELCOME_EMAIL);
        }
        sendData(emailRequest);
    }

    private void sendData(EmailRequest emailRequest) {
        try {
            webClient.post()
                    .uri("/api/v1/send/email")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(emailRequest), EmailRequest.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("User data successfully sent to mail-service.");
        } catch (WebClientException exception) {
            log.error("Failed to send user data to mail-service!");
            throw new SendDataException(exception.getMessage());
        }
    }


    private EmailRequest getEmailRequest(User user, UserToken activationToken) {
        return EmailRequest.builder()
                .emailType(EmailType.USER_ACTIVATE)
                .toEmail(user.getEmail())
                .data(getActivationData(user, activationToken.getToken()))
                .build();
    }

    private Map<String, String> getActivationData(User user, String token) {
        String baseUrl = "http://localhost:8081/api/v1/admin/activation?userToken=";
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME, user.getFirstName());
        data.put(LAST_NAME, user.getLastName());
        data.put("activationLink", baseUrl + token);
        return data;
    }

    private EmailRequest getEmailRequest(User user, EmailType emailType) {
        return EmailRequest.builder()
                .emailType(emailType)
                .toEmail(user.getEmail())
                .data(getWelcomeMessageData(user))
                .build();
    }

    private Map<String, String> getWelcomeMessageData(User user) {
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME, user.getFirstName());
        data.put(LAST_NAME, user.getLastName());
        return data;
    }
}