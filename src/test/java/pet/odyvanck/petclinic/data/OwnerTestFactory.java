package pet.odyvanck.petclinic.data;


import pet.odyvanck.petclinic.domain.Owner;
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
                "firstName",
                "lastName",
                "StrongPass123",
                "+1234567890",
                "email@example.com",
                "123 Main St"
        );
    }

    public static Owner createOwner(UUID id, UUID userId) {
        return Owner.builder()
                .id(id)
                .user(UserTestFactory.createUser(userId))
                .phone("+1234567890")
                .address("address 123 Main St")
                .createdAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC))
                .updatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2))
                .build();
    }

    public static Owner createOwnerWithoutIdAndUser() {
        return Owner.builder()
                .phone("+1234567890")
                .address("address 123 Main St")
                .createdAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC))
                .updatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2))
                .build();
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
                "email@example.com",
                req.address(),
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2),
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)
        );
    }

    public static OwnerResponse createOwnerResponse(UUID id, UUID userId) {
        return new OwnerResponse(
                id,
                userId,
                "firstName",
                "lastName",
                "+1234567890",
                "email@example.com",
                "address 123 Main St",
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
            o.setAddress("address Street " + i);
            owners.add(o);
        }
        return owners;
    }

    public static List<Owner> createOwnerListWithoutIdAndUser(int count) {
        List<Owner> owners = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Owner o = createOwnerForListWithoutIdAndUser(i);
            o.setPhone("+100000000" + i);
            o.setAddress("address Street " + i);
            owners.add(o);
        }
        return owners;
    }


    private static Owner createOwnerForList(UUID i, UUID userId) {
        return Owner.builder()
                .id(i)
                .user(UserTestFactory.createUser(userId))
                .phone("+100000000" + i)
                .address("address Street " + i)
                .createdAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC))
                .updatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2))
                .build();
    }

    private static Owner createOwnerForListWithoutIdAndUser(long i) {
        return Owner.builder()
                .phone("+100000000" + i)
                .address("address Street " + i)
                .createdAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC))
                .updatedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2))
                .build();
    }

    public static List<OwnerResponse> createOwnerResponseList(int count) {
        List<OwnerResponse> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(new OwnerResponse(
                    UserTestFactory.generateForLong(i),
                    UserTestFactory.generateForLong(i + 1),
                    "firstName" + i,
                    "lastName" + i,
                    "+100000000" + i,
                    "email" + i + "@example.com",
                    "address Street " + i,
                    LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC),
                    LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2)
            ));
        }
        return list;
    }
}
