package com.prueba.tecnica.pricing.infrastructure.configuration;

import com.prueba.tecnica.pricing.domain.port.outbound.PriceRepositoryPort;
import com.prueba.tecnica.pricing.domain.service.PriceQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    PriceQueryService priceQueryService(PriceRepositoryPort priceRepositoryPort) {
        return new PriceQueryService(priceRepositoryPort);
    }
}
