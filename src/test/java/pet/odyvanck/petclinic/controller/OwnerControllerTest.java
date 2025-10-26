package pet.odyvanck.petclinic.controller;

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
import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.error.EntityAlreadyExistsException;
import pet.odyvanck.petclinic.service.OwnerService;
import pet.odyvanck.petclinic.web.controller.GlobalExceptionHandler;
import pet.odyvanck.petclinic.web.controller.OwnerController;
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
    @DisplayName("Create owner successfully")
    void registerSuccessfully() throws Exception {
        var request = OwnerTestFactory.createOwnerCreationRequest();
        var owner = OwnerTestFactory.createOwner(1L, 10L);
        var response = OwnerTestFactory.createOwnerResponse(1L, 10L);

        given(ownerMapper.toUser(request)).willReturn(owner.getUser());
        given(ownerMapper.toOwner(request)).willReturn(owner);
        given(ownerService.register(owner, owner.getUser(), request.password())).willReturn(owner);
        given(ownerMapper.toDto(owner.getUser(), owner)).willReturn(response);

        mockMvc.perform(post("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/owners/1"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(ownerService).register(owner, owner.getUser(), "StrongPass123");
    }

    @Test
    @DisplayName("Failed validation for phone and email")
    void registerValidationError() throws Exception {
        var invalid = new OwnerCreationRequest(
                "first", "last", "123", "bad_phone", "not-an-email", "address"
        );

        mockMvc.perform(post("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation errors in request data"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details.phone").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.details.email").exists());
    }


    @Test
    @DisplayName("Return paginated owners")
    void getAllOwnersSuccessfully() throws Exception {
        var owners = OwnerTestFactory.createOwnerList(2);
        var responses = OwnerTestFactory.createOwnerResponseList(2);
        Page<Owner> page = new PageImpl<>(owners, PageRequest.of(0, 10), 2);

        given(ownerService.getAll(any(PageRequest.class), any())).willReturn(page);
        given(ownerMapper.toDto(owners)).willReturn(responses);

        mockMvc.perform(get("/api/v1/owners")
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
    @DisplayName("Empty list when no owners found")
    void getAllOwnersEmpty() throws Exception {
        Page<Owner> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        given(ownerService.getAll(any(PageRequest.class), any())).willReturn(emptyPage);

        mockMvc.perform(get("/api/v1/owners")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elements").isEmpty());
    }


    @Test
    @DisplayName("Transform sort field 'email' to 'user.email'")
    void getAllTransformsSortFieldEmailToUserEmail() throws Exception {
        Page<Owner> page = new PageImpl<>(OwnerTestFactory.createOwnerList(1));
        given(ownerService.getAll(any(PageRequest.class), any())).willReturn(page);

        mockMvc.perform(get("/api/v1/owners")
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
    @DisplayName("Invalid sorted field")
    void getAllInvalidSortField() throws Exception {
        mockMvc.perform(get("/api/v1/owners")
                        .param("sortBy", "invalidField"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Registration with already existing email in the system")
    void registerDuplicateEmail() throws Exception {
        var request = OwnerTestFactory.createOwnerCreationRequest();
        var owner = OwnerTestFactory.createOwnerWithoutIdAndUser();
        var user = OwnerTestFactory.createUserWithoutId();

        given(ownerMapper.toUser(request)).willReturn(user);
        given(ownerMapper.toOwner(request)).willReturn(owner);
        given(ownerService.register(owner, user, request.password()))
                .willThrow(new EntityAlreadyExistsException("User", "email", user.getEmail()));

        mockMvc.perform(post("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(String.format("User with email '%s' already exists", request.email())));

        verify(ownerService).register(any(Owner.class), any(User.class), eq(request.password()));
        verify(ownerMapper, never()).toDto(any(Owner.class));
    }

}
