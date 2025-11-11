package pet.odyvanck.petclinic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pet.odyvanck.petclinic.domain.Pet;

import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID>, JpaSpecificationExecutor<Pet> {
}

