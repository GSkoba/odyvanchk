package pet.odyvanck.petclinic.service;

import jakarta.transaction.Transactional;
import pet.odyvanck.petclinic.domain.User;

public interface UserService {

    @Transactional
    User register(User user, String password);
}
