package com.prueba.tecnica.pricing.domain.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.prueba.tecnica.pricing.domain.exception.PriceNotFoundException;
import com.prueba.tecnica.pricing.domain.model.Price;
import com.prueba.tecnica.pricing.domain.model.PriceResult;
import com.prueba.tecnica.pricing.domain.port.inbound.PriceQueryUseCase;
import com.prueba.tecnica.pricing.domain.port.outbound.PriceRepositoryPort;

/**
 * Domain service implementing the price query use case.
 * Contains the business logic for determining the applicable price.
 */
public class PriceQueryService implements PriceQueryUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    public PriceQueryService(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    @Override
    public PriceResult getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        Price applicablePrice = priceRepositoryPort.findApplicablePrices(brandId, productId, applicationDate)
                .orElseThrow(() -> new PriceNotFoundException("No applicable price found"));
        return mapToPriceResult(applicablePrice);
    }

    private PriceResult mapToPriceResult(Price price) {
        return new PriceResult(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPrice(),
                price.getCurrency());
    }
}
