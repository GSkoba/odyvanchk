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


import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VetServiceImpl implements VetService {

    private final UserService userService;
    private final VetRepository vetRepository;

    @Transactional
    @Override
    public Vet create(Vet vet, User user, String password) {
        Objects.requireNonNull(vet, "vet must be not null");
        Objects.requireNonNull(user, "user must be not null");
        Objects.requireNonNull(password, "password must be not null");

        User createdUser = userService.register(user, password);
        vet.setUser(createdUser);
        return vetRepository.save(vet);
    }

    @Transactional
    @Override
    public Vet update(UUID id, VetUpdateRequest request) {
        Objects.requireNonNull(id, "vet id must be not null");
        Objects.requireNonNull(request, "request must be not null");

        Vet vet = getById(id);

        Vet updatedVet = vet.toBuilder()
                .phone(request.phone())
                .user(vet.getUser().toBuilder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .build())
                .build();
        return vetRepository.save(updatedVet);
    }


    @Transactional(readOnly = true)
    @Override
    public Vet getById(UUID id) {
        Objects.requireNonNull(id, "vet id must be not null");

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
        Objects.requireNonNull(id, "vet id must be not null");

        if (vetRepository.existsById(id)) {
            vetRepository.deleteById(id);
        }
    }

}
