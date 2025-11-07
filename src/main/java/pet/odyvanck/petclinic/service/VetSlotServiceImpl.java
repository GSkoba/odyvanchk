package pet.odyvanck.petclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.odyvanck.petclinic.dao.VetSlotRepository;
import pet.odyvanck.petclinic.domain.SlotStatus;
import pet.odyvanck.petclinic.domain.VetSlot;
import pet.odyvanck.petclinic.domain.error.EntityNotFoundException;
import pet.odyvanck.petclinic.domain.error.InvalidStateException;

import java.time.Instant;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VetSlotServiceImpl implements VetSlotService {

    private final VetSlotRepository vetSlotRepository;

    @Transactional(readOnly = true)
    @Override
    public VetSlot getById(Long slotId) {
        return vetSlotRepository.findById(slotId)
                .orElseThrow(() -> new EntityNotFoundException("Slot", "id", slotId.toString()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<VetSlot> getAvailableSlots(Long vetId) {
        return vetSlotRepository.findByVetIdAndIsAvailableTrue(vetId);
    }

    @Transactional
    @Override
    public VetSlot bookSlot(Long slotId) {
        VetSlot slot = getById(slotId);
        if (!slot.getIsAvailable() || slot.getStatus() != SlotStatus.AVAILABLE || isInPast(slot)) {
            throw new InvalidStateException("Slot is not available for booking");
        }
        slot.setIsAvailable(false);
        slot.setStatus(SlotStatus.BOOKED);
        return vetSlotRepository.save(slot);
    }

    private boolean isInPast(VetSlot slot) {
        return slot.getStartTime().isBefore(ChronoLocalDateTime.from(Instant.now()));
    }

    @Transactional
    @Override
    public VetSlot releaseSlot(Long slotId) {
        VetSlot slot = getById(slotId);
        slot.setIsAvailable(true);
        slot.setStatus(SlotStatus.AVAILABLE);
        return vetSlotRepository.save(slot);
    }

    @Transactional
    @Override
    public VetSlot blockSlot(Long slotId) {
        VetSlot slot = getById(slotId);
        slot.setIsAvailable(false);
        slot.setStatus(SlotStatus.BLOCKED);
        return vetSlotRepository.save(slot);
    }
}

