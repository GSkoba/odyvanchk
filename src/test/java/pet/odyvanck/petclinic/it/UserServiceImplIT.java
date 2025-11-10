package pet.odyvanck.petclinic.it;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pet.odyvanck.petclinic.dao.UserRepository;
import pet.odyvanck.petclinic.data.UserTestFactory;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.UserStatus;
import pet.odyvanck.petclinic.domain.error.EntityAlreadyExistsException;
import pet.odyvanck.petclinic.service.UserServiceImpl;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(IntegrationTestConfig.class)
class UserServiceImplIT {

    @Container
    static final PostgreSQLContainer<?> postgres = IntegrationTestConfig.postgreSQLContainer();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.show-sql", () -> "true");
    }

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = UserTestFactory.createUserWithoutId();
    }

    @AfterAll
    static void clean() {
        postgres.stop();
    }

    @Test
    @DisplayName("User registration successfully")
    void registerSuccessfully() {
        String email = user.getEmail();
        User savedUser = userService.register(user, "securePassword123");

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(savedUser.getPasswordHash()).isNotBlank();
        assertThat(savedUser.getPasswordHash()).isNotEqualTo("securePassword123");

        assertThat(userRepository.findByEmail(email))
                .isPresent()
                .get()
                .extracting(User::getEmail)
                .isEqualTo(email);
    }

    @Test
    @DisplayName("Registration with already existing email")
    void registerDuplicatesEmail() {
        User saved = userService.register(user, "password");

        User duplicate = saved.toBuilder().build();

        assertThatThrownBy(() -> userService.register(duplicate, "anotherPass"))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("email");
    }
}
