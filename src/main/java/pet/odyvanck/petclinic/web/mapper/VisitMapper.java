package pet.odyvanck.petclinic.web.mapper;

import org.mapstruct.*;
import pet.odyvanck.petclinic.domain.Visit;
import pet.odyvanck.petclinic.domain.VisitFilters;
import pet.odyvanck.petclinic.web.dto.visit.VisitRequestParams;
import pet.odyvanck.petclinic.web.dto.visit.VisitResponse;

import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    VisitFilters toEntity(VisitRequestParams params);

    @AfterMapping
    default void mapDates(VisitRequestParams params, @MappingTarget VisitFilters target) {
        if (params.startDate() != null) {
            target.setStartDate(params.startDate().atStartOfDay(ZoneOffset.UTC).toInstant());
        } else if (params.date() != null) {
            target.setStartDate(params.date().atStartOfDay(ZoneOffset.UTC).toInstant());
        }
        if (params.endDate() != null) {
            target.setEndDate(params.endDate().atStartOfDay(ZoneOffset.UTC).toInstant());
        } else if (params.date() != null) {
            target.setEndDate(params.date().atStartOfDay(ZoneOffset.UTC).toInstant());
        }
    }

    @Mapping(source = "vet.id", target = "vetId")
    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "slot.id", target = "slotId")
    @Mapping(source = "slot.startTime", target = "startTime")
    @Mapping(source = "slot.endTime", target = "endTime")
    VisitResponse toDto(Visit visit);

    List<VisitResponse> toDto(List<Visit> visits);
}
