package pet.odyvanck.petclinic.web.dto.pet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PetResponse(
        UUID id,
        String name,
        LocalDate birthDate,
        String type,
        UUID ownerId,
        String breed,
        String color,
        BigDecimal weight,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
