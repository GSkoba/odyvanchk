package pet.odyvanck.petclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.odyvanck.petclinic.dao.VetRepository;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.domain.error.EntityNotFoundException;
import pet.odyvanck.petclinic.web.dto.vet.VetUpdateRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VetServiceImpl implements VetService {

    private final UserService userService;
    private final VetRepository vetRepository;

    @Transactional
    @Override
    public Vet create(Vet vet, User user, String password) {
        User createdUser = userService.register(user, password);
        vet.setUser(createdUser);
        return vetRepository.save(vet);
    }

    @Transactional
    @Override
    public Vet update(UUID id, VetUpdateRequest request) {
        Vet vet = getById(id);
        vet.getUser().setFirstName(request.firstName());
        vet.getUser().setLastName(request.lastName());
        vet.setPhone(request.phone());
        vet.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        return vetRepository.save(vet);
    }


    @Transactional(readOnly = true)
    @Override
    public Vet getById(UUID id) {
        return vetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vet", "id", id.toString()));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Vet> getAll(PageRequest pageRequest) {
        return vetRepository.findAll(pageRequest);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        if (vetRepository.existsById(id)) {
            vetRepository.deleteById(id);
        }
    }

}
