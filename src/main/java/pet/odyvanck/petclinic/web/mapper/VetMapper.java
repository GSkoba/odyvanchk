package pet.odyvanck.petclinic.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.web.dto.vet.VetCreationRequest;
import pet.odyvanck.petclinic.web.dto.vet.VetResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VetMapper {

    Vet toVet(VetCreationRequest request);

    User toUser(VetCreationRequest request);

    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    VetResponse toDto(Vet vet);

    List<VetResponse> toDto(List<Vet> vet);

}
