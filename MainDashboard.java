import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainDashboard extends JFrame {
    private JTabbedPane tabbedPane;
    private List<Student> students;
    private List<Appointment> appointments;
    private List<Case> cases;
    private List<Workshop> workshops;

    public MainDashboard() {
        setTitle("GuidancePro: Smart Guidance Office Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize data lists
        students = new ArrayList<>();
        appointments = new ArrayList<>();
        cases = new ArrayList<>();
        workshops = new ArrayList<>();

        // Add some sample students for demo
        students.add(new Student(1, "John Doe", "john.doe@email.com", "2021001"));
        students.add(new Student(2, "Jane Smith", "jane.smith@email.com", "2021002"));
        students.add(new Student(3, "Bob Johnson", "bob.johnson@email.com", "2021003"));

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        // Create module panels
        AppointmentManager appointmentManager = new AppointmentManager(students);
        CaseTracker caseTracker = new CaseTracker(students);
        WorkshopOrganizer workshopOrganizer = new WorkshopOrganizer(students);
        ReportGenerator reportGenerator = new ReportGenerator(students, appointments, cases, workshops);

        // Add tabs
        tabbedPane.addTab("Appointments", appointmentManager);
        tabbedPane.addTab("Cases", caseTracker);
        tabbedPane.addTab("Workshops", workshopOrganizer);
        tabbedPane.addTab("Reports", reportGenerator);

        add(tabbedPane);
    }

    // Getters for data access (could be used for persistence later)
    public List<Student> getStudents() { return students; }
    public List<Appointment> getAppointments() { return appointments; }
    public List<Case> getCases() { return cases; }
    public List<Workshop> getWorkshops() { return workshops; }
}
