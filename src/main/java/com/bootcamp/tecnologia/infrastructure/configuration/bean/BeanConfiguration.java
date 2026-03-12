package com.bootcamp.tecnologia.infrastructure.configuration.bean;

import com.bootcamp.tecnologia.domain.api.ITechnologyServicePort;
import com.bootcamp.tecnologia.domain.spi.ITechnologyPersistencePort;
import com.bootcamp.tecnologia.domain.usecase.TechnologyUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ITechnologyServicePort technologyServicePort(ITechnologyPersistencePort persistencePort) {
        return new TechnologyUseCase(persistencePort);
    }
}
