package pet.odyvanck.petclinic.data;

import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.domain.Vet;
import pet.odyvanck.petclinic.web.dto.vet.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static pet.odyvanck.petclinic.data.UserTestFactory.createUser;

public class VetTestFactory {

    public static Vet createVet(UUID id, UUID userId) {
        User user = createUser(userId);
        return Vet.builder()
                .id(id)
                .user(user)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now())
                .phone("+1234567890")
                .build();
    }

    public static Vet createVetWithoutIdAndUser() {
        return Vet.builder()
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now())
                .phone("+1234567890")
                .build();
    }

    public static List<Vet> createVetList(int count) {
        List<Vet> vets = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            vets.add(createVet(
                    UserTestFactory.generateForLong(i),
                    UserTestFactory.generateForLong(i + 100)
            ));
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
                "firstName",
                "lastName",
                "StrongPass123",
                "+1234567890",
                "email@example.com"
        );
    }

    public static VetUpdateRequest createVetUpdateRequest() {
        return new VetUpdateRequest(
                "updatedFirstName",
                "updatedLastName",
                "+9876533"
        );
    }

    public static VetResponse createVetResponse(UUID vetId, UUID userId) {
        return new VetResponse(
                vetId,
                userId,
                "firstName",
                "lastName",
                "+1234567890",
                "email@example.com",
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2),
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)
        );
    }

    public static VetResponse createUpdatedVetResponse(UUID id, UUID userId, VetUpdateRequest req) {
        return new VetResponse(
                id,
                userId,
                req.firstName(),
                req.lastName(),
                req.phone(),
                "email@example.com",
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2),
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)
        );
    }

    public static List<VetResponse> createVetResponseList(int count) {
        List<VetResponse> responses = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            responses.add(createVetResponse(
                    UserTestFactory.generateForLong(i),
                    UserTestFactory.generateForLong(i + 100)
            ));
        }
        return responses;
    }
}

