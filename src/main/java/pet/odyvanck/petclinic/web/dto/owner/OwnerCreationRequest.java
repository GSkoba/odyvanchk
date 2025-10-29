package pet.odyvanck.petclinic.web.dto.owner;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OwnerCreationRequest(

        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        String password,
        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[+]?\\d{7,20}$",
                message = "Phone number must contain only digits (7-20) and optionally a leading +"
        )
        String phone,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must not exceed 255 characters")
        String email,

        @NotBlank(message = "Address is required")
        @Size(max = 500, message = "Address must not exceed 500 characters")
        String address
) {}

