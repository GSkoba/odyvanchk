package pet.odyvanck.petclinic.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.service.OwnerService;
import pet.odyvanck.petclinic.web.dto.*;
import pet.odyvanck.petclinic.web.dto.owner.OwnerCreationRequest;
import pet.odyvanck.petclinic.web.dto.owner.OwnerRequestParams;
import pet.odyvanck.petclinic.web.dto.owner.OwnerResponse;
import pet.odyvanck.petclinic.web.mapper.OwnerMapper;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final OwnerMapper ownerMapper;

    @PostMapping
    public ResponseEntity<OwnerResponse> create(@Valid @RequestBody OwnerCreationRequest request) {
        User user = ownerMapper.toUser(request);
        Owner owner = ownerMapper.toOwner(request);
        var created = ownerService.register(owner, user, request.password());
        return ResponseEntity.created(
                URI.create("/api/v1/owners" + created.getId())
        ).body(ownerMapper.toDto(user, created));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OwnerResponse>> getAll(
            @Valid PaginationRequestParams paginationRequestParams,
            OwnerRequestParams ownerRequestParams
    ) {
        PageRequest pageRequest = paginationRequestParams.buildPageRequest();
        Page<Owner> ownerPage = ownerService.getAll(pageRequest, ownerRequestParams);
        PageResponse<OwnerResponse> response = PageResponse.from(
                ownerPage, ownerMapper::toDto
        );
        return ResponseEntity.ok(response);
    }

}
