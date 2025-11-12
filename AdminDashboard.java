import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
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
    private FolderDatabase folderDatabase;
    private JTree folderTree;

    public AdminDashboard(User user) {
        this.currentUser = user;
        setTitle("GuidancePro: Admin Dashboard - " + user.getName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize folder database
        folderDatabase = new FolderDatabase("database");

        // Initialize data lists
        students = new ArrayList<>();
        appointments = new ArrayList<>();
        cases = new ArrayList<>();
        workshops = new ArrayList<>();
        users = UserDatabase.users;

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
        UserManagement userManagement = new UserManagement(users);

        // Add tabs
        tabbedPane.addTab("Appointments", appointmentManager);
        tabbedPane.addTab("Cases", caseTracker);
        tabbedPane.addTab("Workshops", workshopOrganizer);
        tabbedPane.addTab("Reports", reportGenerator);
        tabbedPane.addTab("User Management", userManagement);
        tabbedPane.addTab("Folder Database", createFolderDatabasePanel());

        // Add change listener to refresh User Management tab when selected
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == userManagement) {
                userManagement.refresh();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createFolderDatabasePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Folder Database Browser"), BorderLayout.NORTH);

        // Create a simple tree model for demonstration
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Database");
        List<String> entities = folderDatabase.listEntities();
        for (String entity : entities) {
            DefaultMutableTreeNode entityNode = new DefaultMutableTreeNode(entity);
            List<String> dataIds = folderDatabase.listData(entity);
            for (String id : dataIds) {
                entityNode.add(new DefaultMutableTreeNode(id + ".txt"));
            }
            root.add(entityNode);
        }

        folderTree = new JTree(root);
        folderTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) folderTree.getLastSelectedPathComponent();
                    if (selectedNode != null && selectedNode.isLeaf() && !selectedNode.getUserObject().equals("Database")) {
                        String nodeName = selectedNode.getUserObject().toString();
                        if (nodeName.endsWith(".txt")) {
                            String id = nodeName.substring(0, nodeName.lastIndexOf('.'));
                            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                            String entity = parentNode.getUserObject().toString();
                            String data = folderDatabase.loadData(entity, id);
                            if (data != null) {
                                try {
                                    Runtime.getRuntime().exec("notepad.exe " + "database\\" + entity + "\\" + nodeName);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(folderTree);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add some sample data buttons
        JPanel buttonPanel = new JPanel();
        JButton addUserButton = new JButton("Add Sample User");
        addUserButton.addActionListener(e -> {
            folderDatabase.saveData("users", "1", "ID: 1\nName: John Doe\nEmail: john@example.com");
            refreshFolderTree();
        });
        JButton addStudentButton = new JButton("Add Sample Student");
        addStudentButton.addActionListener(e -> {
            folderDatabase.saveData("students", "1", "ID: 1\nName: Jane Smith\nEmail: jane@example.com\nStudentID: 12345");
            refreshFolderTree();
        });
        buttonPanel.add(addUserButton);
        buttonPanel.add(addStudentButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshFolderTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Database");
        List<String> entities = folderDatabase.listEntities();
        for (String entity : entities) {
            DefaultMutableTreeNode entityNode = new DefaultMutableTreeNode(entity);
            List<String> dataIds = folderDatabase.listData(entity);
            for (String id : dataIds) {
                entityNode.add(new DefaultMutableTreeNode(id + ".txt"));
            }
            root.add(entityNode);
        }
        folderTree.setModel(new DefaultTreeModel(root));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            dispose();
            new LoginForm();
        }
    }
}
