package pet.odyvanck.petclinic.web.dto;

public record OwnerCreationRequest(
        String firstName,
        String lastName,
        String password,
        String phone,
        String email,
        String address,
        String city
) {}

