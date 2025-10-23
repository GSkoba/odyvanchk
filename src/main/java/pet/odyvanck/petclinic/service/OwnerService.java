package pet.odyvanck.petclinic.service;

import jakarta.transaction.Transactional;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;

public interface OwnerService {

    @Transactional
    Owner register(Owner owner, User user, String password);
}
