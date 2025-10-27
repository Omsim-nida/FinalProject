public class Student {
    private int id;
    private String name;
    private String email;
    private String studentId;

    public Student(int id, String name, String email, String studentId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.studentId = studentId;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    @Override
    public String toString() {
        return name + " (" + studentId + ")";
    }
}
