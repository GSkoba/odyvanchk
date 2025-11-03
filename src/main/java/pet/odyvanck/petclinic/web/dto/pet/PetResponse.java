package pet.odyvanck.petclinic.web.dto.pet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PetResponse(
        Long id,
        String name,
        LocalDate birthDate,
        String type,
        Long ownerId,
        String breed,
        String color,
        BigDecimal weight,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
