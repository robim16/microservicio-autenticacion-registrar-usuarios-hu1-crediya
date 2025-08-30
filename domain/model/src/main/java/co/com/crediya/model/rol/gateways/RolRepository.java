package co.com.crediya.model.rol.gateways;

import co.com.crediya.model.rol.Rol;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface RolRepository {
    Mono<Rol> getRolById(BigInteger id);
}
