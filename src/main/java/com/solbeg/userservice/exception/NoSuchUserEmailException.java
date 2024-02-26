package com.solbeg.userservice.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class NoSuchUserEmailException extends InternalAuthenticationServiceException {

    public NoSuchUserEmailException(String message) {
        super(message);
    }
}
