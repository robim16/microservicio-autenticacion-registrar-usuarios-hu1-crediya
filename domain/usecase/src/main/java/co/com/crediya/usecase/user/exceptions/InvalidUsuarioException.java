package co.com.crediya.usecase.user.exceptions;

import java.util.List;

public class InvalidUsuarioException extends RuntimeException {
    private final List<String> errors;

    public InvalidUsuarioException(List<String> errors) {
        super("Errores de validación en usuario");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
