package com.trelloapp.dto;

import lombok.Value;

@Value
public class ErrorResponse {
    private final String code;
    private final String message;
}
