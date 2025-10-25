package pet.odyvanck.petclinic.web.dto.user;

import pet.odyvanck.petclinic.domain.UserStatus;

public record UserResponseDto(
        String email,
        String id,
        UserStatus status
) {
}
