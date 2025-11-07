package pet.odyvanck.petclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pet.odyvanck.petclinic.domain.Visit;
import pet.odyvanck.petclinic.domain.VisitFilters;

import java.time.LocalDateTime;
import java.util.Optional;


public interface VisitService {

    /**
     * Creates a new visit linked to a specific vet slot.
     *
     * @param petId    The pet id to schedule a visit for
     * @param slotId The ID of the vet slot to book
     * @return The created Visit
     */
    Visit create(Long petId, Long slotId);

    /**
     * Reschedules an existing visit to a new vet slot.
     *
     * @param visitId   The visit to reschedule
     * @param newSlotId The new slot ID
     * @return The updated Visit
     */
    Visit reschedule(Long visitId, Long newSlotId);

    /**
     * Cancels a visit and frees its associated slot.
     *
     * @param visitId The visit to cancel
     * @param reason  The cancellation reason
     * @return The updated Visit
     */
    Visit cancel(Long visitId, String reason);

    /**
     * Marks a visit as completed.
     *
     * @param visitId The visit to complete
     * @return The updated Visit
     */
    Visit complete(Long visitId);

    /**
     * Retrieves a visit by its ID.
     *
     * @param visitId The visit ID
     * @return The Visit entity
     */
    Visit getById(Long visitId);

    /**
     * Checks if visit exists for pet and time.
     *
     * @param petId The pet id
     * @param time Checked time
     * @return Existing visit
     */
    Optional<Visit> exists(Long petId, LocalDateTime time);

    /**
     * Combined search by optional filters: date range, vet and status.
     *
     * @param params Filters: date range, vet and status
     * @param pageRequest Page request info
     * @return Selected visits
     */
    Page<Visit> searchVisits(VisitFilters params, PageRequest pageRequest);

}

