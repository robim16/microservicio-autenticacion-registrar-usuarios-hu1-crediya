package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "UsuarioResponseDTO", description = "Respuesta con la información del usuario")
public record UsuarioResponseDTO(
        @Schema(description = "Identificador único del usuario", example = "a1b2c3d4e5") String id,
        @Schema(description = "Nombre del usuario", example = "Carlos") String nombre,
        @Schema(description = "Apellidos del usuario", example = "Arteaga") String apellidos,
        @Schema(description = "Correo electrónico", example = "carlos@email.com") String email,
        @Schema(description = "Salario base", example = "3000000") Long salarioBase
) {
}
