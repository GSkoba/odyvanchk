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
import pet.odyvanck.petclinic.web.dto.owner.OwnerPaginationAndSorting;
import pet.odyvanck.petclinic.web.mapper.OwnerMapper;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final OwnerMapper ownerMapper;
    private final static Map<String, String> SORT_TRANSFORM = Map.of("email","user.email");

    /**
     * Registrates owner in system.
     * @param request owner information.
     * @return created owner entity.
     */
    @PostMapping
    public ResponseEntity<OwnerResponse> create(@Valid @RequestBody OwnerCreationRequest request) {
        User user = ownerMapper.toUser(request);
        Owner owner = ownerMapper.toOwner(request);
        var created = ownerService.register(owner, user, request.password());
        return ResponseEntity.created(
                URI.create("/api/v1/owners/" + created.getId())
        ).body(ownerMapper.toDto(user, created));
    }

    /**
     * Gets all owners by request params.
     * @param paginationAndSorting contains page and sorting info.
     * @param ownerRequestParams filtering params
     * @return all suitable owners.
     */
    @GetMapping
    public ResponseEntity<PageResponse<OwnerResponse>> getAll(
            @Valid OwnerPaginationAndSorting paginationAndSorting,
            OwnerRequestParams ownerRequestParams
    ) {
        PageRequest pageRequest = paginationAndSorting.buildPageRequest(SORT_TRANSFORM);
        Page<Owner> ownerPage = ownerService.getAll(pageRequest, ownerRequestParams);
        PageResponse<OwnerResponse> response = PageResponse.from(
                ownerPage, ownerMapper::toDto
        );
        return ResponseEntity.ok(response);
    }

}
