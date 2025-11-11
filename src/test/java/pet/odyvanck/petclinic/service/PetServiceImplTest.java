package pet.odyvanck.petclinic.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import pet.odyvanck.petclinic.dao.PetRepository;
import pet.odyvanck.petclinic.data.OwnerTestFactory;
import pet.odyvanck.petclinic.data.PetTestFactory;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.domain.PetType;
import pet.odyvanck.petclinic.domain.error.EntityNotFoundException;
import pet.odyvanck.petclinic.web.dto.pet.PetRequestParams;
import pet.odyvanck.petclinic.web.dto.pet.PetUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    @DisplayName("Create pet successfully")
    void createSuccessfully() {
        UUID ownerId = UUID.randomUUID();
        Owner owner = OwnerTestFactory.createOwner(ownerId, UUID.randomUUID());
        Pet pet = PetTestFactory.createPetWithoutId(owner);
        Pet savedPet = PetTestFactory.createPetWithId(owner);

        given(ownerService.getById(ownerId)).willReturn(owner);
        given(petRepository.save(pet)).willReturn(savedPet);

        Pet result = petService.create(pet);

        assertThat(result).isNotNull();
        assertThat(result.getOwner()).isEqualTo(owner);
        verify(ownerService).getById(ownerId);
        verify(petRepository).save(pet);
    }

    @Test
    @DisplayName("Create pet fails when owner not found")
    void createFailsWhenOwnerNotFound() {
        UUID missingOwnerId = UUID.randomUUID();
        Owner owner = OwnerTestFactory.createOwner(missingOwnerId, UUID.randomUUID());
        Pet pet = PetTestFactory.createPetWithoutId(owner);

        given(ownerService.getById(missingOwnerId))
                .willThrow(new EntityNotFoundException("Owner", "id", missingOwnerId.toString()));

        assertThatThrownBy(() -> petService.create(pet))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Owner");
    }

    @Test
    @DisplayName("Update pet successfully")
    void updateSuccessfully() {
        UUID petId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Owner owner = OwnerTestFactory.createOwner(ownerId, UUID.randomUUID());
        Pet existingPet = PetTestFactory.createPetWithId(owner);
        PetUpdateRequest updateRequest = PetTestFactory.createPetUpdateRequest();

        given(petRepository.findById(petId)).willReturn(Optional.of(existingPet));
        given(petRepository.save(existingPet.toBuilder()
                .name(updateRequest.name())
                .color(updateRequest.color())
                .birthDate(updateRequest.birthDate())
                .weight(updateRequest.weight())
                .breed(updateRequest.breed())
                .type(updateRequest.type())
                .build())).willReturn(existingPet);

        Pet result = petService.update(petId, updateRequest);

        assertThat(result).isNotNull();
        verify(petRepository).findById(petId);
        verify(petRepository).save(result);
    }

    @Test
    @DisplayName("Get pet by id successfully")
    void getByIdSuccessfully() {
        UUID petId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Owner owner = OwnerTestFactory.createOwner(ownerId, UUID.randomUUID());
        Pet pet = PetTestFactory.createPetWithId(owner);

        given(petRepository.findById(petId)).willReturn(Optional.of(pet));

        Pet result = petService.getById(petId);

        assertThat(result).isNotNull();
        assertThat(result.getOwner()).isEqualTo(owner);
        verify(petRepository).findById(petId);
    }

    @Test
    @DisplayName("Get pet by id throws EntityNotFoundException when not found")
    void getByIdThrowsWhenNotFound() {
        UUID petId = UUID.randomUUID();

        given(petRepository.findById(petId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> petService.getById(petId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pet");
    }

    @Test
    @DisplayName("Get all pets successfully with filters")
    void getAllWithFiltersSuccessfully() {
        UUID ownerId = UUID.randomUUID();
        Owner owner = OwnerTestFactory.createOwner(ownerId, UUID.randomUUID());
        Pet pet1 = PetTestFactory.createPetWithId(owner);
        Pet pet2 = PetTestFactory.createPetWithId(owner);
        PageRequest pageRequest = PageRequest.of(0, 10);
        PetRequestParams filters = new PetRequestParams(ownerId, PetType.DOG);

        Page<Pet> petsPage = new PageImpl<>(List.of(pet1, pet2));

        given(petRepository.findAll(any(Specification.class), eq(pageRequest))).willReturn(petsPage);

        Page<Pet> result = petService.getAll(pageRequest, filters);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        verify(petRepository).findAll(any(Specification.class), eq(pageRequest));
    }

    @Test
    @DisplayName("Get all pets successfully without filters")
    void getAllWithoutFiltersSuccessfully() {
        UUID ownerId = UUID.randomUUID();
        Owner owner = OwnerTestFactory.createOwner(ownerId, UUID.randomUUID());
        Pet pet = PetTestFactory.createPetWithId(owner);
        PageRequest pageRequest = PageRequest.of(0, 10);
        PetRequestParams emptyFilters = new PetRequestParams(null, null);

        Page<Pet> petsPage = new PageImpl<>(List.of(pet));

        given(petRepository.findAll(any(Specification.class), eq(pageRequest))).willReturn(petsPage);

        Page<Pet> result = petService.getAll(pageRequest, emptyFilters);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(petRepository).findAll(any(Specification.class), eq(pageRequest));
    }

    @Test
    @DisplayName("Delete pet by id when exists")
    void deleteByIdWhenExists() {
        UUID petId = UUID.randomUUID();

        given(petRepository.existsById(petId)).willReturn(true);

        petService.deleteById(petId);

        verify(petRepository, times(1)).deleteById(petId);
    }

    @Test
    @DisplayName("Delete pet by id when not exists (no action)")
    void deleteByIdWhenNotExists() {
        UUID petId = UUID.randomUUID();

        given(petRepository.existsById(petId)).willReturn(false);

        petService.deleteById(petId);

        verify(petRepository, times(0)).deleteById(petId);
    }
}
