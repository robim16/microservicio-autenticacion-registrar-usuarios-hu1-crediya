package co.com.crediya.usecase.user;

import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.model.usuario.security.PasswordService;
import co.com.crediya.model.usuario.security.TokenService;
import co.com.crediya.usecase.user.exceptions.InvalidCredentialsException;
import co.com.crediya.usecase.user.exceptions.RolNotFoundException;
import co.com.crediya.usecase.user.exceptions.UserNotFoundException;
import co.com.crediya.usecase.user.validators.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TokenService tokenService;
    private final PasswordService passwordService;

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

    @Override
    public Mono<String> login(String email, String password) {
        return usuarioRepository.getUsuarioByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuario no encontrado")))
                .flatMap(usuario ->
                        rolRepository.getRolById(usuario.getIdRol())
                                .switchIfEmpty(Mono.error(new RolNotFoundException("Rol no encontrado")))
                                .flatMap(rol -> {
                                    if (passwordService.matches(password, usuario.getPassword())) {
                                        String token = tokenService.generateToken(
                                                usuario.getEmail(),
                                                rol.getNombre()
                                        );
                                        return Mono.just(token);
                                    } else {
                                        return Mono.error(new InvalidCredentialsException("Credenciales inválidas"));
                                    }
                                })
                );
    }

}
