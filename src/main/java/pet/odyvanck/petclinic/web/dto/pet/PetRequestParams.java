package pet.odyvanck.petclinic.web.dto.pet;

import pet.odyvanck.petclinic.domain.PetType;

public record PetRequestParams(
        String ownerId,
        PetType type
) {
}
