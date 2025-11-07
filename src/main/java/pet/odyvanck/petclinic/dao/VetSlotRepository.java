package pet.odyvanck.petclinic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.odyvanck.petclinic.domain.VetSlot;

import java.util.List;

public interface VetSlotRepository extends JpaRepository<VetSlot, Long> {
    List<VetSlot> findByVetIdAndIsAvailableTrue(Long vetId);
}
