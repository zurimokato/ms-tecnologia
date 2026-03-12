package com.bootcamp.tecnologia.domain.usecase;

import com.bootcamp.tecnologia.domain.exception.TechnologyAlreadyExistsException;
import com.bootcamp.tecnologia.domain.exception.TechnologyNotFoundException;
import com.bootcamp.tecnologia.domain.model.Technology;
import com.bootcamp.tecnologia.domain.spi.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private ITechnologyPersistencePort persistencePort;

    @InjectMocks
    private TechnologyUseCase useCase;

    private Technology validTechnology;

    @BeforeEach
    void setUp() {
        validTechnology = new Technology(null, "Java", "Lenguaje de programación orientado a objetos");
    }

    @Nested
    @DisplayName("save() tests")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar tecnología cuando los datos son válidos y el nombre no existe")
        void save_WhenValidAndUniqueName_ShouldSave() {
            Technology saved = new Technology(1L, "Java", "Lenguaje de programación orientado a objetos");
            when(persistencePort.existsByName("Java")).thenReturn(Mono.just(false));
            when(persistencePort.save(any(Technology.class))).thenReturn(Mono.just(saved));

            StepVerifier.create(useCase.save(validTechnology))
                    .expectNextMatches(t -> t.getId().equals(1L) && t.getName().equals("Java"))
                    .verifyComplete();

            verify(persistencePort).existsByName("Java");
            verify(persistencePort).save(any(Technology.class));
        }

        @Test
        @DisplayName("Debe lanzar error cuando el nombre ya existe")
        void save_WhenNameAlreadyExists_ShouldThrow() {
            when(persistencePort.existsByName("Java")).thenReturn(Mono.just(true));

            StepVerifier.create(useCase.save(validTechnology))
                    .expectError(TechnologyAlreadyExistsException.class)
                    .verify();

            verify(persistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar error cuando el nombre es nulo")
        void save_WhenNameIsNull_ShouldThrow() {
            validTechnology.setName(null);

            StepVerifier.create(useCase.save(validTechnology))
                    .expectErrorMatches(e -> e instanceof IllegalArgumentException
                            && e.getMessage().contains("nombre es obligatorio"))
                    .verify();

            verify(persistencePort, never()).existsByName(anyString());
        }

        @Test
        @DisplayName("Debe lanzar error cuando el nombre está vacío")
        void save_WhenNameIsBlank_ShouldThrow() {
            validTechnology.setName("   ");

            StepVerifier.create(useCase.save(validTechnology))
                    .expectError(IllegalArgumentException.class)
                    .verify();
        }

        @Test
        @DisplayName("Debe lanzar error cuando el nombre excede 50 caracteres")
        void save_WhenNameExceeds50Chars_ShouldThrow() {
            validTechnology.setName("A".repeat(51));

            StepVerifier.create(useCase.save(validTechnology))
                    .expectErrorMatches(e -> e instanceof IllegalArgumentException
                            && e.getMessage().contains("50 caracteres"))
                    .verify();
        }

        @Test
        @DisplayName("Debe lanzar error cuando la descripción es nula")
        void save_WhenDescriptionIsNull_ShouldThrow() {
            validTechnology.setDescription(null);

            StepVerifier.create(useCase.save(validTechnology))
                    .expectErrorMatches(e -> e instanceof IllegalArgumentException
                            && e.getMessage().contains("descripción es obligatoria"))
                    .verify();
        }

        @Test
        @DisplayName("Debe lanzar error cuando la descripción está vacía")
        void save_WhenDescriptionIsBlank_ShouldThrow() {
            validTechnology.setDescription("");

            StepVerifier.create(useCase.save(validTechnology))
                    .expectError(IllegalArgumentException.class)
                    .verify();
        }

        @Test
        @DisplayName("Debe lanzar error cuando la descripción excede 90 caracteres")
        void save_WhenDescriptionExceeds90Chars_ShouldThrow() {
            validTechnology.setDescription("A".repeat(91));

            StepVerifier.create(useCase.save(validTechnology))
                    .expectErrorMatches(e -> e instanceof IllegalArgumentException
                            && e.getMessage().contains("90 caracteres"))
                    .verify();
        }

        @Test
        @DisplayName("Debe guardar cuando nombre tiene exactamente 50 caracteres")
        void save_WhenNameHasExactly50Chars_ShouldSave() {
            String name50 = "A".repeat(50);
            validTechnology.setName(name50);
            Technology saved = new Technology(1L, name50, validTechnology.getDescription());

            when(persistencePort.existsByName(name50)).thenReturn(Mono.just(false));
            when(persistencePort.save(any())).thenReturn(Mono.just(saved));

            StepVerifier.create(useCase.save(validTechnology))
                    .expectNextMatches(t -> t.getName().length() == 50)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe guardar cuando descripción tiene exactamente 90 caracteres")
        void save_WhenDescriptionHasExactly90Chars_ShouldSave() {
            String desc90 = "A".repeat(90);
            validTechnology.setDescription(desc90);
            Technology saved = new Technology(1L, validTechnology.getName(), desc90);

            when(persistencePort.existsByName(validTechnology.getName())).thenReturn(Mono.just(false));
            when(persistencePort.save(any())).thenReturn(Mono.just(saved));

            StepVerifier.create(useCase.save(validTechnology))
                    .expectNextMatches(t -> t.getDescription().length() == 90)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("findAll() tests")
    class FindAllTests {

        @Test
        @DisplayName("Debe retornar tecnologías paginadas en orden ascendente")
        void findAll_Ascending_ShouldReturnSorted() {
            Technology t1 = new Technology(1L, "Angular", "Frontend framework");
            Technology t2 = new Technology(2L, "Java", "Backend language");

            when(persistencePort.findAll(0, 10, true)).thenReturn(Flux.just(t1, t2));

            StepVerifier.create(useCase.findAll(0, 10, true))
                    .expectNext(t1)
                    .expectNext(t2)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay tecnologías")
        void findAll_WhenEmpty_ShouldReturnEmpty() {
            when(persistencePort.findAll(0, 10, true)).thenReturn(Flux.empty());

            StepVerifier.create(useCase.findAll(0, 10, true))
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("findById() tests")
    class FindByIdTests {

        @Test
        @DisplayName("Debe retornar tecnología cuando existe por ID")
        void findById_WhenExists_ShouldReturn() {
            Technology tech = new Technology(1L, "Java", "Lenguaje backend");
            when(persistencePort.findById(1L)).thenReturn(Mono.just(tech));

            StepVerifier.create(useCase.findById(1L))
                    .expectNextMatches(t -> t.getId().equals(1L))
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe lanzar error cuando no existe por ID")
        void findById_WhenNotExists_ShouldThrow() {
            when(persistencePort.findById(99L)).thenReturn(Mono.empty());

            StepVerifier.create(useCase.findById(99L))
                    .expectError(TechnologyNotFoundException.class)
                    .verify();
        }
    }

    @Nested
    @DisplayName("findByName() tests")
    class FindByNameTests {

        @Test
        @DisplayName("Debe retornar tecnología cuando existe por nombre")
        void findByName_WhenExists_ShouldReturn() {
            Technology tech = new Technology(1L, "Java", "Lenguaje backend");
            when(persistencePort.findByName("Java")).thenReturn(Mono.just(tech));

            StepVerifier.create(useCase.findByName("Java"))
                    .expectNextMatches(t -> t.getName().equals("Java"))
                    .verifyComplete();
        }

        @Test
        @DisplayName("Debe lanzar error cuando no existe por nombre")
        void findByName_WhenNotExists_ShouldThrow() {
            when(persistencePort.findByName("Rust")).thenReturn(Mono.empty());

            StepVerifier.create(useCase.findByName("Rust"))
                    .expectError(TechnologyNotFoundException.class)
                    .verify();
        }
    }
}
