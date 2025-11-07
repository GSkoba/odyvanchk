package pet.odyvanck.petclinic.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class VisitFilters {

    private Long vetId;
    private VisitStatus status;
    private Instant startDate;
    private Instant endDate;

}
