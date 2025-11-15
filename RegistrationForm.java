import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.sql.SQLException;

public class RegistrationForm extends JDialog implements ActionListener {
    private JTextField usernameField;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JPasswordField confirmPasswordField;
    private JComboBox<Role> roleComboBox;
    private JTextField studentIdField;
    private JButton registerButton;
    private JButton backToLoginButton;
    private List<User> users;
    private boolean emailValid = false;

    public RegistrationForm() {
        this(UserDatabase.users);
    }

    public RegistrationForm(List<User> users) {
        this.users = users != null ? users : UserDatabase.users;
        setTitle("Registration Form");
        setSize(400, 450);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setModal(true);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255)); // Alice Blue background

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        JLabel titleLabel = new JLabel("GuidancePro Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 25));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(usernameField, gbc);

        // Name Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nameLabel, gbc);

        // Name Field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(200, 25));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(nameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 25));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(passwordField, gbc);

        // Email Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailLabel, gbc);

        // Email Field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(200, 25));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        // Add email validation listener
        emailField.addCaretListener(e -> validateEmail());
        formPanel.add(emailField, gbc);

        // Confirm Password Label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(confirmPasswordLabel, gbc);

        // Confirm Password Field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(200, 25));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(confirmPasswordField, gbc);

        // Role Label
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(roleLabel, gbc);

        // Role ComboBox
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        roleComboBox = new JComboBox<>(Role.values());
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setPreferredSize(new Dimension(200, 25));
        roleComboBox.addActionListener(this); // Add listener to show/hide studentId field
        formPanel.add(roleComboBox, gbc);

        // Student ID Label (initially hidden)
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        studentIdLabel.setVisible(false);
        formPanel.add(studentIdLabel, gbc);

        // Student ID Field (initially hidden)
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        studentIdField = new JTextField(20);
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        studentIdField.setPreferredSize(new Dimension(200, 25));
        studentIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        studentIdField.setVisible(false);
        formPanel.add(studentIdField, gbc);

        // Button Panel
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(34, 139, 34)); // Forest Green
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.addActionListener(this);
        registerButton.setEnabled(false); // Initially disabled until email is valid

        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backToLoginButton.setForeground(new Color(70, 130, 180));
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.addActionListener(this);

        buttonPanel.add(registerButton);
        formPanel.add(buttonPanel, gbc);

        // Back to Login Button
        gbc.gridy = 8;
        formPanel.add(backToLoginButton, gbc);

        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    private void validateEmail() {
        String email = emailField.getText();
        // Stronger email validation: must contain @, at least one dot after @, and proper domain
        emailValid = email.contains("@") && email.length() > 5 &&
                    email.indexOf("@") > 0 && email.indexOf("@") < email.length() - 3 &&
                    email.contains(".") && email.lastIndexOf(".") > email.indexOf("@") + 1;
        updateRegisterButtonState();

        // Visual feedback
        if (email.isEmpty()) {
            emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        } else if (emailValid) {
            emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        } else {
            emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 20, 60), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        }
    }

    private void updateRegisterButtonState() {
        registerButton.setEnabled(emailValid);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == roleComboBox) {
            Role selectedRole = (Role) roleComboBox.getSelectedItem();
            boolean isStudent = selectedRole == Role.STUDENT;
            // Find the studentIdLabel and studentIdField components
            Component[] components = ((JPanel) getContentPane().getComponent(1)).getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel && ((JLabel) comp).getText().equals("Student ID:")) {
                    comp.setVisible(isStudent);
                } else if (comp instanceof JTextField && comp == studentIdField) {
                    comp.setVisible(isStudent);
                }
            }
            pack(); // Resize the dialog to fit the new content
        } else if (e.getSource() == registerButton) {
            String username = usernameField.getText();
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String confirmPassword = new String(confirmPasswordField.getPassword());
            Role role = (Role) roleComboBox.getSelectedItem();
            String studentId = studentIdField.getText();
            // Simple validation for registration
            if (username.isEmpty() || name.isEmpty() || password.isEmpty() || email.isEmpty() ||
                (role == Role.STUDENT && studentId.isEmpty())) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
            } else if (!emailValid) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address (must contain @).");
            } else if (password.length() < 8) {
                JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long.");
            } else if (!password.matches(".*[A-Z].*")) {
                JOptionPane.showMessageDialog(this, "Password must contain at least one uppercase letter.");
            } else if (!password.matches(".*[a-z].*")) {
                JOptionPane.showMessageDialog(this, "Password must contain at least one lowercase letter.");
            } else if (!password.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(this, "Password must contain at least one number.");
            } else if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
            } else {
                // Add user to list if users is not null
                if (users != null) {
                    try {
                        int newId = DatabaseManager.getNextUserId();
                        User newUser = role == Role.STUDENT ?
                            new User(newId, username, password, role, name, email, studentId) :
                            new User(newId, username, password, role, name, email);
                        // New users are set to PENDING status by default in User constructor
                        users.add(newUser);
                        DatabaseManager.saveUser(newUser);
                        // If student, add to SharedData.students and save to database
                        if (role == Role.STUDENT) {
                            Student newStudent = new Student(newId, name, email, studentId);
                            SharedData.students.add(newStudent);
                            DatabaseManager.saveStudent(newStudent);
                        }
                        JOptionPane.showMessageDialog(this, "Registration successful! Your account is pending approval. User ID: " + newId);
                    } catch (java.sql.SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving user: " + ex.getMessage());
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                }
                // Close registration form
                dispose();
            }
        } else if (e.getSource() == backToLoginButton) {
            dispose();
            new LoginForm();
        }
    }

}
