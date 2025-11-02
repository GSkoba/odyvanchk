package pet.odyvanck.petclinic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.odyvanck.petclinic.domain.Vet;

@Repository
public interface VetRepository extends JpaRepository<Vet, Long> {
}
