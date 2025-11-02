package pet.odyvanck.petclinic.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.service.VetService;
import pet.odyvanck.petclinic.web.dto.PageResponse;
import pet.odyvanck.petclinic.web.dto.PaginationAndSortingRequestParams;
import pet.odyvanck.petclinic.web.dto.vet.*;
import pet.odyvanck.petclinic.web.mapper.VetMapper;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/vets")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;
    private final VetMapper vetMapper;

    @PostMapping
    public ResponseEntity<VetResponse> create(@Valid @RequestBody VetCreationRequest request) {
        Vet vet = vetMapper.toVet(request);
        User user = vetMapper.toUser(request);
        Vet created = vetService.create(vet, user, request.password());

        return ResponseEntity.created(URI.create("/api/v1/vets/" + created.getId()))
                .body(vetMapper.toDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VetResponse> getById(@PathVariable Long id) {
        Vet vet = vetService.getById(id);
        return ResponseEntity.ok(vetMapper.toDto(vet));
    }

    @GetMapping
    public ResponseEntity<PageResponse<VetResponse>> getAll(
            @Valid PaginationAndSortingRequestParams pageAndSort
            ) {
        PageRequest pageRequest = pageAndSort.buildPageRequest();
        Page<Vet> vetPage = vetService.getAll(pageRequest);
        PageResponse<VetResponse> response = PageResponse.from(vetPage, vetMapper::toDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VetResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VetUpdateRequest request
    ) {
        Vet updated = vetService.update(id, request);
        return ResponseEntity.ok(vetMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
