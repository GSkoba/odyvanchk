package pet.odyvanck.petclinic.web.dto;

import lombok.Data;

@Data
public class OwnerRequestParams {
        private String email;
        private String phone;
        private String firstName;
}

