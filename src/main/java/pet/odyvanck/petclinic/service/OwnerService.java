package pet.odyvanck.petclinic.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.OwnerRequestParams;

public interface OwnerService {

    @Transactional
    Owner register(Owner owner, User user, String password);

    Page<Owner> getAll(PageRequest pageRequest, OwnerRequestParams filter);
}
