package pet.odyvanck.petclinic.web.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.domain.PetType;
import pet.odyvanck.petclinic.web.dto.pet.PetCreationRequest;
import pet.odyvanck.petclinic.web.dto.pet.PetResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PetMapperTest {

    private final PetMapper mapper = Mappers.getMapper(PetMapper.class);

    @Test
    @DisplayName("Should map PetCreationRequest to Pet entity correctly")
    void toEntity() {
        UUID ownerId = UUID.randomUUID();

        PetCreationRequest request = new PetCreationRequest(
                "name",
                LocalDate.of(2020, 1, 1),
                PetType.DOG,
                ownerId,
                "breed",
                "color",
                new BigDecimal("10.0")
        );

        Pet pet = mapper.toEntity(request);

        assertThat(pet).isNotNull();
        assertThat(pet.getName()).isEqualTo("name");
        assertThat(pet.getBirthDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(pet.getType()).isEqualTo(PetType.DOG);
        assertThat(pet.getOwner()).isNotNull();
        assertThat(pet.getOwner().getId()).isEqualTo(ownerId);
        assertThat(pet.getBreed()).isEqualTo("breed");
        assertThat(pet.getColor()).isEqualTo("color");
        assertThat(pet.getWeight()).isEqualTo(new BigDecimal("10.0"));
    }

    @Test
    @DisplayName("Should map Pet entity to PetResponse correctly")
    void toDto() {
        UUID petId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Owner owner = Owner.builder().id(ownerId).build();
        Pet pet = Pet.builder()
                .id(petId)
                .name("name")
                .birthDate(LocalDate.of(2020, 1, 1))
                .type(PetType.DOG)
                .owner(owner)
                .breed("breed")
                .color("color")
                .weight(new BigDecimal("10.0"))
                .createdAt(LocalDateTime.of(2021, 1, 1, 12, 0))
                .updatedAt(LocalDateTime.of(2021, 2, 1, 12, 0))
                .build();

        PetResponse response = mapper.toDto(pet);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(petId);
        assertThat(response.ownerId()).isEqualTo(ownerId);
        assertThat(response.name()).isEqualTo("name");
        assertThat(response.birthDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(response.type()).isEqualTo("DOG");
        assertThat(response.breed()).isEqualTo("breed");
        assertThat(response.color()).isEqualTo("color");
        assertThat(response.weight()).isEqualTo(new BigDecimal("10.0"));
        assertThat(response.createdAt()).isEqualTo(pet.getCreatedAt());
        assertThat(response.updatedAt()).isEqualTo(pet.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map List<Pet> to List<PetResponse> correctly")
    void toDtoList() {
        UUID ownerId = UUID.randomUUID();
        Owner owner = Owner.builder().id(ownerId).build();

        Pet pet1 = Pet.builder().id(UUID.randomUUID()).name("name").type(PetType.DOG).owner(owner).build();
        Pet pet2 = Pet.builder().id(UUID.randomUUID()).name("name1").type(PetType.DOG).owner(owner).build();

        List<PetResponse> responses = mapper.toDto(List.of(pet1, pet2));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).name()).isEqualTo("name");
        assertThat(responses.get(1).name()).isEqualTo("name1");
        assertThat(responses.get(0).ownerId()).isEqualTo(ownerId);
    }
}

