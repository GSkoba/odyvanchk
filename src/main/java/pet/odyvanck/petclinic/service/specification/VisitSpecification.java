package pet.odyvanck.petclinic.service.specification;

import org.springframework.data.jpa.domain.Specification;
import pet.odyvanck.petclinic.domain.Visit;
import pet.odyvanck.petclinic.domain.VisitStatus;

import java.time.*;

public class VisitSpecification {

    public static Specification<Visit> hasVet(Long vetId) {
        return (root, query, cb) ->
                vetId == null ? null : cb.equal(root.get("vet").get("id"), vetId);
    }

    public static Specification<Visit> hasStatus(VisitStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Visit> inDateRange(Instant startDate, Instant endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) return null;
            Instant start = startDate != null ? startDate : endDate;
            Instant end = endDate != null ? endDate : startDate;
            return cb.between(root.get("slot").get("startTime"), start, end);
        };
    }
}

