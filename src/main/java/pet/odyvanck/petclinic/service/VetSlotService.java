package pet.odyvanck.petclinic.service;

import pet.odyvanck.petclinic.domain.VetSlot;

import java.util.List;


/**
 * Service for managing veterinarian appointment slots.
 * <p>
 * This interface provides operations to retrieve, book, release, and block slots
 * for veterinarians. It defines the contract for any implementation that manages
 * vet slot availability in the scheduling system.
 * </p>
 */
public interface VetSlotService {

    /**
     * Retrieves a veterinarian slot by its unique identifier.
     *
     * @param slotId the unique ID of the slot to retrieve
     * @return the {@link VetSlot} corresponding to the provided ID
     */
    VetSlot getById(Long slotId);

    /**
     * Retrieves all available (unbooked and unblocked) slots for a specific veterinarian.
     *
     * @param vetId the unique ID of the veterinarian
     * @return a list of available {@link VetSlot} instances for the specified vet
     */
    List<VetSlot> getAvailableSlots(Long vetId);

    /**
     * Books a slot.
     *
     * @param slotId the unique ID of the slot to book
     * @return the updated {@link VetSlot} after booking
     */
    VetSlot bookSlot(Long slotId);

    /**
     * Releases a previously booked slot, making it available again.
     *
     * @param slotId the unique ID of the slot to release
     * @return the updated {@link VetSlot} after release
     */
    VetSlot releaseSlot(Long slotId);

    /**
     * Blocks a slot to prevent it from being booked (e.g., during a vet’s break or unavailability).
     *
     * @param slotId the unique ID of the slot to block
     * @return the updated {@link VetSlot} after being blocked
     */
    VetSlot blockSlot(Long slotId);
}
