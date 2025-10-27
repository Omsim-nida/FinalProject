import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane;
    private List<Student> students;
    private List<Appointment> appointments;
    private List<Case> cases;
    private List<Workshop> workshops;
    private List<User> users;
    private User currentUser;
    private JButton logoutButton;

    public AdminDashboard(User user) {
        this.currentUser = user;
        setTitle("GuidancePro: Admin Dashboard - " + user.getName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize data lists
        students = new ArrayList<>();
        appointments = new ArrayList<>();
        cases = new ArrayList<>();
        workshops = new ArrayList<>();
        users = UserDatabase.users;

        // Add some sample students for demo
        students.add(new Student(1, "John Doe", "john.doe@email.com", "2021001"));
        students.add(new Student(2, "Jane Smith", "jane.smith@email.com", "2021002"));
        students.add(new Student(3, "Bob Johnson", "bob.johnson@email.com", "2021003"));

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
        UserManagement userManagement = new UserManagement(users);

        // Add tabs
        tabbedPane.addTab("Appointments", appointmentManager);
        tabbedPane.addTab("Cases", caseTracker);
        tabbedPane.addTab("Workshops", workshopOrganizer);
        tabbedPane.addTab("Reports", reportGenerator);
        tabbedPane.addTab("User Management", userManagement);

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
