package pet.odyvanck.petclinic.service.specification;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import pet.odyvanck.petclinic.domain.Owner;

public class OwnerSpecification {

    public static Specification<Owner> hasEmail(@Nullable String email) {
        return (root, query, cb) ->
                email == null ? null : cb.equal(cb.lower(root.get("user").get("email")), email.toLowerCase());
    }

    public static Specification<Owner> hasPhone(@Nullable String phone) {
        return (root, query, cb) ->
                phone == null ? null : cb.equal(root.get("phone"), phone);
    }

    public static Specification<Owner> hasFirstName(@Nullable String firstName) {
        return (root, query, cb) ->
                firstName == null ? null : cb.like(cb.lower(root.get("user").get("firstName")), "%" + firstName.toLowerCase() + "%");
    }
}
