package pet.odyvanck.petclinic.web.dto;

import java.time.LocalDateTime;

public record OwnerCreationResponse(
        Long id,
        String firstName,
        String lastName,
        String phone,
        String email,
        String address,
        String city,
        LocalDateTime createdAt
) {
}
