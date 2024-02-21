package com.solbeg.userservice.exception.handler;

import com.solbeg.userservice.exception.DeleteUserException;
import com.solbeg.userservice.exception.NoSuchUserEmailException;
import com.solbeg.userservice.exception.UniqueEmailException;
import com.solbeg.userservice.exception.model.IncorrectData;
import com.solbeg.userservice.exception.model.ValidationErrorResponse;
import com.solbeg.userservice.exception.model.Violation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UserServiceExceptionHandler {

    @ExceptionHandler(DeleteUserException.class)
    public ResponseEntity<IncorrectData> deleteUserException(DeleteUserException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchUserEmailException.class)
    public ResponseEntity<IncorrectData> noSuchUserEmailException(NoSuchUserEmailException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueEmailException.class)
    public ResponseEntity<IncorrectData> uniqueEmailException(UniqueEmailException exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> constraintValidationException(ConstraintViolationException exception) {
        List<Violation> violations = exception.getConstraintViolations()
                .stream()
                .map(constraintViolation -> new Violation(constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ValidationErrorResponse(HttpStatus.CONFLICT.toString(), violations));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<IncorrectData> handleThrowable(Throwable exception) {
        return getResponse(exception.getClass().getSimpleName(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<IncorrectData> getResponse(String name, String message, HttpStatus status) {
        IncorrectData incorrectData = new IncorrectData(name, message, status.toString());
        return ResponseEntity.status(status).body(incorrectData);
    }
}