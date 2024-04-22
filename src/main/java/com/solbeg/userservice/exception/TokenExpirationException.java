package com.solbeg.userservice.exception;

public class TokenExpirationException extends RuntimeException{

    public TokenExpirationException(String message) {
        super(message);
    }
}
