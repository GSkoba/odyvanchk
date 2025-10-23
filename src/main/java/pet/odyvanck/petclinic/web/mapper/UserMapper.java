package pet.odyvanck.petclinic.web.mapper;


import org.mapstruct.Mapper;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.UserRegistrationDto;
import pet.odyvanck.petclinic.web.dto.UserResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegistrationDto request);

    UserResponseDto toDto(User user);
}
