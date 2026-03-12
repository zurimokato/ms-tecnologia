package com.bootcamp.tecnologia.domain.api;

import com.bootcamp.tecnologia.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {

    Mono<Technology> save(Technology technology);

    Flux<Technology> findAll(int page, int size, boolean ascending);

    Mono<Technology> findById(Long id);

    Mono<Technology> findByName(String name);
}
