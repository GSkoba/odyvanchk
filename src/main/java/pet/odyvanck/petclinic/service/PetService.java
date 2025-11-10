package pet.odyvanck.petclinic.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.web.dto.pet.PetRequestParams;
import pet.odyvanck.petclinic.web.dto.pet.PetUpdateRequest;

import java.util.UUID;

public interface PetService {
    Pet create(@NotNull Pet pet);

    Pet update(@NotNull UUID id, @NotNull PetUpdateRequest request);

    Pet getById(@NotNull UUID id);

    Page<Pet> getAll(@NotNull PageRequest pageRequest, @NotNull PetRequestParams filterFields);

    void deleteById(@NotNull UUID id);
}

