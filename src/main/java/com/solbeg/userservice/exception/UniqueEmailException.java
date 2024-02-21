package com.solbeg.userservice.exception;

public class UniqueEmailException extends RuntimeException{

    public UniqueEmailException(String message) {
        super(message);
    }
}