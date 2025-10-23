package pet.odyvanck.petclinic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pet.odyvanck.petclinic.dao.OwnerRepository;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;

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
}
