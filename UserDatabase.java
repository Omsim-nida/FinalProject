import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    public static List<User> users = new ArrayList<>();

    static {
        // Initialize with default users
        users.add(new User(1000001, "admin", "admin", Role.ADMIN, "Administrator", "admin@guidancepro.com"));
        users.add(new User(1000002, "counselor", "password", Role.COUNSELOR, "Counselor One", "counselor@guidancepro.com"));
        users.add(new User(1000003, "student", "password", Role.STUDENT, "Student One", "student@guidancepro.com", "2021001"));
    }
}
