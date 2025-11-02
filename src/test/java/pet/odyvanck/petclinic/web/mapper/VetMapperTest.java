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
        User user = new User();
        user.setId(42L);
        user.setFirstName("Alice");
        user.setLastName("Wonderland");
        user.setEmail("alice@example.com");

        Vet vet = new Vet();
        vet.setId(1L);
        vet.setUser(user);
        vet.setPhone("987654321");
        vet.setCreatedAt(LocalDateTime.now().minusDays(2));
        vet.setUpdatedAt(LocalDateTime.now());

        VetResponse dto = vetMapper.toDto(vet);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(vet.getId());
        assertThat(dto.userId()).isEqualTo(user.getId());
        assertThat(dto.firstName()).isEqualTo("Alice");
        assertThat(dto.lastName()).isEqualTo("Wonderland");
        assertThat(dto.email()).isEqualTo("alice@example.com");
        assertThat(dto.phone()).isEqualTo("987654321");
        assertThat(dto.createdAt()).isEqualTo(vet.getCreatedAt());
        assertThat(dto.updatedAt()).isEqualTo(vet.getUpdatedAt());
    }

    @Test
    @DisplayName("Map list of Vets to list of VetResponses successfully")
    void toDtoListSuccessfully() {
        User user = new User();
        user.setId(10L);
        user.setFirstName("Bob");
        user.setLastName("Builder");
        user.setEmail("bob@build.com");

        Vet vet = new Vet();
        vet.setId(5L);
        vet.setUser(user);
        vet.setPhone("123123123");
        vet.setCreatedAt(LocalDateTime.now().minusDays(1));
        vet.setUpdatedAt(LocalDateTime.now());

        List<Vet> vets = List.of(vet);

        List<VetResponse> result = vetMapper.toDto(vets);

        assertThat(result).hasSize(1);
        VetResponse dto = result.getFirst();
        assertThat(dto.userId()).isEqualTo(user.getId());
        assertThat(dto.firstName()).isEqualTo("Bob");
        assertThat(dto.lastName()).isEqualTo("Builder");
        assertThat(dto.phone()).isEqualTo("123123123");
    }
}
