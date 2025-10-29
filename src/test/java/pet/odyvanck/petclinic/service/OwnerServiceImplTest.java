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
        Owner savedOwner = OwnerTestFactory.createOwner(1L, 11L);
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
        User savedUser = UserTestFactory.createUser(2L);

        given(userService.register(user, "pwd")).willReturn(savedUser);
        given(ownerRepository.save(owner)).willReturn(owner);

        ownerService.register(owner, user, "pwd");

        assertThat(owner.getUser()).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("Call repository with correct Specification and return page")
    void getAllSuccessfully() {
        OwnerRequestParams params = new OwnerRequestParams();
        params.setEmail("john@example.com");
        params.setPhone("+1234567890");
        params.setFirstName("John");

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
        OwnerRequestParams params = new OwnerRequestParams();
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
        var id = 1L;
        var userId = 2L;
        when(ownerRepository.findById(id)).thenReturn(Optional.of(OwnerTestFactory.createOwner(id, userId)));

        Owner found = ownerService.getById(id);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(id);
        verify(ownerRepository).findById(id);
    }

    @Test
    @DisplayName("Throwing exception when owner not found")
    void getByIdThrowsExceptionNotFound() {
        var id = 1L;
        when(ownerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ownerService.getById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Owner with id '"+ id +"' not found");

        verify(ownerRepository).findById(id);
    }

    @Test
    @DisplayName("Update of owner entity")
    void updateSuccessfully() {
        OwnerUpdateRequest updateRequest = OwnerTestFactory.createOwnerUpdateRequest();

        when(ownerRepository.findById(1L)).thenReturn(Optional.of(OwnerTestFactory.createOwner(1L, 2L)));
        when(ownerRepository.save(any(Owner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Owner updated = ownerService.update(1L, updateRequest);

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
        when(ownerRepository.existsById(1L)).thenReturn(true);

        ownerService.deleteById(1L);

        verify(ownerRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deletion when owner does not exist")
    void deleteByIdOwnerDoesNotExist() {
        when(ownerRepository.existsById(1L)).thenReturn(false);

        ownerService.deleteById(1L);

        verify(ownerRepository, never()).deleteById(anyLong());
    }

}
