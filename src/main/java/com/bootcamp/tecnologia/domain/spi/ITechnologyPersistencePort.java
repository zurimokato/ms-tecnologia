package com.bootcamp.tecnologia.domain.spi;

import com.bootcamp.tecnologia.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {

    Mono<Technology> save(Technology technology);

    Mono<Boolean> existsByName(String name);

    Flux<Technology> findAll(int page, int size, boolean ascending);

    Mono<Technology> findById(Long id);

    Mono<Technology> findByName(String name);
}
