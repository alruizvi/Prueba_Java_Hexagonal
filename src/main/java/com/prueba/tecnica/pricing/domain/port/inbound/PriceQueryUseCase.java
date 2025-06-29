package com.prueba.tecnica.pricing.domain.port.inbound;

import java.time.LocalDateTime;
import java.util.Optional;

import com.prueba.tecnica.pricing.domain.model.PriceResult;

/**
 * Inbound port for price query use cases.
 * This interface defines the operations available to the application layer.
 */
public interface PriceQueryUseCase {
    
    /**
     * Get the applicable price for a product at a specific date and brand.
     * 
     * @param brandId the brand identifier
     * @param productId the product identifier
     * @param applicationDate the date to check price applicability
     * @return the applicable price result if found
     */
    PriceResult getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate);
}
