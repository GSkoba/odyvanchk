package pet.odyvanck.petclinic.data;

import pet.odyvanck.petclinic.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserTestFactory {

    public static User createUserWithoutId() {
        return User.builder()
                .email("email@example.com")
                .firstName("firstName")
                .lastName("lastName")
                .passwordHash("StrongPassHash")
                .build();
    }

    public static User createUser(UUID id) {
        return User.builder()
                .id(id)
                .email("email" + id + "@example.com")
                .firstName("firstName" + id)
                .lastName("lastName" + id)
                .passwordHash("StrongPassHash")
                .build();
    }

    public static List<User> createUserListWithoutId(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            User u = User.builder()
                    .email("email" + i + "@example.com")
                    .firstName("firstName" + i)
                    .lastName("lastName" + i)
                    .passwordHash("StrongPassHash" + i)
                    .build();
            users.add(u);
        }
        return users;
    }

    public static UUID generateForLong(long i) {
        return new UUID(0L, i);
    }

}
