package com.prueba.tecnica.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for the Pricing Service.
 * 
 * This application implements a pricing service for an e-commerce system
 * using Hexagonal Architecture with Spring Boot.
 */
@SpringBootApplication
@EnableCaching
public class PricingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingApplication.class, args);
    }
}