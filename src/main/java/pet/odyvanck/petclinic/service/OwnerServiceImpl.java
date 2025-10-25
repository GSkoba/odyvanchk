package pet.odyvanck.petclinic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pet.odyvanck.petclinic.dao.OwnerRepository;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.service.specification.OwnerSpecification;
import pet.odyvanck.petclinic.web.dto.owner.OwnerRequestParams;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final UserService userService;
    private final OwnerRepository ownerRepository;

    @Transactional
    @Override
    public Owner register(Owner owner, User user, String password) {
        var savedUser = userService.register(user, password);
        owner.setUser(savedUser);
        return ownerRepository.save(owner);
    }

    @Override
    public Page<Owner> getAll(PageRequest pageRequest, OwnerRequestParams filter) {
        var spec = Specification.<Owner>unrestricted().and(OwnerSpecification.hasEmail(filter.getEmail()))
                .and(OwnerSpecification.hasPhone(filter.getPhone()))
                .and(OwnerSpecification.hasFirstName(filter.getFirstName()));
        return ownerRepository.findAll(spec, pageRequest);
    }
}
