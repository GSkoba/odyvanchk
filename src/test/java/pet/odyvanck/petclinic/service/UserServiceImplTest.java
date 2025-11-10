package pet.odyvanck.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import pet.odyvanck.petclinic.dao.UserRepository;
import pet.odyvanck.petclinic.data.UserTestFactory;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.UserStatus;
import pet.odyvanck.petclinic.domain.error.EntityAlreadyExistsException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Register user with encoded password, ACTIVE status")
    void registerSuccessfully() {
        User user = UserTestFactory.createUserWithoutId();
        String rawPassword = "strongPsw";
        String hashedPassword = "superHashedPsw";

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(rawPassword)).willReturn(hashedPassword);
        given(userRepository.save(user)).willReturn(user);

        User saved = userService.register(user, rawPassword);

        assertThat(saved.getPasswordHash()).isEqualTo(hashedPassword);
        assertThat(saved.getStatus()).isEqualTo(UserStatus.ACTIVE);
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("EntityAlreadyExistsException when email already exists")
    void registerDuplicateEmail() {
        User user = UserTestFactory.createUserWithoutId();
        String rawPassword = "psw";

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.register(user, rawPassword))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("email")
                .hasMessageContaining(user.getEmail());

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

}
