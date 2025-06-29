package com.prueba.tecnica.pricing.application.controller;

import com.prueba.tecnica.pricing.domain.exception.PriceNotFoundException;
import com.prueba.tecnica.pricing.domain.model.PriceResult;
import com.prueba.tecnica.pricing.domain.port.inbound.PriceQueryUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * Unit tests for PriceController using MockMvc.
 * These tests focus on the controller layer behavior and response structure.
 */
@WebMvcTest(controllers = PriceController.class)
@ActiveProfiles("test")
@DisplayName("Price Controller - Unit Tests")
class PriceControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriceQueryUseCase priceQueryUseCase;

    private static final Long BRAND_ID = 1L;
    private static final Long PRODUCT_ID = 35455L;

    @Test
    @DisplayName("Should return price successfully for valid request")
    void shouldReturnPriceSuccessfully() throws Exception {
        // Given
        PriceResult mockPriceResult = createMockPriceResult(1L, new BigDecimal("35.50"));
        
        when(priceQueryUseCase.getApplicablePrice(eq(BRAND_ID), eq(PRODUCT_ID), any(LocalDateTime.class)))
                .thenReturn(mockPriceResult);

        // When & Then
        mockMvc.perform(get("/api/v1/prices")
                        .param("brandId", BRAND_ID.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(BRAND_ID))
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"));
    }

    @Test
    @DisplayName("Should return 404 when price not found")
    void shouldReturn404WhenPriceNotFound() throws Exception {
        // Given
        when(priceQueryUseCase.getApplicablePrice(eq(BRAND_ID), eq(PRODUCT_ID), any(LocalDateTime.class)))
                .thenThrow(new PriceNotFoundException("Price not found"));

        // When & Then
        mockMvc.perform(get("/api/v1/prices")
                        .param("brandId", BRAND_ID.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("Should return 400 for missing brandId parameter")
    void shouldReturn400ForMissingBrandId() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/prices")
                        .param("productId", PRODUCT_ID.toString())
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for missing productId parameter")
    void shouldReturn400ForMissingProductId() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/prices")
                        .param("brandId", BRAND_ID.toString())
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for missing applicationDate parameter")
    void shouldReturn400ForMissingApplicationDate() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/prices")
                        .param("brandId", BRAND_ID.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for invalid date format")
    void shouldReturn400ForInvalidDateFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/prices")
                        .param("brandId", BRAND_ID.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .param("applicationDate", "invalid-date")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return promotional price for Test 2 scenario")
    void shouldReturnPromotionalPriceForTest2() throws Exception {
        // Given - Test 2: 16:00 on day 14 should return promotional price
        PriceResult mockPriceResult = new PriceResult(
                PRODUCT_ID,
                BRAND_ID,
                2L,
                LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30, 0),
                new BigDecimal("25.45"),
                "EUR"
        );
        
        when(priceQueryUseCase.getApplicablePrice(eq(BRAND_ID), eq(PRODUCT_ID), any(LocalDateTime.class)))
                .thenReturn(mockPriceResult);

        // When & Then
        mockMvc.perform(get("/api/v1/prices")
                        .param("brandId", BRAND_ID.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00"));
    }

    private PriceResult createMockPriceResult(Long priceList, BigDecimal price) {
        return new PriceResult(
                PRODUCT_ID,
                BRAND_ID,
                priceList,
                LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                price,
                "EUR"
        );
    }
}
