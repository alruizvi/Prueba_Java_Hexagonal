package com.prueba.tecnica.pricing.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.prueba.tecnica.pricing.domain.model.Price;
import com.prueba.tecnica.pricing.domain.port.outbound.PriceRepositoryPort;
import com.prueba.tecnica.pricing.infrastructure.persistence.repository.JpaPriceRepository;

import lombok.RequiredArgsConstructor;

/**
 * Adapter implementing the price repository port using JPA.
 */
@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {
    
    private final JpaPriceRepository jpaPriceRepository;
    
    @Override
    public Optional<Price> findApplicablePrices(Long brandId, Long productId, LocalDateTime applicationDate) {
    return jpaPriceRepository
        .findBestApplicablePrice(brandId, productId, applicationDate)
            .map(entity -> new Price(
            entity.getBrandId(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getPriceList(),
            entity.getProductId(),
            entity.getPriority(),
            entity.getPrice(),
            entity.getCurrency()
        ));
    }
}
