import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        // Initialize data lists from SharedData
        students = SharedData.students;
        appointments = SharedData.appointments;
        cases = SharedData.cases;
        workshops = SharedData.workshops;

        // Ensure students are populated from users if not loaded from DB
        if (students.isEmpty()) {
            for (User registeredUser : UserDatabase.users) {
                if (registeredUser.getRole() == Role.STUDENT && registeredUser.getStudentId() != null) {
                    Student student = new Student(registeredUser.getId(), registeredUser.getName(), registeredUser.getEmail(), registeredUser.getStudentId());
                    students.add(student);
                    SharedData.saveStudent(student);
                }
            }
        }

        initializeComponents();

        // Add window listener to save data on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAllData();
                DatabaseManager.closeConnection();
            }
        });

        setVisible(true);
    }

    private void saveAllData() {
        // Save all appointments
        for (Appointment appt : SharedData.appointments) {
            SharedData.saveAppointment(appt);
        }
        // Save all cases
        for (Case caseObj : SharedData.cases) {
            SharedData.saveCase(caseObj);
        }
        // Save all workshops
        for (Workshop workshop : SharedData.workshops) {
            SharedData.saveWorkshop(workshop);
        }
        // Save all students
        for (Student student : SharedData.students) {
            SharedData.saveStudent(student);
        }
        // Update totals
        SharedData.updateTotals();
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
