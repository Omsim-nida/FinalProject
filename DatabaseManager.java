import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:guidancepro.db";
    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables(); 
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables() throws SQLException {
        Statement stmt = connection.createStatement();

        // Users table
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL,
                name TEXT NOT NULL,
                email TEXT NOT NULL,
                studentId TEXT,
                status TEXT NOT NULL DEFAULT 'PENDING'
            )
        """);

        // Students table
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS students (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                email TEXT NOT NULL,
                studentId TEXT NOT NULL UNIQUE
            )
        """);

        // Appointments table
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS appointments (
                id INTEGER PRIMARY KEY,
                studentId INTEGER NOT NULL,
                counselor TEXT NOT NULL,
                dateTime TEXT NOT NULL,
                status TEXT NOT NULL,
                notes TEXT,
                FOREIGN KEY (studentId) REFERENCES students(id)
            )
        """);

        // Cases table
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS cases (
                id INTEGER PRIMARY KEY,
                studentId INTEGER NOT NULL,
                description TEXT NOT NULL,
                status TEXT NOT NULL,
                wellnessMetrics TEXT,
                followUpNotes TEXT,
                FOREIGN KEY (studentId) REFERENCES students(id)
            )
        """);

        // Workshops table
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS workshops (
                id INTEGER PRIMARY KEY,
                title TEXT NOT NULL,
                dateTime TEXT NOT NULL,
                description TEXT,
                maxParticipants INTEGER NOT NULL
            )
        """);

        // Workshop participants table (many-to-many)
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS workshop_participants (
                workshopId INTEGER NOT NULL,
                studentId INTEGER NOT NULL,
                PRIMARY KEY (workshopId, studentId),
                FOREIGN KEY (workshopId) REFERENCES workshops(id),
                FOREIGN KEY (studentId) REFERENCES students(id)
            )
        """);

        stmt.close();
    }

    // User CRUD
    public static void saveUser(User user) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO users (id, username, password, role, name, email, studentId, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );
        stmt.setInt(1, user.getId());
        stmt.setString(2, user.getUsername());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getRole().toString());
        stmt.setString(5, user.getName());
        stmt.setString(6, user.getEmail());
        stmt.setString(7, user.getStudentId());
        stmt.setString(8, user.getStatus().toString());
        stmt.executeUpdate();
        stmt.close();
    }

    public static List<User> loadUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        while (rs.next()) {
            Role role = Role.valueOf(rs.getString("role"));
            User user = new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                role,
                rs.getString("name"),
                rs.getString("email")
            );
            if (rs.getString("studentId") != null) {
                user.setStudentId(rs.getString("studentId"));
            }
            if (rs.getString("status") != null) {
                user.setStatus(UserStatus.valueOf(rs.getString("status")));
            }
            users.add(user);
        }
        rs.close();
        stmt.close();
        return users;
    }

    // Student CRUD
    public static void saveStudent(Student student) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO students (id, name, email, studentId) VALUES (?, ?, ?, ?)"
        );
        stmt.setInt(1, student.getId());
        stmt.setString(2, student.getName());
        stmt.setString(3, student.getEmail());
        stmt.setString(4, student.getStudentId());
        stmt.executeUpdate();
        stmt.close();
    }

    public static List<Student> loadStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM students");
        while (rs.next()) {
            students.add(new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("studentId")
            ));
        }
        rs.close();
        stmt.close();
        return students;
    }

    // Appointment CRUD
    public static void saveAppointment(Appointment appointment) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO appointments (id, studentId, counselor, dateTime, status, notes) VALUES (?, ?, ?, ?, ?, ?)"
        );
        stmt.setInt(1, appointment.getId());
        stmt.setInt(2, appointment.getStudentId());
        stmt.setString(3, appointment.getCounselor());
        stmt.setString(4, appointment.getDateTime().toString());
        stmt.setString(5, appointment.getStatus());
        stmt.setString(6, appointment.getNotes());
        stmt.executeUpdate();
        stmt.close();
    }

    public static List<Appointment> loadAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM appointments");
        while (rs.next()) {
            appointments.add(new Appointment(
                rs.getInt("id"),
                rs.getInt("studentId"),
                rs.getString("counselor"),
                LocalDateTime.parse(rs.getString("dateTime")),
                rs.getString("status"),
                rs.getString("notes")
            ));
        }
        rs.close();
        stmt.close();
        return appointments;
    }

    // Case CRUD
    public static void saveCase(Case caseObj) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO cases (id, studentId, description, status, wellnessMetrics, followUpNotes) VALUES (?, ?, ?, ?, ?, ?)"
        );
        stmt.setInt(1, caseObj.getId());
        stmt.setInt(2, caseObj.getStudentId());
        stmt.setString(3, caseObj.getDescription());
        stmt.setString(4, caseObj.getStatus());
        stmt.setString(5, caseObj.getWellnessMetrics());
        stmt.setString(6, caseObj.getFollowUpNotes());
        stmt.executeUpdate();
        stmt.close();
    }

    public static List<Case> loadCases() throws SQLException {
        List<Case> cases = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM cases");
        while (rs.next()) {
            cases.add(new Case(
                rs.getInt("id"),
                rs.getInt("studentId"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getString("wellnessMetrics"),
                rs.getString("followUpNotes")
            ));
        }
        rs.close();
        stmt.close();
        return cases;
    }

    // Workshop CRUD
    public static void saveWorkshop(Workshop workshop) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT OR REPLACE INTO workshops (id, title, dateTime, description, maxParticipants) VALUES (?, ?, ?, ?, ?)"
        );
        stmt.setInt(1, workshop.getId());
        stmt.setString(2, workshop.getTitle());
        stmt.setString(3, workshop.getDateTime().toString());
        stmt.setString(4, workshop.getDescription());
        stmt.setInt(5, workshop.getMaxParticipants());
        stmt.executeUpdate();
        stmt.close();

        // Save participants
        PreparedStatement deleteStmt = connection.prepareStatement(
            "DELETE FROM workshop_participants WHERE workshopId = ?"
        );
        deleteStmt.setInt(1, workshop.getId());
        deleteStmt.executeUpdate();
        deleteStmt.close();

        PreparedStatement insertStmt = connection.prepareStatement(
            "INSERT INTO workshop_participants (workshopId, studentId) VALUES (?, ?)"
        );
        for (Integer participantId : workshop.getParticipantIds()) {
            insertStmt.setInt(1, workshop.getId());
            insertStmt.setInt(2, participantId);
            insertStmt.executeUpdate();
        }
        insertStmt.close();
    }

    public static List<Workshop> loadWorkshops() throws SQLException {
        List<Workshop> workshops = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM workshops");
        while (rs.next()) {
            int workshopId = rs.getInt("id");
            List<Integer> participants = new ArrayList<>();
            PreparedStatement partStmt = connection.prepareStatement(
                "SELECT studentId FROM workshop_participants WHERE workshopId = ?"
            );
            partStmt.setInt(1, workshopId);
            ResultSet partRs = partStmt.executeQuery();
            while (partRs.next()) {
                participants.add(partRs.getInt("studentId"));
            }
            partRs.close();
            partStmt.close();

            workshops.add(new Workshop(
                workshopId,
                rs.getString("title"),
                LocalDateTime.parse(rs.getString("dateTime")),
                rs.getString("description"),
                rs.getInt("maxParticipants"),
                participants
            ));
        }
        rs.close();
        stmt.close();
        return workshops;
    }

    // Delete methods
    public static void deleteUser(int userId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, userId);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void deleteStudent(int studentId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM students WHERE id = ?");
        stmt.setInt(1, studentId);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void deleteAppointment(int appointmentId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM appointments WHERE id = ?");
        stmt.setInt(1, appointmentId);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void deleteCase(int caseId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM cases WHERE id = ?");
        stmt.setInt(1, caseId);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void deleteWorkshop(int workshopId) throws SQLException {
        // First delete participants
        PreparedStatement deletePartStmt = connection.prepareStatement(
            "DELETE FROM workshop_participants WHERE workshopId = ?"
        );
        deletePartStmt.setInt(1, workshopId);
        deletePartStmt.executeUpdate();
        deletePartStmt.close();

        // Then delete workshop
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM workshops WHERE id = ?");
        stmt.setInt(1, workshopId);
        stmt.executeUpdate();
        stmt.close();
    }

    // Get next available ID for auto-increment
    public static int getNextUserId() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM users");
        int nextId = rs.next() ? rs.getInt("nextId") : 1000001;
        rs.close();
        stmt.close();
        return nextId;
    }

    public static int getNextStudentId() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM students");
        int nextId = rs.next() ? rs.getInt("nextId") : 1;
        rs.close();
        stmt.close();
        return nextId;
    }

    public static int getNextAppointmentId() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM appointments");
        int nextId = rs.next() ? rs.getInt("nextId") : 1;
        rs.close();
        stmt.close();
        return nextId;
    }

    public static int getNextCaseId() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM cases");
        int nextId = rs.next() ? rs.getInt("nextId") : 1;
        rs.close();
        stmt.close();
        return nextId;
    }

    public static int getNextWorkshopId() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM workshops");
        int nextId = rs.next() ? rs.getInt("nextId") : 1;
        rs.close();
        stmt.close();
        return nextId;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
