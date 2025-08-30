package co.com.crediya.api.config;

import co.com.crediya.usecase.user.exceptions.InvalidUsuarioException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Configuration
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, Object> body;

        if (ex instanceof InvalidUsuarioException invalidEx) {
            status = HttpStatus.BAD_REQUEST;
            body = Map.of(
                    "error", "Validación fallida",
                    "detalles", invalidEx.getErrors()
            );
        } else {
            body = Map.of(
                    "error", "Error interno",
                    "detalle", ex.getMessage()
            );
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();

        try {
            byte[] bytes = mapper.writeValueAsBytes(body);
            return exchange.getResponse().writeWith(Mono.just(bufferFactory.wrap(bytes)));
        } catch (Exception writeEx) {
            return exchange.getResponse().setComplete();
        }
    }
}
