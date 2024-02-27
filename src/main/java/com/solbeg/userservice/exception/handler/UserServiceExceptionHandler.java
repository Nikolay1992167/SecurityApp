package com.solbeg.userservice.exception.handler;

import com.solbeg.userservice.exception.AccessDeniedException;
import com.solbeg.userservice.exception.InformationChangeStatusUserException;
import com.solbeg.userservice.exception.JwtParsingException;
import com.solbeg.userservice.exception.NoSuchUserEmailException;
import com.solbeg.userservice.exception.NotFoundException;
import com.solbeg.userservice.exception.UniqueEmailException;
import com.solbeg.userservice.exception.model.IncorrectData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UserServiceExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<IncorrectData> accessDeniedException(AccessDeniedException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InformationChangeStatusUserException.class)
    public ResponseEntity<IncorrectData> informationChangeStatusUserException(InformationChangeStatusUserException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JwtParsingException.class)
    public ResponseEntity<IncorrectData> jwtParsingException(JwtParsingException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<IncorrectData> usernameNotFoundException(UsernameNotFoundException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchUserEmailException.class)
    public ResponseEntity<IncorrectData> noSuchUserEmailException(NoSuchUserEmailException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<IncorrectData> httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return getResponse(exception.getClass().getSimpleName(), "Specify the correct status!", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<IncorrectData> notFoundException(NotFoundException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueEmailException.class)
    public ResponseEntity<IncorrectData> uniqueEmailException(UniqueEmailException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IncorrectData> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return getResponse(exception.getClass().getSimpleName(), errors.toString(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<IncorrectData> handleSignatureException(SignatureException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<IncorrectData> getResponse(String name, String message, HttpStatus status) {
        IncorrectData incorrectData = new IncorrectData(name, message, status.toString());
        return ResponseEntity.status(status).body(incorrectData);
    }
}