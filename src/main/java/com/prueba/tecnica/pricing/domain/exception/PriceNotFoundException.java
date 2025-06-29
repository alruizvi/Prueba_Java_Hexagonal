package com.prueba.tecnica.pricing.domain.exception;

public class PriceNotFoundException extends DomainException{
    public PriceNotFoundException(String message) {
        super(message);
    }

    public PriceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
