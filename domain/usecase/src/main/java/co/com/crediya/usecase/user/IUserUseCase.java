package co.com.crediya.usecase.user;

import co.com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface IUserUseCase {
    public Mono<Usuario> createUser(Usuario usuario);

    public Mono<Usuario> getUsuarioById(BigInteger id);
    public Mono<Usuario> getUsuarioByEmail(String email);
    public Mono<String> login(String email, String password);
}
