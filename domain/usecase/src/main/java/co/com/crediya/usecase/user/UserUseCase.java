package co.com.crediya.usecase.user;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.user.validators.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {
    private final UsuarioRepository usuarioRepository;

    @Override
    public Mono<Usuario> createUser(Usuario usuario) {
        return UsuarioValidator.validate(usuario)
                .flatMap(usuarioRepository::registrarUsuario);
    }

    @Override
    public Mono<Usuario> getUsuarioById(BigInteger id) {
        return usuarioRepository.getUsuarioById(id);
    }

    @Override
    public Mono<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.getUsuarioByEmail(email);
    }
}
