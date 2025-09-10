package co.com.crediya.usecase.user;

import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.usuario.security.PasswordService;
import co.com.crediya.model.usuario.security.TokenService;
import co.com.crediya.usecase.user.exceptions.InvalidCredentialsException;
import co.com.crediya.usecase.user.exceptions.RolNotFoundException;
import co.com.crediya.usecase.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.mockito.Mockito.*;

class UsuarioUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private PasswordService passwordService;
    private TokenService tokenService;

    private UserUseCase usuarioUseCase;

    @BeforeEach
    void setUp() {
        // Creamos mocks de las dependencias
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        rolRepository = Mockito.mock(RolRepository.class);
        passwordService = Mockito.mock(PasswordService.class);
        tokenService = Mockito.mock(TokenService.class);

        // Inyectamos los mocks en la clase bajo prueba
        usuarioUseCase = new UserUseCase(
                usuarioRepository,
                rolRepository,
                tokenService,
                passwordService
        );
    }

    @Test
    void login_debeRetornarTokenCuandoCredencialesSonValidas() {

        Usuario usuario = Usuario.builder()
                .email("test@test.com")
                .password("hashedPassword")
                .idRol(BigInteger.valueOf(1L))
                .build();

        String tokenEsperado = "jwtToken";

        // Simulamos que el usuario existe
        when(usuarioRepository.getUsuarioByEmail("test@test.com"))
                .thenReturn(Mono.just(usuario));

        // Simulamos que el rol existe
        when(rolRepository.getRolById(BigInteger.valueOf(1L)))
                .thenReturn(Mono.just(
                        Rol.builder().id(BigInteger.valueOf(1L)).nombre("ADMIN").build()));

        // Simulamos que la contraseña es correcta
        when(passwordService.matches("1234", "hashedPassword"))
                .thenReturn(true);

        // Simulamos la generación del token
        when(tokenService.generateToken("test@test.com", "ADMIN"))
                .thenReturn(tokenEsperado);

        Mono<String> resultado = usuarioUseCase.login("test@test.com", "1234");


        StepVerifier.create(resultado)
                .expectNext(tokenEsperado)
                .verifyComplete();

        verify(usuarioRepository, times(1)).getUsuarioByEmail("test@test.com");
        verify(rolRepository, times(1)).getRolById(BigInteger.valueOf(1L));
        verify(passwordService, times(1)).matches("1234", "hashedPassword");
        verify(tokenService, times(1)).generateToken("test@test.com", "ADMIN");
    }

    @Test
    void login_debeLanzarUserNotFoundExceptionSiUsuarioNoExiste() {

        when(usuarioRepository.getUsuarioByEmail("noexiste@test.com"))
                .thenReturn(Mono.empty());

        Mono<String> resultado = usuarioUseCase.login("noexiste@test.com", "1234");

        StepVerifier.create(resultado)
                .expectError(UserNotFoundException.class)
                .verify();

        verify(usuarioRepository, times(1)).getUsuarioByEmail("noexiste@test.com");
        verifyNoInteractions(rolRepository, passwordService, tokenService);
    }

    @Test
    void login_debeLanzarRolNotFoundExceptionSiRolNoExiste() {

        Usuario usuario = Usuario.builder()
                .email("test@test.com")
                .password("hashedPassword")
                .idRol(BigInteger.valueOf(99L))
                .build();


        when(usuarioRepository.getUsuarioByEmail("test@test.com"))
                .thenReturn(Mono.just(usuario));

        // Simulamos que el rol no existe
        when(rolRepository.getRolById(BigInteger.valueOf(99L))).thenReturn(Mono.empty());


        Mono<String> resultado = usuarioUseCase.login("test@test.com", "1234");


        StepVerifier.create(resultado)
                .expectError(RolNotFoundException.class)
                .verify();

        verify(usuarioRepository, times(1)).getUsuarioByEmail("test@test.com");
        verify(rolRepository, times(1)).getRolById(BigInteger.valueOf(99L));
        verifyNoInteractions(passwordService, tokenService);
    }

    @Test
    void login_debeLanzarInvalidCredentialsExceptionSiPasswordEsIncorrecta() {

        Usuario usuario = Usuario.builder()
                .email("test@test.com")
                .password("hashedPassword")
                .idRol(BigInteger.valueOf(1L))
                .build();


        when(usuarioRepository.getUsuarioByEmail("test@test.com"))
                .thenReturn(Mono.just(usuario));

        when(rolRepository.getRolById(BigInteger.valueOf(1L)))
                .thenReturn(Mono.just(
                        Rol.builder().id(BigInteger.valueOf(1L)).nombre("ADMIN").build()));

        // Simulamos contraseña incorrecta
        when(passwordService.matches("wrongpass", "hashedPassword"))
                .thenReturn(false);

        Mono<String> resultado = usuarioUseCase.login("test@test.com", "wrongpass");

        StepVerifier.create(resultado)
                .expectError(InvalidCredentialsException.class)
                .verify();

        verify(usuarioRepository, times(1)).getUsuarioByEmail("test@test.com");
        verify(rolRepository, times(1)).getRolById(BigInteger.valueOf(1L));
        verify(passwordService, times(1)).matches("wrongpass", "hashedPassword");
        verifyNoInteractions(tokenService);
    }
}
