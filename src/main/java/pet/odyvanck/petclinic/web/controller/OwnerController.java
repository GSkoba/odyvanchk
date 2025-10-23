package pet.odyvanck.petclinic.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.service.OwnerService;
import pet.odyvanck.petclinic.web.dto.OwnerCreationRequest;
import pet.odyvanck.petclinic.web.dto.OwnerCreationResponse;
import pet.odyvanck.petclinic.web.mapper.OwnerMapper;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final OwnerMapper ownerMapper;

    @PostMapping
    public ResponseEntity<OwnerCreationResponse> register(@Valid @RequestBody OwnerCreationRequest request) {
        User user = ownerMapper.toUser(request);
        Owner owner = ownerMapper.toOwner(request);
        var created = ownerService.register(owner, user, request.password());
        return ResponseEntity.created(
                URI.create("/api/v1/owners" + created.getId())
        ).body(ownerMapper.toDto(user, created));
    }
}
