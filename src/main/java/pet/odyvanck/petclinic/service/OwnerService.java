package pet.odyvanck.petclinic.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.owner.OwnerRequestParams;

public interface OwnerService {

    /**
     * Registers owner in the system.
     * @param owner owner information.
     * @param user user related information.
     * @param password raw password.
     * @return created owner.
     */
    @Transactional
    Owner register(Owner owner, User user, String password);

    /**
     * Gets information about owners according to request.
     * @param pageRequest page request for getting owner.
     * @param filter filter params for owner.
     * @return owner list according to params.
     */
    Page<Owner> getAll(PageRequest pageRequest, OwnerRequestParams filter);
}
