package pet.odyvanck.petclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.web.dto.pet.PetRequestParams;
import pet.odyvanck.petclinic.web.dto.pet.PetUpdateRequest;

public interface PetService {
    Pet create(Pet pet);

    Pet update(Long id, PetUpdateRequest request);

    Pet getById(Long id);

    Page<Pet> getAll(PageRequest pageRequest, PetRequestParams filterFields);

    void deleteById(Long id);
}

