package pet.odyvanck.petclinic.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.owner.OwnerCreationRequest;
import pet.odyvanck.petclinic.web.dto.owner.OwnerResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    User toUser(OwnerCreationRequest request);

    Owner toOwner(OwnerCreationRequest request);

    @Mapping(source = "owner.id", target = "id")
    @Mapping(source = "owner.createdAt", target = "createdAt")
    @Mapping(source = "owner.updatedAt", target = "updatedAt")
    @Mapping(source = "user.id", target = "userId")
    OwnerResponse toDto(User user, Owner owner);
    
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    OwnerResponse toDto(Owner owner);

    List<OwnerResponse> toDto(List<Owner> owner);

}
