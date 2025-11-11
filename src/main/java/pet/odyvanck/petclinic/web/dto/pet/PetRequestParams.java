package pet.odyvanck.petclinic.web.dto.pet;

import pet.odyvanck.petclinic.domain.PetType;

import java.util.UUID;

public record PetRequestParams(
        UUID ownerId,
        PetType type
) {
}
