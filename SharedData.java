import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class SharedData {
    public static List<Appointment> appointments = new ArrayList<>();
    public static List<Case> cases = new ArrayList<>();
    public static List<Workshop> workshops = new ArrayList<>();
    public static List<Student> students = new ArrayList<>();
    public static int totalAppointments = 0;
    public static int totalCases = 0;
    public static int totalWorkshops = 0;

    static {
        loadDataFromDatabase();
    }

    private static void loadDataFromDatabase() {
        try {
            appointments = DatabaseManager.loadAppointments();
            cases = DatabaseManager.loadCases();
            workshops = DatabaseManager.loadWorkshops();
            students = DatabaseManager.loadStudents();
            totalAppointments = appointments.size();
            totalCases = cases.size();
            totalWorkshops = workshops.size();
        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback to empty lists if database load fails
        }
    }

    public static void saveAppointment(Appointment appointment) {
        try {
            DatabaseManager.saveAppointment(appointment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveCase(Case caseObj) {
        try {
            DatabaseManager.saveCase(caseObj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveWorkshop(Workshop workshop) {
        try {
            DatabaseManager.saveWorkshop(workshop);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveStudent(Student student) {
        try {
            DatabaseManager.saveStudent(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTotals() {
        totalAppointments = appointments.size();
        totalCases = cases.size();
        totalWorkshops = workshops.size();
    }
}
