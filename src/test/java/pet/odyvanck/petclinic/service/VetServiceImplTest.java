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
import pet.odyvanck.petclinic.dao.VetRepository;
import pet.odyvanck.petclinic.data.UserTestFactory;
import pet.odyvanck.petclinic.data.VetTestFactory;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.domain.error.EntityNotFoundException;
import pet.odyvanck.petclinic.web.dto.vet.VetUpdateRequest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetServiceImplTest {

    @Mock
    private VetRepository vetRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private VetServiceImpl vetService;


    @Test
    @DisplayName("Creation of vet successfully when user is registered and vet is saved")
    void createSuccessfully() {
        String password = "securePass123";
        final UUID vetId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        Vet vet = VetTestFactory.createVetWithoutIdAndUser();
        User user = UserTestFactory.createUserWithoutId();
        User createdUser = UserTestFactory.createUser(userId);
        Vet createdVet = VetTestFactory.createVet(vetId, userId);

        when(userService.register(any(), any())).thenReturn(createdVet.getUser());
        when(vetRepository.save(any(Vet.class))).thenReturn(createdVet);

        Vet result = vetService.create(vet, user, password);

        assertNotNull(result);
        assertEquals(createdUser, result.getUser());
        verify(userService).register(any(), any());
        verify(vetRepository).save(any());
    }

    @Test
    @DisplayName("Update vet fields and save successfully")
    void updateSuccessfully() {
        final UUID id = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        VetUpdateRequest request = VetTestFactory.createVetUpdateRequest();
        Vet vet = VetTestFactory.createVet(id, userId);

        when(vetRepository.findById(id)).thenReturn(Optional.of(vet));
        when(vetRepository.save(any(Vet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vet result = vetService.update(id, request);

        assertEquals(request.firstName(), result.getUser().getFirstName());
        assertEquals(request.lastName(), result.getUser().getLastName());
        assertEquals(request.phone(), result.getPhone());
        assertNotNull(result.getUpdatedAt());

        verify(vetRepository).findById(id);
        verify(vetRepository).save(any(Vet.class));
    }

    @Test
    @DisplayName("Throws EntityNotFoundException when updating non-existing vet")
    void updateThrowsEntityNotFoundException() {
        UUID id = UUID.randomUUID();
        VetUpdateRequest request = VetTestFactory.createVetUpdateRequest();
        when(vetRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vetService.update(id, request));
        verify(vetRepository, never()).save(any());
    }

    @Test
    @DisplayName("Vet by ID successfully")
    void getByIdSuccessfully() {
        final UUID vetId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        Vet vet = VetTestFactory.createVet(vetId, userId);
        when(vetRepository.findById(vetId)).thenReturn(Optional.of(vet));

        Vet result = vetService.getById(vetId);

        assertEquals(vet, result);
        verify(vetRepository).findById(vetId);
    }

    @Test
    @DisplayName("Throws EntityNotFoundException when vet not found by ID")
    void getByIdThrowsEntityNotFoundException() {
        var vetId = UUID.randomUUID();
        when(vetRepository.findById(vetId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vetService.getById(vetId));
        verify(vetRepository).findById(vetId);
    }

    @Test
    @DisplayName("Paged list of vets successfully")
    void getAllSuccessfully() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        var vets = VetTestFactory.createVetList(3);
        Page<Vet> page = new PageImpl<>(vets);
        when(vetRepository.findAll(pageRequest)).thenReturn(page);

        Page<Vet> result = vetService.getAll(pageRequest);

        assertEquals(3, result.getTotalElements());
        assertEquals("email" + vets.get(0).getUser().getId() + "@example.com", result.getContent().get(0).getUser().getEmail());
        assertEquals("email" + vets.get(1).getUser().getId() + "@example.com", result.getContent().get(1).getUser().getEmail());
        assertEquals("email" + vets.get(2).getUser().getId() + "@example.com", result.getContent().get(2).getUser().getEmail());

        assertEquals("firstName" + vets.get(0).getUser().getId(),  result.getContent().get(0).getUser().getFirstName());
        assertEquals("firstName" + vets.get(1).getUser().getId(), result.getContent().get(1).getUser().getFirstName());
        assertEquals("firstName" + vets.get(2).getUser().getId(), result.getContent().get(2).getUser().getFirstName());

        assertEquals(vets.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(vets.get(1).getId(), result.getContent().get(1).getId());
        assertEquals(vets.get(2).getId(), result.getContent().get(2).getId());

        verify(vetRepository).findAll(pageRequest);
    }

    @DisplayName("Deletion by id")
    void deleteByIdSuccessfully() {
        var id = UUID.randomUUID();
        when(vetRepository.existsById(id)).thenReturn(true);

        vetRepository.deleteById(id);

        verify(vetRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deletion when owner does not exist")
    void deleteByIdDoesNotExist() {
        var id = UUID.randomUUID();
        when(vetRepository.existsById(id)).thenReturn(false);

        vetService.delete(id);

        verify(vetRepository, never()).deleteById(any(UUID.class));
    }
}
