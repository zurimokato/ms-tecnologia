package com.bootcamp.tecnologia.domain.usecase;

import com.bootcamp.tecnologia.domain.api.ITechnologyServicePort;
import com.bootcamp.tecnologia.domain.exception.DomainConstants;
import com.bootcamp.tecnologia.domain.exception.TechnologyAlreadyExistsException;
import com.bootcamp.tecnologia.domain.exception.TechnologyNotFoundException;
import com.bootcamp.tecnologia.domain.model.Technology;
import com.bootcamp.tecnologia.domain.spi.ITechnologyPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TechnologyUseCase implements ITechnologyServicePort {

    private final ITechnologyPersistencePort technologyPersistencePort;

    public TechnologyUseCase(ITechnologyPersistencePort technologyPersistencePort) {
        this.technologyPersistencePort = technologyPersistencePort;
    }

    @Override
    public Mono<Technology> save(Technology technology) {
        return Mono.defer(() -> validate(technology))
                .then(Mono.defer(() -> technologyPersistencePort.existsByName(technology.getName())))
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new TechnologyAlreadyExistsException(technology.getName()));
                    }
                    return technologyPersistencePort.save(technology);
                });
    }

    @Override
    public Flux<Technology> findAll(int page, int size, boolean ascending) {
        return technologyPersistencePort.findAll(page, size, ascending);
    }

    @Override
    public Mono<Technology> findById(Long id) {
        return technologyPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(new TechnologyNotFoundException(id)));
    }

    @Override
    public Mono<Technology> findByName(String name) {
        return technologyPersistencePort.findByName(name)
                .switchIfEmpty(Mono.error(new TechnologyNotFoundException(name)));
    }

    private Mono<Void> validate(Technology technology) {
        if (technology.getName() == null || technology.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException(DomainConstants.NAME_REQUIRED));
        }
        if (technology.getName().length() > DomainConstants.MAX_NAME_LENGTH) {
            return Mono.error(new IllegalArgumentException(DomainConstants.NAME_TOO_LONG));
        }
        if (technology.getDescription() == null || technology.getDescription().isBlank()) {
            return Mono.error(new IllegalArgumentException(DomainConstants.DESCRIPTION_REQUIRED));
        }
        if (technology.getDescription().length() > DomainConstants.MAX_DESCRIPTION_LENGTH) {
            return Mono.error(new IllegalArgumentException(DomainConstants.DESCRIPTION_TOO_LONG));
        }
        return Mono.empty();
    }
}
