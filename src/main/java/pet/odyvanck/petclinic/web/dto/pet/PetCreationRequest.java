package pet.odyvanck.petclinic.web.dto.pet;

import jakarta.validation.constraints.*;
import pet.odyvanck.petclinic.domain.PetType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PetCreationRequest(
        @NotBlank
        String name,
        @NotNull @PastOrPresent
        LocalDate birthDate,
        @NotNull
        PetType type,
        @NotNull
        Long ownerId,
        String breed,
        String color,
        @DecimalMin(value = "0.1")
        BigDecimal weight
) {
}
