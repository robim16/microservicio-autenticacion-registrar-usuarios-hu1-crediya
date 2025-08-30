package co.com.crediya.api.mapper.user;


import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.dto.UsuarioResponseDTO;
import co.com.crediya.model.usuario.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    Usuario mapToEntity(CreateUserDTO userDTO);

    CreateUserDTO mapToDTO(Usuario usuario);
    UsuarioResponseDTO mapToResponseDTO(Usuario usuario);

}
