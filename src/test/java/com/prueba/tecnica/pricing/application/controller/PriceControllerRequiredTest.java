package com.prueba.tecnica.pricing.application.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.prueba.tecnica.pricing.application.dto.PriceResponseDto;

/**
 * Integration tests for PriceController validating the required test cases.
 * These tests validate the 5 specific scenarios mentioned in the requirements.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Price Controller - Required Test Cases")
class PriceControllerRequiredTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final Long BRAND_ID = 1L;
    private static final Long PRODUCT_ID = 35455L;

    @Test
    @DisplayName("Test 1: Request at 10:00 on day 14 for product 35455 and brand 1 (ZARA)")
    void test1_requestAt10AmOnDay14() {
        // Given
        String applicationDate = "2020-06-14T10:00:00";
        String url = buildUrl(BRAND_ID, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<PriceResponseDto> response = restTemplate.getForEntity(url, PriceResponseDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        PriceResponseDto priceResponse = response.getBody();
        assertThat(priceResponse.getBrandId()).isEqualTo(BRAND_ID);
        assertThat(priceResponse.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(priceResponse.getPriceList()).isEqualTo(1L);
        assertThat(priceResponse.getPrice()).isEqualTo(new BigDecimal("35.50"));
        assertThat(priceResponse.getCurrency()).isEqualTo("EUR");
        assertThat(priceResponse.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0, 0));
        assertThat(priceResponse.getEndDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    }

    @Test
    @DisplayName("Test 2: Request at 16:00 on day 14 for product 35455 and brand 1 (ZARA)")
    void test2_requestAt4PmOnDay14() {
        // Given
        String applicationDate = "2020-06-14T16:00:00";
        String url = buildUrl(BRAND_ID, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<PriceResponseDto> response = restTemplate.getForEntity(url, PriceResponseDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        PriceResponseDto priceResponse = response.getBody();
        assertThat(priceResponse.getBrandId()).isEqualTo(BRAND_ID);
        assertThat(priceResponse.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(priceResponse.getPriceList()).isEqualTo(2L);
        assertThat(priceResponse.getPrice()).isEqualTo(new BigDecimal("25.45"));
        assertThat(priceResponse.getCurrency()).isEqualTo("EUR");
        assertThat(priceResponse.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 15, 0, 0));
        assertThat(priceResponse.getEndDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 18, 30, 0));
    }

    @Test
    @DisplayName("Test 3: Request at 21:00 on day 14 for product 35455 and brand 1 (ZARA)")
    void test3_requestAt9PmOnDay14() {
        // Given
        String applicationDate = "2020-06-14T21:00:00";
        String url = buildUrl(BRAND_ID, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<PriceResponseDto> response = restTemplate.getForEntity(url, PriceResponseDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        PriceResponseDto priceResponse = response.getBody();
        assertThat(priceResponse.getBrandId()).isEqualTo(BRAND_ID);
        assertThat(priceResponse.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(priceResponse.getPriceList()).isEqualTo(1L);
        assertThat(priceResponse.getPrice()).isEqualTo(new BigDecimal("35.50"));
        assertThat(priceResponse.getCurrency()).isEqualTo("EUR");
        assertThat(priceResponse.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0, 0));
        assertThat(priceResponse.getEndDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    }

    @Test
    @DisplayName("Test 4: Request at 10:00 on day 15 for product 35455 and brand 1 (ZARA)")
    void test4_requestAt10AmOnDay15() {
        // Given
        String applicationDate = "2020-06-15T10:00:00";
        String url = buildUrl(BRAND_ID, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<PriceResponseDto> response = restTemplate.getForEntity(url, PriceResponseDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        PriceResponseDto priceResponse = response.getBody();
        assertThat(priceResponse.getBrandId()).isEqualTo(BRAND_ID);
        assertThat(priceResponse.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(priceResponse.getPriceList()).isEqualTo(3L);
        assertThat(priceResponse.getPrice()).isEqualTo(new BigDecimal("30.50"));
        assertThat(priceResponse.getCurrency()).isEqualTo("EUR");
        assertThat(priceResponse.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 15, 0, 0, 0));
        assertThat(priceResponse.getEndDate()).isEqualTo(LocalDateTime.of(2020, 6, 15, 11, 0, 0));
    }

    @Test
    @DisplayName("Test 5: Request at 21:00 on day 16 for product 35455 and brand 1 (ZARA)")
    void test5_requestAt9PmOnDay16() {
        // Given
        String applicationDate = "2020-06-16T21:00:00";
        String url = buildUrl(BRAND_ID, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<PriceResponseDto> response = restTemplate.getForEntity(url, PriceResponseDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        PriceResponseDto priceResponse = response.getBody();
        assertThat(priceResponse.getBrandId()).isEqualTo(BRAND_ID);
        assertThat(priceResponse.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(priceResponse.getPriceList()).isEqualTo(4L);
        assertThat(priceResponse.getPrice()).isEqualTo(new BigDecimal("38.95"));
        assertThat(priceResponse.getCurrency()).isEqualTo("EUR");
        assertThat(priceResponse.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 15, 16, 0, 0));
        assertThat(priceResponse.getEndDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    }

    private String buildUrl(Long brandId, Long productId, String applicationDate) {
        return String.format("http://localhost:%d/api/v1/prices?brandId=%d&productId=%d&applicationDate=%s",
                port, brandId, productId, applicationDate);
    }
}
