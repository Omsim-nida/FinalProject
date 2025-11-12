import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagement extends JPanel implements ActionListener {
    private JTable userTable;
    private JButton addUserButton;
    private JButton editUserButton;
    private JButton deleteUserButton;
    private List<User> users;

    public UserManagement(List<User> users) {
        this.users = users;
        initializeComponents();
    }

    public void refresh() {
        updateTable();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Table
        updateTable();

        // Buttons
        JPanel buttonPanel = new JPanel();
        addUserButton = new JButton("Add User");
        editUserButton = new JButton("Edit User");
        deleteUserButton = new JButton("Delete User");

        addUserButton.addActionListener(this);
        editUserButton.addActionListener(this);
        deleteUserButton.addActionListener(this);

        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTable() {
        String[] columnNames = {"ID", "Username", "Role", "Name", "Email"};
        Object[][] data = new Object[users.size()][5];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getId();
            data[i][1] = user.getUsername();
            data[i][2] = user.getRole();
            data[i][3] = user.getName();
            data[i][4] = user.getEmail();
        }
        userTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addUserButton) {
            // Open registration form for adding user
            new RegistrationForm(users);
            // Refresh the table after registration (executes after modal dialog closes)
            updateTable();
        } else if (e.getSource() == editUserButton) {
            // TODO: Implement edit functionality
            JOptionPane.showMessageDialog(this, "Edit functionality not implemented yet.");
        } else if (e.getSource() == deleteUserButton) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.");
                return;
            }
            int userId = (int) userTable.getValueAt(selectedRow, 0);
            User userToDelete = users.stream().filter(user -> user.getId() == userId).findFirst().orElse(null);
            if (userToDelete != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user: " + userToDelete.getName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    users.remove(userToDelete);
                    // If the user is a student, remove from SharedData.students
                    if (userToDelete.getRole() == Role.STUDENT) {
                        SharedData.students.removeIf(student -> student.getId() == userToDelete.getId());
                    }
                    updateTable();
                }
            }
        }
    }
}
