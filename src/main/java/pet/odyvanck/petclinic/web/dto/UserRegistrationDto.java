package pet.odyvanck.petclinic.web.dto;

public record UserRegistrationDto(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
