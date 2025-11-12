import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class UserDatabase {
    public static List<User> users = new ArrayList<>();

    static {
        loadUsersFromDatabase();
    }

    private static void loadUsersFromDatabase() {
        try {
            users = DatabaseManager.loadUsers();
            // If no users in database, add defaults
            if (users.isEmpty()) {
                User admin = new User(1000001, "admin", "admin", Role.ADMIN, "Administrator", "admin@guidancepro.com");
                User counselor = new User(1000002, "counselor", "password", Role.COUNSELOR, "Counselor One", "counselor@guidancepro.com");
                User student = new User(1000003, "student", "password", Role.STUDENT, "Student One", "student@guidancepro.com", "2021001");
                users.add(admin);
                users.add(counselor);
                users.add(student);
                // Save defaults to database
                DatabaseManager.saveUser(admin);
                DatabaseManager.saveUser(counselor);
                DatabaseManager.saveUser(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback to default users if database fails
            users.add(new User(1000001, "admin", "admin", Role.ADMIN, "Administrator", "admin@guidancepro.com"));
            users.add(new User(1000002, "counselor", "password", Role.COUNSELOR, "Counselor One", "counselor@guidancepro.com"));
            users.add(new User(1000003, "student", "password", Role.STUDENT, "Student One", "student@guidancepro.com", "2021001"));
        }
    }

    public static void saveUser(User user) {
        try {
            DatabaseManager.saveUser(user);
            if (!users.contains(user)) {
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
