package pet.odyvanck.petclinic.web.dto.owner;


public record OwnerRequestParams(
        String email,
        String phone,
        String firstName
) {
}

