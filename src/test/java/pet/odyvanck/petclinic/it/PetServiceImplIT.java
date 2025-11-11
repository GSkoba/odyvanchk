package pet.odyvanck.petclinic.it;

import org.junit.jupiter.api.*;
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
import pet.odyvanck.petclinic.dao.OwnerRepository;
import pet.odyvanck.petclinic.dao.PetRepository;
import pet.odyvanck.petclinic.data.OwnerTestFactory;
import pet.odyvanck.petclinic.data.PetTestFactory;
import pet.odyvanck.petclinic.data.UserTestFactory;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.domain.PetType;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.service.OwnerService;
import pet.odyvanck.petclinic.service.PetService;
import pet.odyvanck.petclinic.web.dto.pet.PetRequestParams;
import pet.odyvanck.petclinic.web.dto.pet.PetUpdateRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(IntegrationTestConfig.class)
class PetServiceImplIT {

    @Container
    static PostgreSQLContainer<?> postgres = IntegrationTestConfig.postgreSQLContainer();

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private PetService petService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    private List<Owner> preloadedOwners;
    private List<Pet> preloadedPets;
    private final int ownerCount = 3;
    private final int petCountPerOwner = 2;

    @BeforeEach
    void setup() {
        petRepository.deleteAll();
        ownerRepository.deleteAll();

        preloadedOwners = new ArrayList<>();
        preloadedPets = new ArrayList<>();

        // preload owners and users
        List<Owner> owners = OwnerTestFactory.createOwnerListWithoutIdAndUser(ownerCount);
        List<User> users = UserTestFactory.createUserListWithoutId(ownerCount);

        for (int i = 0; i < ownerCount; i++) {
            var savedOwner = ownerService.register(owners.get(i), users.get(i), "password" + i);
            preloadedOwners.add(savedOwner);
        }

        // preload pets for each owner
        for (int i = 0; i < ownerCount; i++) {
            Owner owner = preloadedOwners.get(i);

            Pet dog = PetTestFactory.createPetWithoutId(owner).toBuilder()
                    .name("Dog" + i)
                    .type(PetType.DOG)
                    .breed("Bulldog" + i)
                    .build();

            Pet cat = PetTestFactory.createPetWithoutId(owner).toBuilder()
                    .name("Cat" + i)
                    .type(PetType.CAT)
                    .breed("Siamese" + i)
                    .build();

            preloadedPets.add(petService.create(dog));
            preloadedPets.add(petService.create(cat));
        }
    }

    @AfterAll
    static void clean() {
        postgres.stop();
    }

    @Test
    @DisplayName("Create pet successfully")
    void createPetSuccessfully() {
        Owner owner = preloadedOwners.getFirst();
        Pet pet = PetTestFactory.createPetWithoutId(owner);

        Pet savedPet = petService.create(pet);

        assertThat(savedPet.getId()).isNotNull();
        assertThat(savedPet.getOwner()).isEqualTo(owner);
        assertThat(savedPet.getName()).isEqualTo(pet.getName());
    }

    @Test
    @DisplayName("Update existing pet successfully")
    void updatePetSuccessfully() {
        Pet existing = preloadedPets.getFirst();

        PetUpdateRequest updateRequest = new PetUpdateRequest(
                "UpdatedName",
                LocalDate.of(2021, 1, 1),
                PetType.CAT,
                "UpdatedBreed",
                "UpdatedColor",
                new BigDecimal("5.5")
        );

        Pet updated = petService.update(existing.getId(), updateRequest);

        assertThat(updated.getName()).isEqualTo("UpdatedName");
        assertThat(updated.getBreed()).isEqualTo("UpdatedBreed");
        assertThat(updated.getColor()).isEqualTo("UpdatedColor");
        assertThat(updated.getType()).isEqualTo(PetType.CAT);
    }

    @Test
    @DisplayName("Get pet by id successfully")
    void getByIdSuccessfully() {
        Pet saved = preloadedPets.get(1);

        Pet found = petService.getById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getOwner().getId()).isEqualTo(saved.getOwner().getId());
    }

    @Test
    @DisplayName("Get all pets successfully with filters")
    void getAllWithFiltersSuccessfully() {
        Owner owner = preloadedOwners.getFirst();
        PetRequestParams filters = new PetRequestParams(owner.getId(), PetType.CAT);
        Page<Pet> page = petService.getAll(PageRequest.of(0, 10), filters);

        assertThat(page).isNotNull();
        assertThat(page.getContent()).isNotEmpty();
        assertThat(page.getContent().stream()
                .allMatch(p -> p.getType() == PetType.CAT && p.getOwner().getId().equals(owner.getId())))
                .isTrue();
    }

    @Test
    @DisplayName("Get all pets without filters")
    void getAllWithoutFiltersSuccessfully() {
        PetRequestParams filters = new PetRequestParams(null, null);
        Page<Pet> page = petService.getAll(PageRequest.of(0, 10), filters);

        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(preloadedPets.size());
    }

    @Test
    @DisplayName("Delete pet by id successfully")
    void deletePetSuccessfully() {
        Pet saved = preloadedPets.getLast();

        petService.deleteById(saved.getId());

        assertThat(petRepository.findById(saved.getId())).isEmpty();
    }
}

