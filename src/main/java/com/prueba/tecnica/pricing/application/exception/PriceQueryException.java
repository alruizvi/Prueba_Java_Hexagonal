package com.prueba.tecnica.pricing.application.exception;

public class PriceQueryException extends ApplicationException {
    public PriceQueryException(String message) {
        super(message);
    }
    public PriceQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
