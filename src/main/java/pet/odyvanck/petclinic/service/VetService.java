package pet.odyvanck.petclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.web.dto.vet.VetUpdateRequest;

public interface VetService {

    Vet create(Vet vet, User user, String password);

    Vet update(Long id, VetUpdateRequest request);

    Vet getById(Long id);

    Page<Vet> getAll(PageRequest pageRequest);

    void delete(Long id);

}
