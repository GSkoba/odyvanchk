package pet.odyvanck.petclinic.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.web.dto.vet.VetUpdateRequest;

import java.util.UUID;

public interface VetService {

    Vet create(@NotNull Vet vet, @NotNull User user, @NotNull String password);

    Vet update(@NotNull UUID id, @NotNull VetUpdateRequest request);

    Vet getById(@NotNull UUID id);

    Page<Vet> getAll(@NotNull PageRequest pageRequest);

    void delete(@NotNull UUID id);

}
