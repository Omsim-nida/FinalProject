import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StudentDashboard extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane;
    private List<Student> students;
    private List<Appointment> appointments;
    private List<Case> cases;
    private List<Workshop> workshops;
    private User currentUser;
    private JButton logoutButton;

    public StudentDashboard(User user) {
        this.currentUser = user;
        setTitle("GuidancePro: Student Dashboard - " + user.getName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize data lists
        students = new ArrayList<>();
        appointments = new ArrayList<>();
        cases = new ArrayList<>();
        workshops = new ArrayList<>();
        // Populate students from registered users, but only the current student
        for (User registeredUser : UserDatabase.users) {
            if (registeredUser.getId() == user.getId() && registeredUser.getRole() == Role.STUDENT && registeredUser.getStudentId() != null) {
                students.add(new Student(registeredUser.getId(), registeredUser.getName(), registeredUser.getEmail(), registeredUser.getStudentId()));
                break; // Only one student
            }
        }

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Logout button at top
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        // Create module panels
        AppointmentManager appointmentManager = new AppointmentManager(students, true);
        CaseTracker caseTracker = new CaseTracker(students, true);
        WorkshopOrganizer workshopOrganizer = new WorkshopOrganizer(students, true);

        // Add tabs (limited for student)
        tabbedPane.addTab("My Appointments", appointmentManager);
        tabbedPane.addTab("My Cases", caseTracker);
        tabbedPane.addTab("Workshops", workshopOrganizer);

        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            dispose();
            new LoginForm();
        }
    }
}
