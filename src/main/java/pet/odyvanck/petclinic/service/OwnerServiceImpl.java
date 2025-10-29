package pet.odyvanck.petclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.odyvanck.petclinic.dao.OwnerRepository;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.error.EntityNotFoundException;
import pet.odyvanck.petclinic.service.specification.OwnerSpecification;
import pet.odyvanck.petclinic.web.dto.owner.OwnerRequestParams;
import pet.odyvanck.petclinic.web.dto.owner.OwnerUpdateRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    @Transactional(readOnly = true)
    @Override
    public Page<Owner> getAll(PageRequest pageRequest, OwnerRequestParams filter) {
        var spec = Specification.<Owner>unrestricted().and(OwnerSpecification.hasEmail(filter.getEmail()))
                .and(OwnerSpecification.hasPhone(filter.getPhone()))
                .and(OwnerSpecification.hasFirstName(filter.getFirstName()));
        return ownerRepository.findAll(spec, pageRequest);
    }

    @Transactional(readOnly = true)
    public Owner getById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner", "id", id.toString()));
    }

    @Transactional
    public Owner update(Long id, OwnerUpdateRequest fieldsToUpdate) {
        Owner owner = getById(id);
        owner.setPhone(fieldsToUpdate.phone());
        owner.setAddress(fieldsToUpdate.address());
        owner.getUser().setFirstName(fieldsToUpdate.firstName());
        owner.getUser().setLastName(fieldsToUpdate.lastName());
        owner.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        return ownerRepository.save(owner);
    }

    @Transactional
    public void deleteById(Long id) {
        if (ownerRepository.existsById(id)) {
            ownerRepository.deleteById(id);
        }
    }

}
