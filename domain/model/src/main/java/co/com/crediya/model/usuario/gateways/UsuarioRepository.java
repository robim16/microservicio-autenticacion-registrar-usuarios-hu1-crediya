package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UsuarioRepository {
    Mono<Usuario> registrarUsuario(Usuario usuario);

    Mono<Usuario> getUsuarioById(BigInteger id);
    Mono<Usuario> getUsuarioByEmail(String email);
}
