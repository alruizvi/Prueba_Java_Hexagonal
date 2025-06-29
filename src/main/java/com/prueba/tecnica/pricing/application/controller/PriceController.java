package com.prueba.tecnica.pricing.application.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.tecnica.pricing.application.dto.PriceResponseDto;
import com.prueba.tecnica.pricing.domain.model.PriceResult;
import com.prueba.tecnica.pricing.domain.port.inbound.PriceQueryUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for price queries.
 */
@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Price Management", description = "APIs for querying product prices")
public class PriceController {
    
    private final PriceQueryUseCase priceQueryUseCase;
    
    /**
     * Get applicable price for a product at a specific date and brand.
     * 
     * @param brandId Brand identifier
     * @param productId Product identifier  
     * @param applicationDate Date for price applicability (format: yyyy-MM-dd'T'HH:mm:ss)
     * @return Price information if found
     */
    @Operation(
        summary = "Get applicable price for a product",
        description = "Retrieves the applicable price for a specific product, brand and date. " +
                     "Returns the price information that matches the given criteria and has the highest priority.",
        tags = "Price Management"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Price found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PriceResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{\"message\": \"Validation failed\", \"details\": [\"brandId must be greater than or equal to 0\"]}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No price found for the given criteria",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{\"message\": \"No price found for the given criteria\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{\"message\": \"Internal server error occurred\"}"
                )
            )
        )
    })
    @GetMapping
    public ResponseEntity<PriceResponseDto> getApplicablePrice(
            @Parameter(
                description = "Brand identifier. Must be a non-negative number.",
                required = true,
                example = "1",
                schema = @Schema(type = "integer", format = "int64", minimum = "0")
            )
            @RequestParam @NotNull @Min(0) Long brandId,
            
            @Parameter(
                description = "Product identifier. Must be a non-negative number.",
                required = true,
                example = "35455",
                schema = @Schema(type = "integer", format = "int64", minimum = "0")
            )
            @RequestParam @NotNull @Min(0) Long productId,
            
            @Parameter(
                description = "Date and time for price applicability. Must be in ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss).",
                required = true,
                example = "2020-06-14T10:00:00",
                schema = @Schema(type = "string", format = "date-time", pattern = "yyyy-MM-dd'T'HH:mm:ss")
            )
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {
        
        log.info("Requesting price for brandId: {}, productId: {}, date: {}", brandId, productId, applicationDate);

        return ResponseEntity.ok(mapToResponseDto(priceQueryUseCase.getApplicablePrice(brandId, productId, applicationDate)));
    }
    
    private PriceResponseDto mapToResponseDto(PriceResult priceResult) {
        return PriceResponseDto.builder()
                .productId(priceResult.getProductId())
                .brandId(priceResult.getBrandId())
                .priceList(priceResult.getPriceList())
                .startDate(priceResult.getStartDate())
                .endDate(priceResult.getEndDate())
                .price(priceResult.getPrice())
                .currency(priceResult.getCurrency())
                .build();
    }
}
