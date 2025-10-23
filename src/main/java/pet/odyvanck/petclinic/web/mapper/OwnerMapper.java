package pet.odyvanck.petclinic.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.OwnerCreationRequest;
import pet.odyvanck.petclinic.web.dto.OwnerCreationResponse;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    User toUser(OwnerCreationRequest request);

    Owner toOwner(OwnerCreationRequest request);

    @Mapping(source = "owner.id", target = "id")
    @Mapping(source = "owner.email", target = "email")
    @Mapping(source = "owner.createdAt", target = "createdAt")
    OwnerCreationResponse toDto(User user, Owner owner);
}
