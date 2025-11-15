public class User {
    private int id;
    private String username;
    private String password;
    private Role role;
    private String name;
    private String email;
    private String studentId;
    private UserStatus status;

    public User(int id, String username, String password, Role role, String name, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
        this.studentId = null; // Default to null for non-students
        this.status = UserStatus.PENDING; // Default status for new users
    }

    public User(int id, String username, String password, Role role, String name, String email, String studentId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.status = UserStatus.PENDING; // Default status for new users
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    @Override
    public String toString() {
        return name + " (" + username + " - " + role + ")";
    }
}
