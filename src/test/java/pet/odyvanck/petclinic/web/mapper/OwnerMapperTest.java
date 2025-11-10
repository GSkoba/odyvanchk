package pet.odyvanck.petclinic.web.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.owner.OwnerCreationRequest;
import pet.odyvanck.petclinic.web.dto.owner.OwnerResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerMapperTest {

    private final OwnerMapper mapper = Mappers.getMapper(OwnerMapper.class);

    @Test
    void toOwner() {
        OwnerCreationRequest request = new OwnerCreationRequest(
                "firstName",
                "lastName",
                "password123",
                "+1234567890",
                "email@example.com",
                "address 123 Street"
        );

        Owner owner = mapper.toOwner(request);

        assertThat(owner).isNotNull();
        assertThat(owner.getPhone()).isEqualTo("+1234567890");
        assertThat(owner.getAddress()).isEqualTo("address 123 Street");
    }

    @Test
    void toUser() {
        OwnerCreationRequest request = new OwnerCreationRequest(
                "firstName",
                "lastName",
                "password123",
                "+1234567890",
                "email@example.com",
                "address 123 Street"
        );

        User user = mapper.toUser(request);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("email@example.com");
        assertThat(user.getFirstName()).isEqualTo("firstName");
        assertThat(user.getLastName()).isEqualTo("lastName");
    }


    @Test
    void toDto() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName("firstName")
                .lastName("lastName")
                .email("email@example.com")
                .build();

        Owner owner = Owner.builder()
                .id(UUID.randomUUID())
                .user(user)
                .phone("+123456789")
                .address("address 123 Street")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        OwnerResponse dto = mapper.toDto(owner);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(owner.getId());
        assertThat(dto.userId()).isEqualTo(user.getId());
        assertThat(dto.firstName()).isEqualTo(user.getFirstName());
        assertThat(dto.lastName()).isEqualTo(user.getLastName());
        assertThat(dto.email()).isEqualTo(user.getEmail());
        assertThat(dto.address()).isEqualTo(owner.getAddress());
        assertThat(dto.createdAt()).isEqualTo(owner.getCreatedAt());
        assertThat(dto.updatedAt()).isEqualTo(owner.getUpdatedAt());
    }

    @Test
    void toDtoList() {
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .firstName("firstName1")
                .lastName("lastName1")
                .email("email1@example.com")
                .build();

        Owner owner1 = Owner.builder()
                .id(UUID.randomUUID())
                .user(user1)
                .phone("+111111111")
                .address("address Street 1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        User user2 = User.builder()
                .id(UUID.randomUUID())
                .firstName("firstName2")
                .lastName("lastName2")
                .email("email2@example.com")
                .build();

        Owner owner2 = Owner.builder()
                .id(UUID.randomUUID())
                .user(user2)
                .phone("+222222222")
                .address("address Street 2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Owner> owners = List.of(owner1, owner2);

        List<OwnerResponse> dtos = mapper.toDto(owners);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).userId()).isEqualTo(user1.getId());
        assertThat(dtos.get(0).firstName()).isEqualTo(user1.getFirstName());
        assertThat(dtos.get(1).userId()).isEqualTo(user2.getId());
        assertThat(dtos.get(1).firstName()).isEqualTo(user2.getFirstName());
    }

}

