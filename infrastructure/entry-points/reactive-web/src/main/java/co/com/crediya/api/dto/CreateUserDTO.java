package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;


import java.math.BigInteger;
import java.time.LocalDate;

@Schema(name = "CreateUserDTO", description = "Datos de entrada para crear un usuario")
public record CreateUserDTO(
        @Schema(description = "Documento de identidad", example = "123456789") String documentoIdentidad,
        @Schema(description = "Nombre del usuario", example = "Carlos") String nombre,
        @Schema(description = "Apellidos del usuario", example = "Arteaga") String apellidos,
        @Schema(description = "Correo electrónico", example = "carlos@email.com") String email,
        @Schema(description = "Fecha de nacimiento", example = "1995-08-29") LocalDate fechaNacimiento,
        @Schema(description = "Dirección de residencia", example = "Calle 123") String direccion,
        @Schema(description = "Id del rol asignado", example = "1") BigInteger idRol,
        @Schema(description = "Salario base del usuario", example = "3000000") Long salarioBase,
        @Schema(description = "Password del usuario", example = "12345") String password
) {
}
