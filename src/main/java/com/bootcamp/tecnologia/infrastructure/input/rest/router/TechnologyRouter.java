package com.bootcamp.tecnologia.infrastructure.input.rest.router;

import com.bootcamp.tecnologia.infrastructure.input.rest.handler.TechnologyHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.bootcamp.tecnologia.infrastructure.input.rest.dto.TechnologyRequest;
import com.bootcamp.tecnologia.infrastructure.input.rest.dto.TechnologyResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class TechnologyRouter {

    private static final String BASE_PATH = "/api/technologies";

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/technologies",
                    method = RequestMethod.POST,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "createTechnology",
                            summary = "Registrar una nueva tecnología",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TechnologyRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Tecnología creada",
                                            content = @Content(schema = @Schema(implementation = TechnologyResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                                    @ApiResponse(responseCode = "409", description = "La tecnología ya existe")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/technologies",
                    method = RequestMethod.GET,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "findAll",
                    operation = @Operation(
                            operationId = "listTechnologies",
                            summary = "Listar tecnologías paginadas y ordenadas",
                            parameters = {
                                    @Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
                                    @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10")),
                                    @Parameter(name = "ascending", in = ParameterIn.QUERY, schema = @Schema(type = "boolean", defaultValue = "true"))
                            },
                            responses = @ApiResponse(responseCode = "200", description = "Lista de tecnologías")
                    )
            ),
            @RouterOperation(
                    path = "/api/technologies/{id}",
                    method = RequestMethod.GET,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            operationId = "getTechnologyById",
                            summary = "Obtener tecnología por ID",
                            parameters = @Parameter(name = "id", in = ParameterIn.PATH, required = true),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Tecnología encontrada"),
                                    @ApiResponse(responseCode = "404", description = "No encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/technologies/name/{name}",
                    method = RequestMethod.GET,
                    beanClass = TechnologyHandler.class,
                    beanMethod = "findByName",
                    operation = @Operation(
                            operationId = "getTechnologyByName",
                            summary = "Obtener tecnología por nombre",
                            parameters = @Parameter(name = "name", in = ParameterIn.PATH, required = true),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Tecnología encontrada"),
                                    @ApiResponse(responseCode = "404", description = "No encontrada")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> technologyRoutes(TechnologyHandler handler) {
        return RouterFunctions
                .route(POST(BASE_PATH).and(accept(MediaType.APPLICATION_JSON)), handler::save)
                .andRoute(GET(BASE_PATH), handler::findAll)
                .andRoute(GET(BASE_PATH + "/{id}"), handler::findById)
                .andRoute(GET(BASE_PATH + "/name/{name}"), handler::findByName);
    }
}
