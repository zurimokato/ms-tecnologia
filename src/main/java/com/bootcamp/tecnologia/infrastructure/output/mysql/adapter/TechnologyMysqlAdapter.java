package com.bootcamp.tecnologia.infrastructure.output.mysql.adapter;

import com.bootcamp.tecnologia.domain.model.Technology;
import com.bootcamp.tecnologia.domain.spi.ITechnologyPersistencePort;
import com.bootcamp.tecnologia.infrastructure.output.mysql.mapper.ITechnologyEntityMapper;
import com.bootcamp.tecnologia.infrastructure.output.mysql.repository.ITechnologyRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TechnologyMysqlAdapter implements ITechnologyPersistencePort {

    private final ITechnologyRepository repository;
    private final ITechnologyEntityMapper mapper;

    public TechnologyMysqlAdapter(ITechnologyRepository repository, ITechnologyEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Technology> save(Technology technology) {
        return repository.save(mapper.toEntity(technology))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Flux<Technology> findAll(int page, int size, boolean ascending) {
        int offset = page * size;
        if (ascending) {
            return repository.findAllAsc(size, offset).map(mapper::toDomain);
        }
        return repository.findAllDesc(size, offset).map(mapper::toDomain);
    }

    @Override
    public Mono<Technology> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Technology> findByName(String name) {
        return repository.findByName(name).map(mapper::toDomain);
    }
}
