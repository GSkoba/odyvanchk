package pet.odyvanck.petclinic.it;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pet.odyvanck.petclinic.dao.OwnerRepository;
import pet.odyvanck.petclinic.data.OwnerTestFactory;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.UserStatus;
import pet.odyvanck.petclinic.domain.error.EntityAlreadyExistsException;
import pet.odyvanck.petclinic.service.OwnerService;
import pet.odyvanck.petclinic.service.UserService;
import pet.odyvanck.petclinic.web.dto.owner.OwnerRequestParams;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Testcontainers
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(IntegrationTestConfig.class)
class OwnerServiceImplIT {

    @Container
    static PostgreSQLContainer<?> postgres = IntegrationTestConfig.postgreSQLContainer();

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.show-sql", () -> "true");
    }

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private UserService userService;

    private List<Owner> preloadedOwners;
    private final int count = 3;

    @BeforeEach
    void setup() {
        ownerRepository.deleteAll();

        preloadedOwners = new ArrayList<>();
        List<Owner> owners = OwnerTestFactory.createOwnerListWithoutIdAndUser(count);
        List<User> users = OwnerTestFactory.createUserListWithoutId(count);
        for (int i = 0; i < count; i++) {
            var saved = ownerService.register(owners.get(i), users.get(i), "password" + i);
            preloadedOwners.add(saved);
        }
    }

    @AfterAll
    static void clean() {
        postgres.stop();
    }

    @Test
    @DisplayName("Owner registration successfully")
    void registerSuccessfully() {
        User user = OwnerTestFactory.createUserWithoutId();
        Owner owner = OwnerTestFactory.createOwnerWithoutIdAndUser();

        Owner savedOwner = ownerService.register(owner, user, "password123");

        assertThat(savedOwner.getId()).isNotNull();
        assertThat(savedOwner.getUser()).isNotNull();
        assertThat(savedOwner.getUser().getId()).isNotNull();
        assertThat(savedOwner.getUser().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(savedOwner.getUser().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Registration with already existing email")
    void registerDuplicatesEmail() {
        User user = OwnerTestFactory.createUserWithoutId();
        Owner owner = OwnerTestFactory.createOwnerWithoutIdAndUser();

        Owner savedOwner = ownerService.register(owner, user, "password123");

        Owner duplicate = OwnerTestFactory.createOwnerWithoutIdAndUser();
        User duplicateUser = OwnerTestFactory.createUserWithoutId();

        assertThatThrownBy(() -> ownerService.register(duplicate, duplicateUser, "anotherPass"))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("email");
    }

    @Test
    @DisplayName("Getting all owners without filtering")
    void getAllWithoutFilters() {
        OwnerRequestParams params = new OwnerRequestParams();
        var page = ownerService.getAll(PageRequest.of(0, 10), params);

        assertThat(page.getTotalElements()).isEqualTo(count);
        assertThat(page.getContent())
                .extracting(owner -> owner.getUser().getFirstName())
                .containsExactlyInAnyOrder(
                        preloadedOwners.stream().map(owner ->
                                owner.getUser().getFirstName()).toArray(String[]::new)
                );
    }

    @Test
    @DisplayName("Getting owner by email")
    void getAllWithEmailFilter() {
        var email = preloadedOwners.getLast().getUser().getEmail();
        var firstName = preloadedOwners.getLast().getUser().getFirstName();
        OwnerRequestParams params = new OwnerRequestParams();
        params.setEmail(email);

        var page = ownerService.getAll(PageRequest.of(0, 10), params);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().getFirst().getUser().getFirstName()).isEqualTo(firstName);
        assertThat(page.getContent().getFirst().getUser().getEmail()).isEqualTo(email);
    }

    @Test
    void getAllWithPhoneFilter() {
        var phone = preloadedOwners.getLast().getPhone();
        var firstName = preloadedOwners.getLast().getUser().getFirstName();
        OwnerRequestParams params = new OwnerRequestParams();
        params.setPhone(phone);

        var page = ownerService.getAll(PageRequest.of(0, 10), params);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().getFirst().getUser().getFirstName()).isEqualTo(firstName);
        assertThat(page.getContent().getFirst().getPhone()).isEqualTo(phone);
    }

    @Test
    void getAllWithFirstNameFilter() {
        OwnerRequestParams params = new OwnerRequestParams();
        var firstName = preloadedOwners.get(1).getUser().getFirstName();
        params.setFirstName(firstName);

        var page = ownerService.getAll(PageRequest.of(0, 10), params);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().getFirst().getUser().getFirstName()).isEqualTo(firstName);
    }
}
