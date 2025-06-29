package com.prueba.tecnica.pricing.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prueba.tecnica.pricing.infrastructure.persistence.entity.PriceEntity;

/**
 * JPA repository for price entities.
 */
@Repository
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {

@Cacheable("prices")
@Query(value = """
    SELECT p.* FROM PRICES p 
    WHERE p.brand_id = :brandId 
      AND p.product_id = :productId 
      AND p.start_date <= :applicationDate 
      AND p.end_date >= :applicationDate 
    ORDER BY p.priority DESC, p.price DESC
    LIMIT 1
    """, nativeQuery = true)
Optional<PriceEntity> findBestApplicablePrice(
    @Param("brandId") Long brandId,
    @Param("productId") Long productId,
    @Param("applicationDate") LocalDateTime applicationDate
);
}
