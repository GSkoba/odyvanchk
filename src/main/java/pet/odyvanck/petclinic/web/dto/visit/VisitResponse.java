package pet.odyvanck.petclinic.web.dto.visit;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import pet.odyvanck.petclinic.domain.VisitStatus;

import java.time.LocalDateTime;

@Data
public class VisitResponse {

    private Long id;
    private Long petId;
    private Long vetId;
    private Long slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private VisitStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cancellationReason;
    private LocalDateTime completionDateTime;
}
