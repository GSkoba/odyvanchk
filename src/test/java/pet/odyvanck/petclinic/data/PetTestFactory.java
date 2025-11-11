package pet.odyvanck.petclinic.data;

import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.domain.PetType;
import pet.odyvanck.petclinic.web.dto.pet.PetCreationRequest;
import pet.odyvanck.petclinic.web.dto.pet.PetResponse;
import pet.odyvanck.petclinic.web.dto.pet.PetUpdateRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class PetTestFactory {

    public static Pet createPetWithoutId(Owner owner) {
        return Pet.builder()
                .name("name")
                .birthDate(LocalDate.of(2020, 1, 1))
                .type(PetType.DOG)
                .owner(owner)
                .breed("breed")
                .color("color")
                .weight(new BigDecimal("10.0"))
                .build();
    }

    public static Pet createPetWithId(Owner owner) {
        return Pet.builder()
                .id(UUID.randomUUID())
                .name("name")
                .birthDate(LocalDate.of(2020, 1, 1))
                .type(PetType.DOG)
                .owner(owner)
                .breed("breed")
                .color("color")
                .weight(new BigDecimal("10.0"))
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static PetCreationRequest createPetCreationRequest(UUID ownerId) {
        return new PetCreationRequest(
                "name",
                LocalDate.of(2020, 1, 1),
                PetType.DOG,
                ownerId,
                "breed",
                "color",
                new BigDecimal("10.0")
        );
    }

    public static PetUpdateRequest createPetUpdateRequest() {
        return new PetUpdateRequest(
                "name",
                LocalDate.of(2020, 1, 1),
                PetType.DOG,
                "breed",
                "color",
                new BigDecimal("10.0")
        );
    }

    public static PetResponse createPetResponse(UUID id, UUID ownerId) {
        return new PetResponse(
                id,
                "name",
                LocalDate.of(2020, 1, 1),
                PetType.DOG.name(),
                ownerId,
                "breed",
                "color",
                new BigDecimal("10.0"),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
    }
}

