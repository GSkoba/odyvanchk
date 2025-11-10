package pet.odyvanck.petclinic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pet.odyvanck.petclinic.domain.Owner;

import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, UUID>, JpaSpecificationExecutor<Owner> {
}
