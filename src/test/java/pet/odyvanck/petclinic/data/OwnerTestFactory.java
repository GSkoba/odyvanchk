package pet.odyvanck.petclinic.data;


import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.owner.OwnerCreationRequest;
import pet.odyvanck.petclinic.web.dto.owner.OwnerResponse;
import pet.odyvanck.petclinic.web.dto.owner.OwnerUpdateRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Factory class providing ready-to-use test objects for Owner-related tests.
 */
public final class OwnerTestFactory {

    private OwnerTestFactory() {
    }

    public static OwnerCreationRequest createOwnerCreationRequest() {
        return new OwnerCreationRequest(
                "John",
                "Doe",
                "StrongPass123",
                "+1234567890",
                "john@example.com",
                "123 Main St"
        );
    }

    public static OwnerCreationRequest createOwnerCreationRequest(String email) {
        return new OwnerCreationRequest(
                "Jane",
                "Smith",
                "StrongPass123",
                "+1987654321",
                email,
                "456 Elm Street"
        );
    }

    public static Owner createOwner(UUID id, UUID userId) {
        Owner owner = new Owner();
        owner.setId(id);
        owner.setUser(UserTestFactory.createUser(userId));
        owner.setPhone("+1234567890");
        owner.setAddress("123 Main St");
        owner.setCreatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC));
        owner.setUpdatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2));
        return owner;
    }

    public static Owner createOwnerWithoutIdAndUser() {
        Owner owner = new Owner();
        owner.setPhone("+1234567890");
        owner.setAddress("123 Main St");
        owner.setCreatedAt(LocalDateTime.now().minusDays(1));
        owner.setUpdatedAt(LocalDateTime.now());
        return owner;
    }

    public static OwnerUpdateRequest createOwnerUpdateRequest() {
        return new OwnerUpdateRequest(
             "updatedFirstName",
             "updatedLastName",
             "+9876533",
             "updated address"
        );
    }
    public static OwnerResponse createUpdatedOwnerResponse(UUID id, UUID userId, OwnerUpdateRequest req) {
        return new OwnerResponse(
                id,
                userId,
                req.firstName(),
                req.lastName(),
                req.phone(),
                "john@example.com",
                req.address(),
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2),
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)
        );
    }

    public static OwnerResponse createOwnerResponse(UUID id, UUID userId) {
        return new OwnerResponse(
                id,
                userId,
                "John",
                "Doe",
                "+1234567890",
                "john@example.com",
                "123 Main St",
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2),
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)
        );
    }

    public static List<Owner> createOwnerList(int count) {
        List<Owner> owners = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Owner o = createOwnerForList(
                    UserTestFactory.generateForLong(i),
                    UserTestFactory.generateForLong(i + 1)
            );
            o.setPhone("+100000000" + i);
            o.setAddress("Street " + i);
            owners.add(o);
        }
        return owners;
    }

    public static List<Owner> createOwnerListWithoutIdAndUser(int count) {
        List<Owner> owners = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Owner o = createOwnerForListWithoutIdAndUser(i);
            o.setPhone("+100000000" + i);
            o.setAddress("Street " + i);
            owners.add(o);
        }
        return owners;
    }



    private static Owner createOwnerForList(UUID i, UUID userId) {
        Owner owner = new Owner();
        owner.setId(i);
        owner.setUser(createUserForList(userId));
        owner.setPhone("+100000000" + i);
        owner.setAddress("Street " + i);
        owner.setCreatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC));
        owner.setUpdatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2));
        return owner;
    }

    private static Owner createOwnerForListWithoutIdAndUser(long i) {
        Owner owner = new Owner();
        owner.setPhone("+100000000" + i);
        owner.setAddress("Street " + i);
        owner.setCreatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC));
        owner.setUpdatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2));
        return owner;
    }

    private static User createUserForList(UUID i) {
        User user = new User();
        user.setId(i);
        user.setEmail("owner" + i + "@example.com");
        user.setFirstName("Owner" + i);
        user.setLastName("Last" + i);
        user.setPasswordHash("StrongPassHash");
        return user;
    }

    public static List<OwnerResponse> createOwnerResponseList(int count) {
        List<OwnerResponse> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(new OwnerResponse(
                    UserTestFactory.generateForLong(i),
                    UserTestFactory.generateForLong(i + 1),
                    "Owner" + i,
                    "Last" + i,
                    "+100000000" + i,
                    "owner" + i + "@example.com",
                    "Street " + i,
                    LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC),
                    LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2)
            ));
        }
        return list;
    }
}
