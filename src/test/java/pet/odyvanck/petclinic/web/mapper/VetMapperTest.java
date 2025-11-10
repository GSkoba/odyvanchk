package pet.odyvanck.petclinic.web.mapper;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.web.dto.vet.VetCreationRequest;
import pet.odyvanck.petclinic.web.dto.vet.VetResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class VetMapperTest {

    private final VetMapper vetMapper = Mappers.getMapper(VetMapper.class);

    @Test
    @DisplayName("Map VetCreationRequest to Vet entity successfully")
    void toVetSuccessfully() {
        VetCreationRequest request =
                new VetCreationRequest(
                        "John",
                        "Doe",
                        "StrongPass123",
                        "+1234567890",
                        "john@example.com"
                );
        Vet result = vetMapper.toVet(request);

        assertThat(result).isNotNull();
        assertThat(result.getPhone()).isEqualTo("+1234567890");
    }

    @Test
    @DisplayName("Map VetCreationRequest to User entity successfully")
    void toUserSuccessfully() {
        VetCreationRequest request = new VetCreationRequest(
                "John",
                "Doe",
                "StrongPass123",
                "+1234567890",
                "john@example.com"
        );

        User result = vetMapper.toUser(request);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Map Vet entity to VetResponse successfully")
    void toDtoSuccessfully() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("firstName")
                .lastName("lastName")
                .email("email@example.com")
                .build();

        Vet vet = Vet.builder()
                .id(UUID.randomUUID())
                .user(user)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now())
                .phone("+1234567890")
                .build();

        VetResponse dto = vetMapper.toDto(vet);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(vet.getId());
        assertThat(dto.userId()).isEqualTo(user.getId());
        assertThat(dto.firstName()).isEqualTo("firstName");
        assertThat(dto.lastName()).isEqualTo("lastName");
        assertThat(dto.email()).isEqualTo("email@example.com");
        assertThat(dto.phone()).isEqualTo("+1234567890");
        assertThat(dto.createdAt()).isEqualTo(vet.getCreatedAt());
        assertThat(dto.updatedAt()).isEqualTo(vet.getUpdatedAt());
    }

    @Test
    @DisplayName("Map list of Vets to list of VetResponses successfully")
    void toDtoListSuccessfully() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("firstName")
                .lastName("lastName")
                .email("email@example.com")
                .build();

        Vet vet = Vet.builder()
                .id(UUID.randomUUID())
                .user(user)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now())
                .phone("+1234567890")
                .build();

        List<Vet> vets = List.of(vet);

        List<VetResponse> result = vetMapper.toDto(vets);

        assertThat(result).hasSize(1);
        VetResponse dto = result.getFirst();
        assertThat(dto.userId()).isEqualTo(user.getId());
        assertThat(dto.firstName()).isEqualTo("firstName");
        assertThat(dto.lastName()).isEqualTo("lastName");
        assertThat(dto.phone()).isEqualTo("+1234567890");
    }
}
