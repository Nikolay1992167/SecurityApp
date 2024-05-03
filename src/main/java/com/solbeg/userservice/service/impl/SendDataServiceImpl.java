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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

import static com.solbeg.userservice.util.Constants.ACTIVATION_URL;
import static com.solbeg.userservice.util.Constants.FIRST_NAME;
import static com.solbeg.userservice.util.Constants.LAST_NAME;
import static com.solbeg.userservice.util.Constants.NAME_LINK;
import static com.solbeg.userservice.util.Constants.URL_EMAIL_SERVICE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendDataServiceImpl implements SendingDataService {
    private final RestClient restClient;

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
        switch (emailType) {
            case USER_TOKEN_EXPIRATION -> emailRequest = getEmailRequest(user, EmailType.USER_TOKEN_EXPIRATION);
            case USER_WELCOME_EMAIL -> emailRequest = getEmailRequest(user, EmailType.USER_WELCOME_EMAIL);
        }
        sendData(emailRequest);
    }

    private void sendData(EmailRequest emailRequest) {
        try {
            restClient.post()
                    .uri(URL_EMAIL_SERVICE)
                    .contentType(APPLICATION_JSON)
                    .body(emailRequest)
                    .retrieve()
                    .body(EmailRequest.class);
            log.info("User data successfully sent to mail-service.");
        } catch (RestClientException exception) {
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
        Map<String, String> data = new HashMap<>();
        data.put(FIRST_NAME, user.getFirstName());
        data.put(LAST_NAME, user.getLastName());
        data.put(NAME_LINK, ACTIVATION_URL + token);
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