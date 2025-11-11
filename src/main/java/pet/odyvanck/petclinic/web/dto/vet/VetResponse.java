package pet.odyvanck.petclinic.web.dto.vet;

import java.time.LocalDateTime;
import java.util.UUID;

public record VetResponse(
        UUID id,
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
