package pet.odyvanck.petclinic.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.odyvanck.petclinic.domain.Visit;
import pet.odyvanck.petclinic.service.IdempotencyService;
import pet.odyvanck.petclinic.service.VisitService;
import pet.odyvanck.petclinic.web.dto.PageResponse;
import pet.odyvanck.petclinic.web.dto.PaginationAndSortingRequestParams;
import pet.odyvanck.petclinic.web.dto.visit.VisitRequestParams;
import pet.odyvanck.petclinic.web.dto.visit.VisitResponse;
import pet.odyvanck.petclinic.web.mapper.VisitMapper;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final IdempotencyService idempotencyService;
    private final VisitMapper visitMapper;

    @PostMapping
    public ResponseEntity<VisitResponse> createVisit(
            @RequestParam Long petId,
            @RequestParam Long slotId,
            @RequestHeader(value = "Idempotency-Key", required = false) String key
    ) {
        Visit visit = idempotencyService.execute(
                key,
                () -> visitService.create(petId, slotId),
                Visit.class
        );
        return ResponseEntity.created(URI.create("/api/v1/visits/" + visit.getId()))
                .body(visitMapper.toDto(visit));
    }


    @GetMapping("/{id}/reschedule")
    public Visit rescheduleVisit(
            @PathVariable Long id,
            @RequestParam Long newSlotId
    ) {
        return visitService.reschedule(id, newSlotId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/cancel")
    public void cancelVisit(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "No reason provided") String reason
    ) {
        visitService.cancel(id, reason);
    }

    @PutMapping("/{id}/complete")
    public Visit completeVisit(@PathVariable Long id) {
        return visitService.complete(id);
    }


    @GetMapping("/{id}")
    public Visit getVisitById(@PathVariable Long id) {
        return visitService.getById(id);
    }


    @GetMapping("/search")
    public ResponseEntity<PageResponse<VisitResponse>> searchVisits(
            @ModelAttribute VisitRequestParams params,
            PaginationAndSortingRequestParams paginationAndSortingRequestParams
    ) {
        Page<Visit> visits = visitService.searchVisits(
                visitMapper.toEntity(params),
                paginationAndSortingRequestParams.buildPageRequest()
        );
        PageResponse<VisitResponse> response = PageResponse.from(
                visits, visitMapper::toDto
        );
        return ResponseEntity.ok(response);
    }
}
