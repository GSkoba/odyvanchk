package pet.odyvanck.petclinic.service;

import jakarta.transaction.Transactional;
import pet.odyvanck.petclinic.domain.User;

public interface UserService {

    /**
     * Registrates user in the system.
     * @param user user info.
     * @param password raw password.
     * @return created user.
     */
    @Transactional
    User register(User user, String password);
}
