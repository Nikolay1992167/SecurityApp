package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.EmailRequest;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.entity.User_;
import com.solbeg.userservice.enums.EmailType;
import com.solbeg.userservice.exception.SendDataException;
import com.solbeg.userservice.service.SendingDataService;
import com.solbeg.userservice.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.solbeg.userservice.util.Constants.ACTIVATION_URL;
import static com.solbeg.userservice.util.Constants.NAME_LINK;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendDataServiceImpl implements SendingDataService {

    private final RabbitTemplate rabbitTemplate;
    private final UserTokenService userTokenService;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Override
    public void sendRequestForActivationUser(User user) {
        UserToken activationToken = userTokenService.createActivationToken(user.getId());

        EmailRequest emailRequest = getEmailRequest(user, activationToken);

        sendMessage("activation", emailRequest);
    }

    @Override
    public void sendInformation(User user, EmailType emailType) {
        EmailRequest emailRequest = null;
        switch (emailType) {
            case USER_TOKEN_EXPIRATION -> emailRequest = getEmailRequest(user, EmailType.USER_TOKEN_EXPIRATION);
            case USER_WELCOME_EMAIL -> emailRequest = getEmailRequest(user, EmailType.USER_WELCOME_EMAIL);
        }
        sendMessage("information", Objects.requireNonNull(emailRequest));
    }

    private void sendMessage(String nameQueue, EmailRequest emailRequest) {
        try {
            rabbitTemplate.convertAndSend(exchange, nameQueue, emailRequest);
            log.info("Successfully sent message!");
        } catch (AmqpException exception) {
            log.error("Failed to send message!");
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
        data.put(User_.FIRST_NAME, user.getFirstName());
        data.put(User_.LAST_NAME, user.getLastName());
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
        data.put(User_.FIRST_NAME, user.getFirstName());
        data.put(User_.LAST_NAME, user.getLastName());
        return data;
    }
}