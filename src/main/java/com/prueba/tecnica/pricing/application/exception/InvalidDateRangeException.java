package com.prueba.tecnica.pricing.application.exception;

public class InvalidDateRangeException extends ApplicationException {
    public InvalidDateRangeException(String message) {
        super(message);
    }
    public InvalidDateRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
