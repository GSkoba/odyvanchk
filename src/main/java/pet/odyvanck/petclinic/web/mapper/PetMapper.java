package pet.odyvanck.petclinic.web.mapper;

import org.mapstruct.*;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.web.dto.pet.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(source = "ownerId", target = "owner.id")
    Pet toEntity(PetCreationRequest request);

    @Mapping(source = "owner.id", target = "ownerId")
    PetResponse toDto(Pet pet);

    List<PetResponse> toDto(List<Pet> pets);

}

