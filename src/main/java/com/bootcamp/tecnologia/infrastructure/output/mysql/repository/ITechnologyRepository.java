package com.bootcamp.tecnologia.infrastructure.output.mysql.repository;

import com.bootcamp.tecnologia.infrastructure.output.mysql.entity.TechnologyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {

    Mono<Boolean> existsByName(String name);

    Mono<TechnologyEntity> findByName(String name);

    @Query("SELECT * FROM tecnologias ORDER BY nombre ASC LIMIT :limit OFFSET :offset")
    Flux<TechnologyEntity> findAllAsc(int limit, int offset);

    @Query("SELECT * FROM tecnologias ORDER BY nombre DESC LIMIT :limit OFFSET :offset")
    Flux<TechnologyEntity> findAllDesc(int limit, int offset);
}
