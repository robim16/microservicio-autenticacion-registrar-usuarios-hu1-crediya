package co.com.crediya.usecase.user;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> createUser(Usuario usuario) {
        return usuarioRepository.registrarUsuario(usuario);
    }
}
