package pet.odyvanck.petclinic.web.dto;

import java.time.LocalDateTime;

public record OwnerResponse (
    Long id,
    Long userId,
    String firstName,
    String lastName,
    String phone,
    String email,
    String address,
    String city,
    LocalDateTime createdAt
) {
}
