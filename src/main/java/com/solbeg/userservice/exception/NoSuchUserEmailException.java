package com.solbeg.userservice.exception;

public class NoSuchUserEmailException extends RuntimeException{

    public NoSuchUserEmailException(String message) {
        super(message);
    }
}
