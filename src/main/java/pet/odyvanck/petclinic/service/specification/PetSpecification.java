package pet.odyvanck.petclinic.service.specification;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.domain.PetType;

public class PetSpecification {

    public static Specification<Pet> hasOwner(@Nullable String ownerId) {
        return (root, query, cb) ->
                ownerId == null ? null : cb.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Pet> hasType(@Nullable PetType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type.toString());
    }

}
