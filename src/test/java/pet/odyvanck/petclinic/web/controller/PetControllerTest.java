package pet.odyvanck.petclinic.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pet.odyvanck.petclinic.data.PetTestFactory;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.Pet;
import pet.odyvanck.petclinic.domain.PetType;
import pet.odyvanck.petclinic.service.PetService;
import pet.odyvanck.petclinic.web.dto.PageResponse;
import pet.odyvanck.petclinic.web.dto.pet.*;
import pet.odyvanck.petclinic.web.mapper.PetMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PetControllerTest {

    private static final String BASE_URI = "/api/v1/pets";

    private MockMvc mockMvc;
    private PetService petService;
    private PetMapper petMapper;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        petService = mock(PetService.class);
        petMapper = mock(PetMapper.class);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        PetController controller = new PetController(petService, petMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/pets → should create pet successfully")
    void createSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        PetCreationRequest request = new PetCreationRequest(
                "name",
                LocalDate.of(2020, 1, 1),
                PetType.DOG,
                ownerId,
                "breed",
                "color",
                new BigDecimal("10.0")
        );

        Owner owner = Owner.builder().id(ownerId).build();
        Pet pet = PetTestFactory.createPetWithoutId(owner);
        Pet created = pet.toBuilder().id(id).build();
        PetResponse response = PetTestFactory.createPetResponse(id, ownerId);

        given(petMapper.toEntity(request)).willReturn(pet);
        given(petService.create(pet)).willReturn(created);
        given(petMapper.toDto(created)).willReturn(response);

        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BASE_URI + "/" + id))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.type").value("DOG"));

        verify(petService).create(pet);
    }

    @Test
    @DisplayName("PUT /api/v1/pets/{id} → should update pet successfully")
    void updateSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        PetUpdateRequest request = new PetUpdateRequest(
                "updatedName",
                LocalDate.of(2021, 1, 1),
                PetType.CAT,
                "updatedBreed",
                "updatedColor",
                new BigDecimal("5.5")
        );

        Owner owner = Owner.builder().id(ownerId).build();
        Pet updatedPet = PetTestFactory.createPetWithId(owner).toBuilder()
                .name("updatedName")
                .breed("updatedBreed")
                .color("updatedColor")
                .type(PetType.CAT)
                .build();

        PetResponse response = PetTestFactory.createPetResponse(id, ownerId);

        given(petService.update(id, request)).willReturn(updatedPet);
        given(petMapper.toDto(updatedPet)).willReturn(response);

        mockMvc.perform(put(BASE_URI + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.type").value("DOG")); // because PetResponse stub uses DOG in factory

        verify(petService).update(id, request);
    }

    @Test
    @DisplayName("GET /api/v1/pets/{id} → should return pet successfully")
    void getByIdSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Pet pet = PetTestFactory.createPetWithId(Owner.builder().id(ownerId).build());
        PetResponse response = PetTestFactory.createPetResponse(id, ownerId);

        given(petService.getById(id)).willReturn(pet);
        given(petMapper.toDto(pet)).willReturn(response);

        mockMvc.perform(get(BASE_URI + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.type").value("DOG"));

        verify(petService).getById(id);
    }

    @Test
    @DisplayName("GET /api/v1/pets → should return paginated pets successfully")
    void getAllSuccessfully() throws Exception {
        UUID ownerId = UUID.randomUUID();

        Pet pet1 = PetTestFactory.createPetWithId(Owner.builder().id(ownerId).build());
        Pet pet2 = PetTestFactory.createPetWithId(Owner.builder().id(ownerId).build());
        List<Pet> pets = List.of(pet1, pet2);
        PetResponse response1 = PetTestFactory.createPetResponse(UUID.randomUUID(), ownerId);
        PetResponse response2 = PetTestFactory.createPetResponse(UUID.randomUUID(), ownerId);
        List<PetResponse> responses = List.of(response1, response2);

        var page = new PageImpl<>(pets, PageRequest.of(0, 10), 2);
        var pageResponse = PageResponse.from(page, pet -> responses);

        given(petService.getAll(any(PageRequest.class), any(PetRequestParams.class))).willReturn(page);
        given(petMapper.toDto(pets)).willReturn(responses);

        mockMvc.perform(get(BASE_URI)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements").isArray())
                .andExpect(jsonPath("$.elements.length()").value(2))
                .andExpect(jsonPath("$.elements[0].id").value(responses.get(0).id().toString()))
                .andExpect(jsonPath("$.elements[1].id").value(responses.get(1).id().toString()));

        verify(petService).getAll(any(PageRequest.class), any(PetRequestParams.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/pets/{id} → should delete pet successfully")
    void deleteSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(petService).deleteById(id);

        mockMvc.perform(delete(BASE_URI + "/" + id))
                .andExpect(status().isNoContent());

        verify(petService).deleteById(id);
    }
}

