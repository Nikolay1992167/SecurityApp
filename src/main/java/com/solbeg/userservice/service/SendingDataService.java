package com.solbeg.userservice.service;

import com.solbeg.userservice.entity.EmailRequest;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.enums.EmailType;

import java.util.Map;

public interface SendingDataService {

    void sendRequestToMailService(EmailRequest emailRequest);

    Map<String, String> getActivationData(final User user, String token);

    Map<String, String> getWelcomeMessageData(User user);

    EmailRequest getEmailRequest(User user, UserToken activationToken);

    EmailRequest getEmailRequest(User user, EmailType emailType);
}