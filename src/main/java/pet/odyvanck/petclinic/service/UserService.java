package pet.odyvanck.petclinic.service;

import jakarta.validation.constraints.NotNull;
import pet.odyvanck.petclinic.domain.User;

public interface UserService {

    /**
     * Registrates user in the system.
     * @param user user info.
     * @param password raw password.
     * @return created user.
     */
    User register(@NotNull User user, @NotNull String password);
}
