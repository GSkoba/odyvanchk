package pet.odyvanck.petclinic.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.service.PetService;
import pet.odyvanck.petclinic.web.dto.PageResponse;
import pet.odyvanck.petclinic.web.dto.PaginationAndSortingRequestParams;
import pet.odyvanck.petclinic.web.dto.pet.*;
import pet.odyvanck.petclinic.web.mapper.PetMapper;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final PetMapper petMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PetResponse> create(@Validated @RequestBody PetCreationRequest request) {
        Pet pet = petMapper.toEntity(request);
        Pet created = petService.create(pet);
        return ResponseEntity.created(URI.create("/api/v1/pets/" + created.getId()))
                .body(petMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> update(@PathVariable Long id, @Validated @RequestBody PetUpdateRequest request) {
        return ResponseEntity.ok(petMapper.toDto(petService.update(id, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(petMapper.toDto(petService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PetResponse>> getAll(
            @Valid PaginationAndSortingRequestParams paginationAndSorting,
            PetRequestParams petRequestParams
    ) {
        PageRequest pageRequest = paginationAndSorting.buildPageRequest();
        Page<Pet> ownerPage = petService.getAll(pageRequest, petRequestParams);
        PageResponse<PetResponse> response = PageResponse.from(
                ownerPage, petMapper::toDto
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.deleteById(id);
    }
}
