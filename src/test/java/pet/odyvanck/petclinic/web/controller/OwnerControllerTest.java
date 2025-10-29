package pet.odyvanck.petclinic.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pet.odyvanck.petclinic.data.OwnerTestFactory;
import pet.odyvanck.petclinic.data.UserTestFactory;
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.error.EntityAlreadyExistsException;
import pet.odyvanck.petclinic.service.OwnerService;
import pet.odyvanck.petclinic.web.dto.owner.*;
import pet.odyvanck.petclinic.web.mapper.OwnerMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OwnerControllerTest {

    private MockMvc mockMvc;
    private OwnerService ownerService;
    private OwnerMapper ownerMapper;
    private ObjectMapper objectMapper;
    private static final String BASE_URI = "/api/v1/owners";

    @BeforeEach
    void setup() {
        ownerService = mock(OwnerService.class);
        ownerMapper = mock(OwnerMapper.class);
        objectMapper = new ObjectMapper();

        var controller = new OwnerController(ownerService, ownerMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/owners → should create owner successfully")
    void registerSuccessfully() throws Exception {
        var request = OwnerTestFactory.createOwnerCreationRequest();
        var owner = OwnerTestFactory.createOwner(1L, 10L);
        var response = OwnerTestFactory.createOwnerResponse(1L, 10L);

        given(ownerMapper.toUser(request)).willReturn(owner.getUser());
        given(ownerMapper.toOwner(request)).willReturn(owner);
        given(ownerService.register(owner, owner.getUser(), request.password())).willReturn(owner);
        given(ownerMapper.toDto(owner)).willReturn(response);

        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/owners/1"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(ownerService).register(owner, owner.getUser(), "StrongPass123");
    }

    @Test
    @DisplayName("POST /api/v1/owners → should fail validation for phone and email")
    void registerValidationError() throws Exception {
        var invalid = new OwnerCreationRequest(
                "first", "last", "123", "bad_phone", "not-an-email", "address"
        );

        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors in request data"))
                .andExpect(jsonPath("$.details.phone").exists())
                .andExpect(jsonPath("$.details.email").exists());
    }

    @Test
    @DisplayName("GET /api/v1/owners → should return paginated list of owners")
    void getAllOwnersSuccessfully() throws Exception {
        var owners = OwnerTestFactory.createOwnerList(2);
        var responses = OwnerTestFactory.createOwnerResponseList(2);
        Page<Owner> page = new PageImpl<>(owners, PageRequest.of(0, 10), 2);

        given(ownerService.getAll(any(PageRequest.class), any())).willReturn(page);
        given(ownerMapper.toDto(owners)).willReturn(responses);

        mockMvc.perform(get(BASE_URI)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "firstName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements.length()").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.totalPages").value(1))

                .andExpect(jsonPath("$.totalElements").value(2))

                .andExpect(jsonPath("$.elements[0].firstName").value("Owner1"))
                .andExpect(jsonPath("$.elements[1].email").value("owner2@example.com"));
    }

    @Test
    @DisplayName("GET /api/v1/owners → should return empty list when no owners exist")
    void getAllOwnersEmpty() throws Exception {
        Page<Owner> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        given(ownerService.getAll(any(PageRequest.class), any())).willReturn(emptyPage);

        mockMvc.perform(get(BASE_URI)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements").isEmpty());
    }

    @Test
    @DisplayName("GET /api/v1/owners → should transform sort field 'email' to 'user.email'")
    void getAllTransformsSortFieldEmailToUserEmail() throws Exception {
        Page<Owner> page = new PageImpl<>(OwnerTestFactory.createOwnerList(1));
        given(ownerService.getAll(any(PageRequest.class), any())).willReturn(page);

        mockMvc.perform(get(BASE_URI)
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "email"))
                .andExpect(status().isOk());

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(ownerService).getAll(captor.capture(), any());

        PageRequest actualPageRequest = captor.getValue();

        // Verify sorting transformation
        Sort.Order order = actualPageRequest.getSort().getOrderFor("user.email");
        assertThat(order)
                .withFailMessage("Expected sort to contain 'user.email' but got: %s", actualPageRequest.getSort())
                .isNotNull();
    }

    @Test
    @DisplayName("GET /api/v1/owners → should fail when invalid sort field is provided")
    void getAllInvalidSortField() throws Exception {
        mockMvc.perform(get(BASE_URI)
                        .param("sortBy", "invalidField"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /api/v1/owners → should fail when email already exists")
    void registerDuplicateEmail() throws Exception {
        var request = OwnerTestFactory.createOwnerCreationRequest();
        var owner = OwnerTestFactory.createOwnerWithoutIdAndUser();
        var user = UserTestFactory.createUserWithoutId();

        given(ownerMapper.toUser(request)).willReturn(user);
        given(ownerMapper.toOwner(request)).willReturn(owner);
        given(ownerService.register(owner, user, request.password()))
                .willThrow(new EntityAlreadyExistsException("User", "email", user.getEmail()));

        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(String.format("User with email '%s' already exists", request.email())));
    }

    @Test
    @DisplayName("GET /api/v1/owners/{id} → should return owner by ID")
    void getByIdSuccessfully() throws Exception {
        final Long id = 1L;
        Owner owner = OwnerTestFactory.createOwner(id, 2L);
        OwnerResponse response = OwnerTestFactory.createOwnerResponse(id, 2L);

        given(ownerService.getById(id)).willReturn(owner);
        given(ownerMapper.toDto(owner)).willReturn(response);

        mockMvc.perform(get(BASE_URI + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(response.email()))
                .andExpect(jsonPath("$.lastName").value(response.lastName()))
                .andExpect(jsonPath("$.phone").value(response.phone()))
                .andExpect(jsonPath("$.address").value(response.address()))
                .andExpect(jsonPath("$.firstName").value(response.firstName()));
    }

    @Test
    @DisplayName("PUT /api/v1/owners/{id} → should update and return updated owner")
    void updateSuccessfully() throws Exception {
        final Long id = 1L;
        OwnerUpdateRequest updateRequest = OwnerTestFactory.createOwnerUpdateRequest();
        Owner owner = OwnerTestFactory.createOwner(id, 2L);
        OwnerResponse updatedResponse = OwnerTestFactory.createUpdatedOwnerResponse(id, 2L, updateRequest);

        given(ownerService.getById(id)).willReturn(owner);
        doNothing().when(ownerMapper).updateOwnerFromRequest(any(), any());
        given(ownerService.update(any(Long.class), any(OwnerUpdateRequest.class))).willReturn(owner);
        given(ownerMapper.toDto(any(Owner.class))).willReturn(updatedResponse);

        mockMvc.perform(put(BASE_URI + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(updatedResponse.email()))
                .andExpect(jsonPath("$.lastName").value(updatedResponse.lastName()))
                .andExpect(jsonPath("$.phone").value(updatedResponse.phone()))
                .andExpect(jsonPath("$.address").value(updatedResponse.address()))
                .andExpect(jsonPath("$.firstName").value(updatedResponse.firstName()));
    }

    @Test
    @DisplayName("DELETE /api/v1/owners/{id} - should delete and return 204")
    void deleteReturnsNoContent() throws Exception {
        final Long id = 1L;

        doNothing().when(ownerService).deleteById(id);

        mockMvc.perform(delete(BASE_URI + "/" + id))
                .andExpect(status().isNoContent());
    }
}
