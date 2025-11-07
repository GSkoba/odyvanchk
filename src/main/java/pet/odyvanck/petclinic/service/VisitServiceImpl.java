package pet.odyvanck.petclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.odyvanck.petclinic.dao.VisitRepository;
import pet.odyvanck.petclinic.domain.*;
import pet.odyvanck.petclinic.domain.error.EntityNotFoundException;
import pet.odyvanck.petclinic.domain.error.InvalidStateException;
import pet.odyvanck.petclinic.service.specification.VisitSpecification;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final VetSlotService slotService;
    private final PetService petService;

    @Transactional
    @Override
    public Visit create(Long petId, Long slotId) {
        Pet existingPet = petService.getById(petId);
        VetSlot slotInfo = slotService.getById(slotId);
        if (exists(petId, slotInfo.getStartTime()).isPresent()) {
            throw new InvalidStateException("Pet already has visit at this time");
        }
        VetSlot slot = slotService.bookSlot(slotId);

        Visit visit = new Visit();
        visit.setPet(existingPet);
        visit.setVet(slot.getVet());
        visit.setSlot(slot);
        visit.setStatus(VisitStatus.SCHEDULED);

        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit reschedule(Long visitId, Long newSlotId) {
        Visit visit = getById(visitId);

        if (visit.getStatus() == VisitStatus.CANCELLED || visit.getStatus() == VisitStatus.COMPLETED) {
            throw new InvalidStateException("Cannot reschedule a cancelled or completed visit");
        }

        slotService.releaseSlot(visit.getSlot().getId());
        VetSlot newSlot = slotService.bookSlot(newSlotId);

        visit.setSlot(newSlot);
        visit.setVet(newSlot.getVet());

        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit cancel(Long visitId, String reason) {
        Visit visit = getById(visitId);

        if (visit.getStatus() == VisitStatus.COMPLETED) {
            throw new InvalidStateException("Cannot cancel a completed visit");
        }

        visit.setStatus(VisitStatus.CANCELLED);
        visit.setCancellationReason(reason);

        slotService.releaseSlot(visit.getSlot().getId());
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit complete(Long visitId) {
        Visit visit = getById(visitId);

        if (visit.getStatus() != VisitStatus.SCHEDULED) {
            throw new InvalidStateException("Only scheduled visits can be completed");
        }

        visit.setStatus(VisitStatus.COMPLETED);
        visit.setCompletionDateTime(LocalDateTime.now());
        return visitRepository.save(visit);
    }

    @Transactional(readOnly = true)
    @Override
    public Visit getById(Long visitId) {
        return visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit", "id", visitId.toString()));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Visit> exists(Long petId, LocalDateTime time) {
        return visitRepository.findByPetIdAndSlotStartTimeAndStatus(petId, time, VisitStatus.SCHEDULED);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Visit> searchVisits(VisitFilters params, PageRequest pageRequest) {
        var spec = Specification.<Visit>unrestricted().and(VisitSpecification.hasStatus(params.getStatus()))
                .and(VisitSpecification.hasVet(params.getVetId()))
                .and(VisitSpecification.inDateRange(params.getStartDate(), params.getEndDate()));
        return visitRepository.findAll(spec, pageRequest);
    }
}
