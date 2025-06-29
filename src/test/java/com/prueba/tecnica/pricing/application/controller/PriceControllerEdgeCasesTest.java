package com.prueba.tecnica.pricing.application.controller;

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

import com.prueba.tecnica.pricing.application.dto.ApiErrorResponse;
import com.prueba.tecnica.pricing.application.dto.PriceResponseDto;

/**
 * Additional test cases for PriceController covering edge cases and error scenarios.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Price Controller - Edge Cases and Error Handling")
class PriceControllerEdgeCasesTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final Long BRAND_ID = 1L;
    private static final Long PRODUCT_ID = 35455L;

    @Test
    @DisplayName("Should return 404 when no price found for non-existent product")
    void shouldReturn404ForNonExistentProduct() {
        // Given
        Long nonExistentProductId = 99999L;
        String applicationDate = "2020-06-14T10:00:00";
        String url = buildUrl(BRAND_ID, nonExistentProductId, applicationDate);

        // When
        ResponseEntity<ApiErrorResponse> response = restTemplate.getForEntity(url, ApiErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        ApiErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Should return 404 when no price found for non-existent brand")
    void shouldReturn404ForNonExistentBrand() {
        // Given
        Long nonExistentBrandId = 99L;
        String applicationDate = "2020-06-14T10:00:00";
        String url = buildUrl(nonExistentBrandId, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<ApiErrorResponse> response = restTemplate.getForEntity(url, ApiErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        ApiErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Should return 404 when no price found for date outside range")
    void shouldReturn404ForDateOutsideRange() {
        // Given - Date before any price range
        String applicationDate = "2020-01-01T10:00:00";
        String url = buildUrl(BRAND_ID, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<ApiErrorResponse> response = restTemplate.getForEntity(url, ApiErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        ApiErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Should return 400 for invalid date format")
    void shouldReturn400ForInvalidDateFormat() {
        // Given
        String invalidDate = "invalid-date";
        String url = String.format("http://localhost:%d/api/v1/prices?brandId=%d&productId=%d&applicationDate=%s",
                port, BRAND_ID, PRODUCT_ID, invalidDate);

        // When
        ResponseEntity<ApiErrorResponse> response = restTemplate.getForEntity(url, ApiErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        ApiErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Should return 400 for negative brand ID")
    void shouldReturn400ForNegativeBrandId() {
        // Given
        Long negativeBrandId = -1L;
        String applicationDate = "2020-06-14T10:00:00";
        String url = buildUrl(negativeBrandId, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<ApiErrorResponse> response = restTemplate.getForEntity(url, ApiErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        ApiErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Should return 400 for negative product ID")
    void shouldReturn400ForNegativeProductId() {
        // Given
        Long negativeProductId = -1L;
        String applicationDate = "2020-06-14T10:00:00";
        String url = buildUrl(BRAND_ID, negativeProductId, applicationDate);

        // When
        ResponseEntity<ApiErrorResponse> response = restTemplate.getForEntity(url, ApiErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        ApiErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Should handle exactly start date boundary")
    void shouldHandleExactlyStartDateBoundary() {
        // Given - Exact start date of price list 2
        String applicationDate = "2020-06-14T15:00:00";
        String url = buildUrl(BRAND_ID, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<PriceResponseDto> response = restTemplate.getForEntity(url, PriceResponseDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        PriceResponseDto priceResponse = response.getBody();
        assertThat(priceResponse.getPriceList()).isEqualTo(2L);
        assertThat(priceResponse.getPrice()).isEqualByComparingTo("25.45");
    }

    @Test
    @DisplayName("Should handle exactly end date boundary")
    void shouldHandleExactlyEndDateBoundary() {
        // Given - Exact end date of price list 2
        String applicationDate = "2020-06-14T18:30:00";
        String url = buildUrl(BRAND_ID, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<PriceResponseDto> response = restTemplate.getForEntity(url, PriceResponseDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        PriceResponseDto priceResponse = response.getBody();
        assertThat(priceResponse.getPriceList()).isEqualTo(2L);
        assertThat(priceResponse.getPrice()).isEqualByComparingTo("25.45");
    }

    @Test
    @DisplayName("Should prioritize higher priority price when multiple prices overlap")
    void shouldPrioritizeHigherPriorityPrice() {
        // Given - Time when both price list 1 (priority 0) and 2 (priority 1) are active
        String applicationDate = "2020-06-14T16:00:00";
        String url = buildUrl(BRAND_ID, PRODUCT_ID, applicationDate);

        // When
        ResponseEntity<PriceResponseDto> response = restTemplate.getForEntity(url, PriceResponseDto.class);

        // Then - Should return price list 2 (higher priority)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        PriceResponseDto priceResponse = response.getBody();
        assertThat(priceResponse.getPriceList()).isEqualTo(2L);
        assertThat(priceResponse.getPrice()).isEqualByComparingTo("25.45");
    }

    private String buildUrl(Long brandId, Long productId, String applicationDate) {
        return String.format("http://localhost:%d/api/v1/prices?brandId=%d&productId=%d&applicationDate=%s",
                port, brandId, productId, applicationDate);
    }
}
