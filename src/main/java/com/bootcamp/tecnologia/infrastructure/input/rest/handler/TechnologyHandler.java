package com.bootcamp.tecnologia.infrastructure.input.rest.handler;

import com.bootcamp.tecnologia.domain.api.ITechnologyServicePort;
import com.bootcamp.tecnologia.infrastructure.input.rest.dto.TechnologyRequest;
import com.bootcamp.tecnologia.infrastructure.input.rest.mapper.ITechnologyRequestMapper;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TechnologyHandler {

    private final ITechnologyServicePort technologyServicePort;
    private final ITechnologyRequestMapper mapper;
    private final Validator validator;

    public TechnologyHandler(ITechnologyServicePort technologyServicePort,
                             ITechnologyRequestMapper mapper,
                             Validator validator) {
        this.technologyServicePort = technologyServicePort;
        this.mapper = mapper;
        this.validator = validator;
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(TechnologyRequest.class)
                .flatMap(this::validateRequest)
                .map(mapper::toDomain)
                .flatMap(technologyServicePort::save)
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        boolean ascending = Boolean.parseBoolean(request.queryParam("ascending").orElse("true"));

        return technologyServicePort.findAll(page, size, ascending)
                .map(mapper::toResponse)
                .collectList()
                .flatMap(list -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(list));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return technologyServicePort.findById(id)
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        String name = request.pathVariable("name");
        return technologyServicePort.findByName(name)
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    private Mono<TechnologyRequest> validateRequest(TechnologyRequest request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            return Mono.error(new IllegalArgumentException(message));
        }
        return Mono.just(request);
    }
}
