package pet.odyvanck.petclinic.data;

import pet.odyvanck.petclinic.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserTestFactory {

    public static User createUserWithoutId() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPasswordHash("StrongPassHash");
        return user;
    }

    public static User createUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("john" + id + "@example.com");
        user.setFirstName("John" + id);
        user.setLastName("Doe" + id);
        user.setPasswordHash("StrongPassHash");
        return user;
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
}
