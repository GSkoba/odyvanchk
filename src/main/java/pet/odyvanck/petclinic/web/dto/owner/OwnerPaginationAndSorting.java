package pet.odyvanck.petclinic.web.dto.owner;

import lombok.Data;
import pet.odyvanck.petclinic.web.dto.PaginationAndSortingRequestParams;
import pet.odyvanck.petclinic.web.dto.validation.ValidSortingFields;

@Data
public class OwnerPaginationAndSorting extends PaginationAndSortingRequestParams {
    @ValidSortingFields(allowed = {
            "id",
            "userId",
            "firstName",
            "lastName",
            "phone",
            "email",
            "createdAt",
            "updatedAt"
    })
    private String[] sortBy;
}
