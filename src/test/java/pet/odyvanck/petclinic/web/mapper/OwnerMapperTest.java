package pet.odyvanck.petclinic.web.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.owner.OwnerCreationRequest;
import pet.odyvanck.petclinic.web.dto.owner.OwnerResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerMapperTest {

    private final OwnerMapper mapper = Mappers.getMapper(OwnerMapper.class);

    @Test
    void toOwner() {
        OwnerCreationRequest request = new OwnerCreationRequest(
                "John",
                "Doe",
                "password123",
                "+1234567890",
                "john@example.com",
                "123 Street"
        );

        Owner owner = mapper.toOwner(request);

        assertThat(owner).isNotNull();
        assertThat(owner.getPhone()).isEqualTo("+1234567890");
        assertThat(owner.getAddress()).isEqualTo("123 Street");
    }

    @Test
    void toUser() {
        OwnerCreationRequest request = new OwnerCreationRequest(
                "Jane",
                "Smith",
                "password123",
                "+1987654321",
                "jane@example.com",
                "456 Avenue"
        );

        User user = mapper.toUser(request);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("jane@example.com");
        assertThat(user.getFirstName()).isEqualTo("Jane");
        assertThat(user.getLastName()).isEqualTo("Smith");
    }


    @Test
    void toDto() {
        User user = new User();
        user.setId(10L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");

        Owner owner = new Owner();
        owner.setId(100L);
        owner.setUser(user);
        owner.setPhone("+123456789");
        owner.setAddress("123 Street");
        owner.setCreatedAt(LocalDateTime.now());
        owner.setUpdatedAt(LocalDateTime.now());

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
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Alice");
        user1.setLastName("Smith");
        user1.setEmail("alice@example.com");

        Owner owner1 = new Owner();
        owner1.setId(101L);
        owner1.setUser(user1);
        owner1.setPhone("+111111111");
        owner1.setAddress("Street 1");
        owner1.setCreatedAt(LocalDateTime.now());
        owner1.setUpdatedAt(LocalDateTime.now());

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Bob");
        user2.setLastName("Johnson");
        user2.setEmail("bob@example.com");

        Owner owner2 = new Owner();
        owner2.setId(102L);
        owner2.setUser(user2);
        owner2.setPhone("+222222222");
        owner2.setAddress("Street 2");
        owner2.setCreatedAt(LocalDateTime.now());
        owner2.setUpdatedAt(LocalDateTime.now());

        List<Owner> owners = List.of(owner1, owner2);

        List<OwnerResponse> dtos = mapper.toDto(owners);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).userId()).isEqualTo(user1.getId());
        assertThat(dtos.get(0).firstName()).isEqualTo(user1.getFirstName());
        assertThat(dtos.get(1).userId()).isEqualTo(user2.getId());
        assertThat(dtos.get(1).firstName()).isEqualTo(user2.getFirstName());
    }

}

