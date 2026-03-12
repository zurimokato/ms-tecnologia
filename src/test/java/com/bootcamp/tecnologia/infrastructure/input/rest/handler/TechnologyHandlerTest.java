package com.bootcamp.tecnologia.infrastructure.input.rest.handler;

import com.bootcamp.tecnologia.domain.api.ITechnologyServicePort;
import com.bootcamp.tecnologia.domain.model.Technology;
import com.bootcamp.tecnologia.infrastructure.input.rest.dto.TechnologyRequest;
import com.bootcamp.tecnologia.infrastructure.input.rest.dto.TechnologyResponse;
import com.bootcamp.tecnologia.infrastructure.input.rest.mapper.ITechnologyRequestMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyHandlerTest {

    @Mock
    private ITechnologyServicePort servicePort;

    @Mock
    private ITechnologyRequestMapper mapper;

    private TechnologyHandler handler;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void setUp() {
        handler = new TechnologyHandler(servicePort, mapper, validator);
    }

    @Test
    @DisplayName("save - debe retornar 201 cuando la tecnología se crea exitosamente")
    void save_ShouldReturn201() {
        TechnologyRequest request = new TechnologyRequest("Java", "Lenguaje backend");
        Technology domain = new Technology(null, "Java", "Lenguaje backend");
        Technology saved = new Technology(1L, "Java", "Lenguaje backend");
        TechnologyResponse response = new TechnologyResponse(1L, "Java", "Lenguaje backend");

        when(mapper.toDomain(any(TechnologyRequest.class))).thenReturn(domain);
        when(servicePort.save(domain)).thenReturn(Mono.just(saved));
        when(mapper.toResponse(saved)).thenReturn(response);

        MockServerRequest serverRequest = MockServerRequest.builder()
                .body(Mono.just(request));

        StepVerifier.create(handler.save(serverRequest))
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();
    }

    @Test
    @DisplayName("save - debe lanzar error con nombre vacío")
    void save_WithBlankName_ShouldFail() {
        TechnologyRequest request = new TechnologyRequest("", "Descripción válida");

        MockServerRequest serverRequest = MockServerRequest.builder()
                .body(Mono.just(request));

        StepVerifier.create(handler.save(serverRequest))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    @DisplayName("findAll - debe retornar 200 con lista de tecnologías")
    void findAll_ShouldReturn200() {
        Technology tech = new Technology(1L, "Java", "Backend");
        TechnologyResponse response = new TechnologyResponse(1L, "Java", "Backend");

        when(servicePort.findAll(0, 10, true)).thenReturn(Flux.just(tech));
        when(mapper.toResponse(tech)).thenReturn(response);

        MockServerRequest serverRequest = MockServerRequest.builder()
                .queryParam("page", "0")
                .queryParam("size", "10")
                .queryParam("ascending", "true")
                .build();

        StepVerifier.create(handler.findAll(serverRequest))
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }
}
