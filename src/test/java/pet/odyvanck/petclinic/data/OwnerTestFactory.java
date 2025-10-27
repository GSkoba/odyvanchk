package pet.odyvanck.petclinic.data;


import pet.odyvanck.petclinic.domain.Owner;
import pet.odyvanck.petclinic.domain.User;
import pet.odyvanck.petclinic.web.dto.owner.OwnerCreationRequest;
import pet.odyvanck.petclinic.web.dto.owner.OwnerResponse;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

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

    public static User createUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPasswordHash("StrongPassHash");
        return user;
    }

    public static Owner createOwner(Long id, Long userId) {
        Owner owner = new Owner();
        owner.setId(id);
        owner.setUser(createUser(userId));
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

    public static User createUserWithoutId() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPasswordHash("StrongPassHash");
        return user;
    }


    public static OwnerResponse createOwnerResponse(Long id, Long userId) {
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
            Owner o = createOwnerForList((long) i, (long) i + 1);
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

    public static List<User> createUserListWithoutId(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            User u = new User();
            u.setEmail("example" + i +"@gmail.com");
            u.setFirstName("John" + i);
            u.setLastName("Doe" + i);
            u.setPasswordHash("StrongPassHash" + i);
            users.add(u);
        }
        return users;
    }

    private static Owner createOwnerForList(long i, long userId) {
        Owner owner = new Owner();
        owner.setId(i);
        owner.setUser(createUserForList(userId));
        owner.setPhone("+100000000" + i);
        owner.setAddress("Street " + i);
        LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2);
        return owner;
    }

    private static Owner createOwnerForListWithoutIdAndUser(long i) {
        Owner owner = new Owner();
        owner.setPhone("+100000000" + i);
        owner.setAddress("Street " + i);
        LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(2);
        return owner;
    }

    private static User createUserForList(long i) {
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
                    (long) i,
                    (long) (1 + i),
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
