import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CounselorDashboard extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane;
    private List<Student> students;
    private List<Appointment> appointments;
    private List<Case> cases;
    private List<Workshop> workshops;
    private User currentUser;
    private JButton logoutButton;

    public CounselorDashboard(User user) {
        this.currentUser = user;
        setTitle("GuidancePro: Counselor Dashboard - " + user.getName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize data lists
        students = new ArrayList<>();
        appointments = new ArrayList<>();
        cases = new ArrayList<>();
        workshops = new ArrayList<>();
        // Populate students from registered users with STUDENT role
        for (User registeredUser : UserDatabase.users) {
            if (registeredUser.getRole() == Role.STUDENT && registeredUser.getStudentId() != null) {
                students.add(new Student(registeredUser.getId(), registeredUser.getName(), registeredUser.getEmail(), registeredUser.getStudentId()));
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
        AppointmentManager appointmentManager = new AppointmentManager(students);
        CaseTracker caseTracker = new CaseTracker(students);
        WorkshopOrganizer workshopOrganizer = new WorkshopOrganizer(students);
        ReportGenerator reportGenerator = new ReportGenerator(students, appointments, cases, workshops);

        // Add tabs
        tabbedPane.addTab("Appointments", appointmentManager);
        tabbedPane.addTab("Cases", caseTracker);
        tabbedPane.addTab("Workshops", workshopOrganizer);
        tabbedPane.addTab("Reports", reportGenerator);

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
