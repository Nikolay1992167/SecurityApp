package com.solbeg.userservice.service;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.enums.EmailType;

public interface SendingDataService {
    void sendRequestForActivationUser(User user);

    void sendInformation(User user, EmailType emailType);
}