package pet.odyvanck.petclinic.web.dto.visit;

import org.springframework.format.annotation.DateTimeFormat;
import pet.odyvanck.petclinic.domain.VisitStatus;

import java.time.LocalDate;

/**
 * Encapsulates filtering parameters for searching visits.
 * All fields are optional.
 */
public record VisitRequestParams(
        Long vetId,

        VisitStatus status,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate
) { }
