package co.com.crediya.api;

import co.com.crediya.api.config.UsuarioPath;
import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.bind.annotation.RequestMethod;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UsuarioPath usuarioPath;
    private final Handler usuarioHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = { RequestMethod.POST },
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUsuario"

            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/{id}",
                    produces = { "application/json" },
                    method = { RequestMethod.GET },
                    beanClass = Handler.class,
                    beanMethod = "listenGetUserById"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(usuarioPath.getUsuarios()), usuarioHandler::listenSaveUsuario)
                .andRoute(GET(usuarioPath.getUsuariosById()), usuarioHandler::listenGetUserById);
    }
}