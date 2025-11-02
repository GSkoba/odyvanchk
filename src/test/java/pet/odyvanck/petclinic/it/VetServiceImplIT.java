package pet.odyvanck.petclinic.it;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pet.odyvanck.petclinic.dao.VetRepository;
import pet.odyvanck.petclinic.data.UserTestFactory;
import pet.odyvanck.petclinic.data.VetTestFactory;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.domain.error.EntityNotFoundException;
import pet.odyvanck.petclinic.service.UserService;
import pet.odyvanck.petclinic.service.VetService;
import pet.odyvanck.petclinic.web.dto.vet.VetUpdateRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(IntegrationTestConfig.class)
class VetServiceImplIT {

    @Container
    static final PostgreSQLContainer<?> postgres = IntegrationTestConfig.postgreSQLContainer();

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private VetService vetService;

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private UserService userService;

    private List<Vet> preloadedVets;
    private final int count = 3;

    @BeforeEach
    void setup() {
        vetRepository.deleteAll();

        preloadedVets = new ArrayList<>();
        List<Vet> owners = VetTestFactory.createVetListWithoutUserAndId(count);
        List<User> users = UserTestFactory.createUserListWithoutId(count);
        for (int i = 0; i < count; i++) {
            var saved = vetService.create(owners.get(i), users.get(i), "password" + i);
            preloadedVets.add(saved);
        }
    }

    @AfterAll
    static void clean() {
        postgres.stop();
    }


    @Test
    @DisplayName("Creation of vet with linked user")
    void createSuccessfully() {
        Vet vet = VetTestFactory.createVetWithoutIdAndUser();
        User user = UserTestFactory.createUserWithoutId();

        Vet created = vetService.create(vet, user, "password");

        assertThat(created.getId()).isNotNull();
        assertThat(created.getUser()).isNotNull();
        assertThat(vetRepository.findById(created.getId())).isPresent();
    }

    @Test
    @DisplayName("Update existing vet data")
    void updateSuccessfully() {
        VetUpdateRequest req = new VetUpdateRequest("Jane", "Smith", "987654321");

        Vet updated = vetService.update(preloadedVets.getFirst().getId(), req);

        assertThat(updated.getUser().getFirstName()).isEqualTo("Jane");
        assertThat(updated.getUser().getLastName()).isEqualTo("Smith");
        assertThat(updated.getPhone()).isEqualTo("987654321");
        assertThat(updated.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Fail for non-existent vet")
    void updateThrowsEntityNotFoundException() {
        VetUpdateRequest req = new VetUpdateRequest("Jane", "Smith", "987654321");
        assertThatThrownBy(() -> vetService.update(999L, req))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Getting persisted vet")
    void getByIdSuccessfully() {
        var saved = preloadedVets.get(2);

        Vet found = vetService.getById(saved.getId());

        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getUser().getFirstName()).isEqualTo(saved.getUser().getFirstName());
    }

    @Test
    @DisplayName("Fail when vet not found")
    void getByIdThrowsEntityNotFoundException() {
        assertThatThrownBy(() -> vetService.getById(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Getting paginated vets")
    void getAllSuccessfully() {
        Page<Vet> result = vetService.getAll(PageRequest.of(0, 10));

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().size()).isEqualTo(preloadedVets.size());
    }

    @Test
    @DisplayName("Deletion vet by ID")
    void deleteSuccessfully() {
        var saved = preloadedVets.get(2);

        vetService.delete(saved.getId());

        assertThat(vetRepository.findById(saved.getId())).isEmpty();
    }
}

