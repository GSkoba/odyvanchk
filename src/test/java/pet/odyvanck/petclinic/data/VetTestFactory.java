package pet.odyvanck.petclinic.data;

import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.web.dto.vet.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static pet.odyvanck.petclinic.data.UserTestFactory.createUser;

public class VetTestFactory {

    public static Vet createVet(Long id, Long userId) {
        User user = createUser(userId);
        Vet vet = new Vet();
        vet.setId(id);
        vet.setUser(user);
        vet.setPhone("+555000" + id);
        vet.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));
        vet.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        return vet;
    }

    public static Vet createVetWithoutIdAndUser() {
        Vet vet = new Vet();
        vet.setPhone("+555000");
        vet.setCreatedAt(LocalDateTime.now().minusDays(1));
        vet.setUpdatedAt(LocalDateTime.now());
        return vet;
    }

    public static List<Vet> createVetList(int count) {
        List<Vet> vets = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            vets.add(createVet(i, i + 100));
        }
        return vets;
    }

    public static List<Vet> createVetListWithoutUserAndId(int count) {
        List<Vet> vets = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            vets.add(createVetWithoutIdAndUser());
        }
        return vets;
    }


    public static VetCreationRequest createVetCreationRequest() {
        return new VetCreationRequest(
                "John",
                "Doe",
                "StrongPass123",
                "+1234567890",
                "john@example.com"
        );
    }

    public static VetUpdateRequest createVetUpdateRequest() {
        return new VetUpdateRequest(
                "updatedFirstName",
                "updatedLastName",
                "+9876533"
        );
    }

    public static VetResponse createVetResponse(Long vetId, Long userId) {
        return new VetResponse(
                vetId,
                userId,
                "John",
                "Doe",
                "+1234567890",
                "john@example.com",
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2),
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)
        );
    }

    public static VetResponse createUpdatedVetResponse(Long id, Long userId, VetUpdateRequest req) {
        return new VetResponse(
                id,
                userId,
                req.firstName(),
                req.lastName(),
                req.phone(),
                "john@example.com",
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2),
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)
        );
    }

    public static List<VetResponse> createVetResponseList(int count) {
        List<VetResponse> responses = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            responses.add(createVetResponse(i, i + 100));
        }
        return responses;
    }
}

