package pet.odyvanck.petclinic.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pet.odyvanck.petclinic.data.UserTestFactory;
import pet.odyvanck.petclinic.data.VetTestFactory;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.domain.error.EntityAlreadyExistsException;
import pet.odyvanck.petclinic.service.VetService;
import pet.odyvanck.petclinic.web.dto.vet.*;
import pet.odyvanck.petclinic.web.mapper.VetMapper;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VetControllerTest {

    private MockMvc mockMvc;
    private VetService vetService;
    private VetMapper vetMapper;
    private ObjectMapper objectMapper;

    private static final String BASE_URI = "/api/v1/vets";

    @BeforeEach
    void setup() {
        vetService = mock(VetService.class);
        vetMapper = mock(VetMapper.class);
        objectMapper = new ObjectMapper();

        var controller = new VetController(vetService, vetMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/vets → should create vet successfully")
    void createVetSuccessfully() throws Exception {
        var vetId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var request = VetTestFactory.createVetCreationRequest();
        var vet = VetTestFactory.createVetWithoutIdAndUser();
        var user = UserTestFactory.createUserWithoutId();
        var createdVet = VetTestFactory.createVet(vetId, userId);
        var response = VetTestFactory.createVetResponse(vetId, userId);

        given(vetMapper.toVet(request)).willReturn(vet);
        given(vetMapper.toUser(request)).willReturn(user);
        given(vetService.create(vet, user, request.password())).willReturn(createdVet);
        given(vetMapper.toDto(createdVet)).willReturn(response);

        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/vets/" + vetId))
                .andExpect(jsonPath("$.firstName").value(response.firstName()))
                .andExpect(jsonPath("$.lastName").value(response.lastName()))
                .andExpect(jsonPath("$.phone").value(response.phone()))
                .andExpect(jsonPath("$.userId").value(response.userId().toString()))
                .andExpect(jsonPath("$.email").value(response.email()));

        verify(vetService).create(vet, user, request.password());
    }

    @Test
    @DisplayName("POST /api/v1/vets → should fail when email already exists")
    void createVetDuplicateEmail() throws Exception {
        var request = VetTestFactory.createVetCreationRequest();
        var vet = VetTestFactory.createVetWithoutIdAndUser();
        var user = UserTestFactory.createUserWithoutId();

        given(vetMapper.toVet(request)).willReturn(vet);
        given(vetMapper.toUser(request)).willReturn(user);
        given(vetService.create(vet, user, request.password()))
                .willThrow(new EntityAlreadyExistsException("Vet", "email", request.email()));

        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        String.format("Vet with email '%s' already exists", request.email())
                ));

        verify(vetService).create(any(Vet.class), any(User.class), eq(request.password()));
    }

    @Test
    @DisplayName("GET /api/v1/vets → should return paginated list of vets")
    void getAllVetsSuccessfully() throws Exception {
        var vets = VetTestFactory.createVetList(2);
        var responses = VetTestFactory.createVetResponseList(2);
        Page<Vet> page = new PageImpl<>(vets, PageRequest.of(0, 10), 2);

        given(vetService.getAll(any(PageRequest.class))).willReturn(page);
        given(vetMapper.toDto(vets)).willReturn(responses);

        mockMvc.perform(get(BASE_URI)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "firstName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements.length()").value(2))
                .andExpect(jsonPath("$.elements[0].email").value(responses.get(0).email()))
                .andExpect(jsonPath("$.elements[1].email").value(responses.get(1).email()));
    }

    @Test
    @DisplayName("GET /api/v1/vets → should return empty list when no vets found")
    void getAllVetsEmpty() throws Exception {
        Page<Vet> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        given(vetService.getAll(any(PageRequest.class))).willReturn(emptyPage);

        mockMvc.perform(get(BASE_URI)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements").isEmpty());
    }

    @Test
    @DisplayName("GET /api/v1/vets/{id} → should return vet by ID")
    void getVetByIdSuccessfully() throws Exception {
        final UUID id = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();

        var vet = VetTestFactory.createVet(id, userId);
        var response = VetTestFactory.createVetResponse(id, userId);

        given(vetService.getById(id)).willReturn(vet);
        given(vetMapper.toDto(vet)).willReturn(response);

        mockMvc.perform(get(BASE_URI + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(response.email()))
                .andExpect(jsonPath("$.firstName").value(response.firstName()))
                .andExpect(jsonPath("$.lastName").value(response.lastName()));
    }

    @Test
    @DisplayName("PUT /api/v1/vets/{id} → should update and return updated vet")
    void updateVetSuccessfully() throws Exception {
        final UUID id = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        var request = VetTestFactory.createVetUpdateRequest();
        var vet = VetTestFactory.createVet(id, userId);
        var response = VetTestFactory.createUpdatedVetResponse(id, userId, request);

        given(vetService.update(eq(id), any(VetUpdateRequest.class))).willReturn(vet);
        given(vetMapper.toDto(vet)).willReturn(response);

        mockMvc.perform(put(BASE_URI + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(response.email()))
                .andExpect(jsonPath("$.firstName").value(response.firstName()))
                .andExpect(jsonPath("$.lastName").value(response.lastName()))
                .andExpect(jsonPath("$.phone").value(response.phone()));
    }

    @Test
    @DisplayName("DELETE /api/v1/vets/{id} → should delete vet and return 204")
    void deleteVetSuccessfully() throws Exception {
        final UUID id = UUID.randomUUID();
        doNothing().when(vetService).delete(id);

        mockMvc.perform(delete(BASE_URI + "/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/vets → should transform sort field 'email' correctly")
    void getAllTransformsSortFieldEmail() throws Exception {
        Page<Vet> page = new PageImpl<>(VetTestFactory.createVetList(1));
        given(vetService.getAll(any(PageRequest.class))).willReturn(page);

        mockMvc.perform(get(BASE_URI)
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "email"))
                .andExpect(status().isOk());

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(vetService).getAll(captor.capture());
        PageRequest actual = captor.getValue();

        Sort.Order order = actual.getSort().getOrderFor("email");
        assertThat(order)
                .withFailMessage("Expected sort to include 'email' but got: %s", actual.getSort())
                .isNotNull();
    }
}
