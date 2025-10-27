package pet.odyvanck.petclinic.it;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import pet.odyvanck.petclinic.dao.OwnerRepository;
import pet.odyvanck.petclinic.dao.UserRepository;
import pet.odyvanck.petclinic.service.OwnerServiceImpl;
import pet.odyvanck.petclinic.service.UserService;
import pet.odyvanck.petclinic.service.UserServiceImpl;

@TestConfiguration
public class IntegrationTestConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserServiceImpl userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Bean
    OwnerServiceImpl ownerService(UserService userService, OwnerRepository ownerRepository) {
        return new OwnerServiceImpl(userService, ownerRepository);
    }

    public static PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");

        postgres.start();
        return postgres;
    }
}
