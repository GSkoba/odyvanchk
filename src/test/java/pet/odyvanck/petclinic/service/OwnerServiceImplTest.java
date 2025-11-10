package pet.odyvanck.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import pet.odyvanck.petclinic.dao.OwnerRepository;
import pet.odyvanck.petclinic.data.OwnerTestFactory;
import pet.odyvanck.petclinic.data.UserTestFactory;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.owner.OwnerRequestParams;
import pet.odyvanck.petclinic.web.dto.owner.OwnerUpdateRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

class OwnerServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerServiceImpl ownerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Register owner successfully")
    void registerSuccessfully() {
        Owner owner = OwnerTestFactory.createOwnerWithoutIdAndUser();
        User user = UserTestFactory.createUserWithoutId();
        final UUID id = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        Owner savedOwner = OwnerTestFactory.createOwner(id, userId);
        User savedUser = savedOwner.getUser();

        given(userService.register(user, "StrongPass123")).willReturn(savedUser);
        given(ownerRepository.save(owner)).willReturn(savedOwner);

        Owner result = ownerService.register(owner, user, "StrongPass123");

        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(savedUser);
        verify(userService).register(user, "StrongPass123");
        verify(ownerRepository).save(owner);
    }

    @Test
    @DisplayName("Set the saved user into owner before saving")
    void registerSetsUserBeforeSave() {
        Owner owner = OwnerTestFactory.createOwnerWithoutIdAndUser();
        User user = UserTestFactory.createUserWithoutId();
        final UUID userId = UUID.randomUUID();
        User savedUser = UserTestFactory.createUser(userId);

        given(userService.register(user, "pwd")).willReturn(savedUser);
        given(ownerRepository.save(owner)).willReturn(owner);

        ownerService.register(owner, user, "pwd");

        assertThat(owner.getUser()).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("Call repository with correct Specification and return page")
    void getAllSuccessfully() {
        OwnerRequestParams params = new OwnerRequestParams("john@example.com", "+1234567890", "John");

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("firstName"));
        List<Owner> owners = OwnerTestFactory.createOwnerList(2);
        Page<Owner> ownerPage = new PageImpl<>(owners, pageRequest, 2);

        given(ownerRepository.findAll(any(Specification.class), eq(pageRequest))).willReturn(ownerPage);

        Page<Owner> result = ownerService.getAll(pageRequest, params);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        verify(ownerRepository, times(1))
                .findAll(any(Specification.class), eq(pageRequest));
    }

    @Test
    @DisplayName("Support empty filters")
    void getAllEmptyFilters() {
        OwnerRequestParams params = new OwnerRequestParams(null, null, null);
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Owner> emptyPage = new PageImpl<>(List.of(), pageRequest, 0);

        given(ownerRepository.findAll(any(Specification.class), eq(pageRequest))).willReturn(emptyPage);

        Page<Owner> result = ownerService.getAll(pageRequest, params);

        assertThat(result.getContent()).isEmpty();
        verify(ownerRepository).findAll(any(Specification.class), eq(pageRequest));
    }

    @Test
    @DisplayName("Getting owner by id")
    void getByIdSuccessfully() {
        final UUID id = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();

        when(ownerRepository.findById(id)).thenReturn(Optional.of(OwnerTestFactory.createOwner(id, userId)));

        Owner found = ownerService.getById(id);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(id);
        verify(ownerRepository).findById(id);
    }

    @Test
    @DisplayName("Throwing exception when owner not found")
    void getByIdThrowsExceptionNotFound() {
        final UUID id = UUID.randomUUID();
        when(ownerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ownerService.getById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Owner with id '" + id + "' not found");

        verify(ownerRepository).findById(id);
    }

    @Test
    @DisplayName("Update of owner entity")
    void updateSuccessfully() {
        OwnerUpdateRequest updateRequest = OwnerTestFactory.createOwnerUpdateRequest();
        final UUID id = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        when(ownerRepository.findById(id)).thenReturn(Optional.of(OwnerTestFactory.createOwner(id, userId)));
        when(ownerRepository.save(any(Owner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Owner updated = ownerService.update(id, updateRequest);

        assertThat(updated.getPhone()).isEqualTo(updateRequest.phone());
        assertThat(updated.getAddress()).isEqualTo(updateRequest.address());
        assertThat(updated.getUser().getFirstName()).isEqualTo(updateRequest.firstName());
        assertThat(updated.getUser().getLastName()).isEqualTo(updateRequest.lastName());
        assertThat(updated.getUpdatedAt()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now(ZoneOffset.UTC));

        verify(ownerRepository).save(any(Owner.class));
    }


    @Test
    @DisplayName("Deletion by id")
    void deleteByIdSuccessfully() {
        final UUID id = UUID.randomUUID();
        when(ownerRepository.existsById(id)).thenReturn(true);

        ownerService.deleteById(id);

        verify(ownerRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deletion when owner does not exist")
    void deleteByIdOwnerDoesNotExist() {
        final UUID id = UUID.randomUUID();
        when(ownerRepository.existsById(id)).thenReturn(false);

        ownerService.deleteById(id);

        verify(ownerRepository, never()).deleteById(any(UUID.class));
    }

}
