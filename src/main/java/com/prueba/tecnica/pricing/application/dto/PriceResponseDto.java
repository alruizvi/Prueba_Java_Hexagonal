package com.prueba.tecnica.pricing.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for price query response.
 */
@Value
@Builder
public class PriceResponseDto {
    Long productId;
    Long brandId;
    Long priceList;
    LocalDateTime startDate;
    LocalDateTime endDate;
    BigDecimal price;
    String currency;
}
