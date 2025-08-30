package co.com.crediya.usecase.user.validators;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.usecase.user.exceptions.InvalidUsuarioException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class UsuarioValidator {

    public static Mono<Usuario> validate(Usuario usuario) {
        List<String> errors = new ArrayList<>();

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            errors.add("El nombre es obligatorio");
        }

        if (usuario.getApellidos() == null || usuario.getApellidos().isBlank()) {
            errors.add("Los apellidos son obligatorios");
        }

        if (usuario.getFechaNacimiento() == null) {
            errors.add("La fecha nacimiento es obligatoria");
        }

        if (usuario.getDireccion() == null || usuario.getDireccion().isBlank()) {
            errors.add("La dirección es obligatoria");
        }

        if (usuario.getSalarioBase() == null) {
            errors.add("El salario es obligatorio");
        } else if (usuario.getSalarioBase() < 0 || usuario.getSalarioBase() > 15000000) {
            errors.add("El salario debe estar entre 0 y 15000000");
        }

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            errors.add("El email es obligatorio");
        } else if (!usuario.getEmail().contains("@")) {
            errors.add("El email no es válido");
        }

        if (usuario.getIdRol() == null) {
            errors.add("El rol es obligatorio");
        }

        if (!errors.isEmpty()) {
            return Mono.error(new InvalidUsuarioException(errors));
        }

        return Mono.just(usuario);
    }
}
