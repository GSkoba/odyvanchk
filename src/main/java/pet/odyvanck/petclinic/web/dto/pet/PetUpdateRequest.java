package pet.odyvanck.petclinic.web.dto.pet;

import jakarta.validation.constraints.*;
import pet.odyvanck.petclinic.domain.PetType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PetUpdateRequest(
        String name,
        @PastOrPresent
        LocalDate birthDate,
        @NotNull
        PetType type,
        String breed,
        String color,
        @DecimalMin(value = "0.1")
        BigDecimal weight
) {
}

