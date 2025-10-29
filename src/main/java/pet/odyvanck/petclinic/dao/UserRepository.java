package pet.odyvanck.petclinic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.odyvanck.petclinic.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

