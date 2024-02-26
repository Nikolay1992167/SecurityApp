package com.solbeg.userservice.exception.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record IncorrectData(String exception,
                            String errorMessage,
                            String errorCode) {
}
