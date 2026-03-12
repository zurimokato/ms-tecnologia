package com.bootcamp.tecnologia.infrastructure.output.mysql.adapter;

import com.bootcamp.tecnologia.domain.model.Technology;
import com.bootcamp.tecnologia.infrastructure.output.mysql.entity.TechnologyEntity;
import com.bootcamp.tecnologia.infrastructure.output.mysql.mapper.ITechnologyEntityMapper;
import com.bootcamp.tecnologia.infrastructure.output.mysql.repository.ITechnologyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyMysqlAdapterTest {

    @Mock
    private ITechnologyRepository repository;

    @Mock
    private ITechnologyEntityMapper mapper;

    @InjectMocks
    private TechnologyMysqlAdapter adapter;

    @Test
    @DisplayName("save - debe persistir y retornar la tecnología")
    void save_ShouldPersistAndReturn() {
        Technology domain = new Technology(null, "Java", "Lenguaje backend");
        TechnologyEntity entity = new TechnologyEntity(null, "Java", "Lenguaje backend");
        TechnologyEntity savedEntity = new TechnologyEntity(1L, "Java", "Lenguaje backend");
        Technology savedDomain = new Technology(1L, "Java", "Lenguaje backend");

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(savedEntity));
        when(mapper.toDomain(savedEntity)).thenReturn(savedDomain);

        StepVerifier.create(adapter.save(domain))
                .expectNextMatches(t -> t.getId().equals(1L) && t.getName().equals("Java"))
                .verifyComplete();
    }

    @Test
    @DisplayName("existsByName - debe retornar true si existe")
    void existsByName_WhenExists_ShouldReturnTrue() {
        when(repository.existsByName("Java")).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByName("Java"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("existsByName - debe retornar false si no existe")
    void existsByName_WhenNotExists_ShouldReturnFalse() {
        when(repository.existsByName("Rust")).thenReturn(Mono.just(false));

        StepVerifier.create(adapter.existsByName("Rust"))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll ascendente - debe retornar lista paginada")
    void findAll_Ascending_ShouldReturnPaged() {
        TechnologyEntity e1 = new TechnologyEntity(1L, "Angular", "Frontend");
        TechnologyEntity e2 = new TechnologyEntity(2L, "Java", "Backend");
        Technology d1 = new Technology(1L, "Angular", "Frontend");
        Technology d2 = new Technology(2L, "Java", "Backend");

        when(repository.findAllAsc(10, 0)).thenReturn(Flux.just(e1, e2));
        when(mapper.toDomain(e1)).thenReturn(d1);
        when(mapper.toDomain(e2)).thenReturn(d2);

        StepVerifier.create(adapter.findAll(0, 10, true))
                .expectNext(d1)
                .expectNext(d2)
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll descendente - debe retornar lista paginada")
    void findAll_Descending_ShouldReturnPaged() {
        TechnologyEntity e1 = new TechnologyEntity(2L, "Java", "Backend");
        Technology d1 = new Technology(2L, "Java", "Backend");

        when(repository.findAllDesc(10, 0)).thenReturn(Flux.just(e1));
        when(mapper.toDomain(e1)).thenReturn(d1);

        StepVerifier.create(adapter.findAll(0, 10, false))
                .expectNext(d1)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById - debe retornar tecnología cuando existe")
    void findById_WhenExists_ShouldReturn() {
        TechnologyEntity entity = new TechnologyEntity(1L, "Java", "Backend");
        Technology domain = new Technology(1L, "Java", "Backend");

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        StepVerifier.create(adapter.findById(1L))
                .expectNextMatches(t -> t.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("findById - debe retornar vacío cuando no existe")
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findById(99L))
                .verifyComplete();
    }

    @Test
    @DisplayName("findByName - debe retornar tecnología cuando existe")
    void findByName_WhenExists_ShouldReturn() {
        TechnologyEntity entity = new TechnologyEntity(1L, "Java", "Backend");
        Technology domain = new Technology(1L, "Java", "Backend");

        when(repository.findByName("Java")).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        StepVerifier.create(adapter.findByName("Java"))
                .expectNextMatches(t -> t.getName().equals("Java"))
                .verifyComplete();
    }
}
