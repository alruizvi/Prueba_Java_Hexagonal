package com.prueba.tecnica.pricing.domain.port.outbound;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.prueba.tecnica.pricing.domain.model.Price;

/**
 * Outbound port for price repository operations.
 * This interface defines what the domain needs from the infrastructure layer.
 */
public interface PriceRepositoryPort {
    
    /**
     * Find prices that are applicable for the given criteria.
     * 
     * @param brandId the brand identifier
     * @param productId the product identifier
     * @param applicationDate the date to check price applicability
     * @return list of applicable prices
     */
    Optional<Price> findApplicablePrices(Long brandId, Long productId, LocalDateTime applicationDate);
}
