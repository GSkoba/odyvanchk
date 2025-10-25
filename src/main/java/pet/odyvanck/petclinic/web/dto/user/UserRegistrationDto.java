package pet.odyvanck.petclinic.web.dto.user;

public record UserRegistrationDto(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
