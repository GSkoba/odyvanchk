package pet.odyvanck.petclinic.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pet.odyvanck.petclinic.domain.Visit;
import pet.odyvanck.petclinic.domain.VisitStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit> {

    Optional<Visit> findByPetIdAndSlotStartTimeAndStatus(Long petId, LocalDateTime time, VisitStatus status);
}

