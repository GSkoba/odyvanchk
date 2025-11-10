package pet.odyvanck.petclinic.web.dto.owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OwnerUpdateRequest(

        @NotBlank
        @Size(max = 50)
        String firstName,

        @NotBlank
        @Size(max = 50)
        String lastName,

        @Size(max = 20)
        String phone,

        @Size(max = 255)
        String address
) {
}
