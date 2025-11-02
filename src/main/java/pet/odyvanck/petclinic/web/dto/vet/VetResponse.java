package pet.odyvanck.petclinic.web.dto.vet;

import java.time.LocalDateTime;

public record VetResponse(
        Long id,
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
