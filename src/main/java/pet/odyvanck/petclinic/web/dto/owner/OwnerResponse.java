package pet.odyvanck.petclinic.web.dto.owner;

import java.time.LocalDateTime;
import java.util.UUID;

public record OwnerResponse(
        UUID id,
        UUID userId,
        String firstName,
        String lastName,
        String phone,
        String email,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
