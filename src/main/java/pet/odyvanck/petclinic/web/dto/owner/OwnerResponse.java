package pet.odyvanck.petclinic.web.dto.owner;

import java.time.LocalDateTime;

public record OwnerResponse (
    Long id,
    Long userId,
    String firstName,
    String lastName,
    String phone,
    String email,
    String address,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
