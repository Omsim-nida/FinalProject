import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
// import EmailUtility; // Commented out until JavaMail is added

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
        JButton approveButton = new JButton("Approve User");
        JButton rejectButton = new JButton("Reject User");

        addUserButton.addActionListener(this);
        editUserButton.addActionListener(this);
        deleteUserButton.addActionListener(this);
        approveButton.addActionListener(this);
        rejectButton.addActionListener(this);

        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(deleteUserButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTable() {
        // Refresh users from database for real-time updates
        UserDatabase.refreshUsers();
        users = UserDatabase.users;
        
        // Remove old table if it exists
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JScrollPane) {
                remove(comp);
            }
        }
        
        String[] columnNames = {"ID", "Username", "Role", "Name", "Email", "Status"};
        Object[][] data = new Object[users.size()][6];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getId();
            data[i][1] = user.getUsername();
            data[i][2] = user.getRole();
            data[i][3] = user.getName();
            data[i][4] = user.getEmail();
            data[i][5] = user.getStatus();
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
            RegistrationForm regForm = new RegistrationForm(users);
            // Refresh the table after registration (executes after modal dialog closes)
            // Use SwingUtilities to ensure it runs after dialog closes
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(200); // Small delay to ensure database save completes
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                updateTable();
            });
        } else if (e.getSource() == editUserButton) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.");
                return;
            }
            int userId = (int) userTable.getValueAt(selectedRow, 0);
            User userToEdit = users.stream().filter(user -> user.getId() == userId).findFirst().orElse(null);
            if (userToEdit != null) {
                // Refresh user from database to get latest data
                UserDatabase.refreshUsers();
                User freshUser = UserDatabase.users.stream()
                    .filter(u -> u.getId() == userId)
                    .findFirst()
                    .orElse(null);
                if (freshUser != null) {
                    EditUserDialog editDialog = new EditUserDialog((Frame) SwingUtilities.getWindowAncestor(this), freshUser);
                    // Refresh table after edit dialog closes
                    SwingUtilities.invokeLater(() -> {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        updateTable();
                    });
                }
            }
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
                    // If the user is a student, remove from SharedData.students first
                    if (userToDelete.getRole() == Role.STUDENT) {
                        SharedData.deleteStudent(userToDelete.getId());
                    }
                    SharedData.deleteUser(userId);
                    updateTable();
                }
            }
        } else if (e.getSource() instanceof JButton && ((JButton) e.getSource()).getText().equals("Approve User")) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to approve.");
                return;
            }
            int userId = (int) userTable.getValueAt(selectedRow, 0);
            User userToApprove = users.stream().filter(user -> user.getId() == userId).findFirst().orElse(null);
            if (userToApprove != null && userToApprove.getStatus() == UserStatus.PENDING) {
                userToApprove.setStatus(UserStatus.APPROVED);
                try {
                    DatabaseManager.saveUser(userToApprove);
                    // TODO: Send approval email notification once JavaMail is added
                    // EmailUtility.sendApprovalNotification(userToApprove.getEmail(), userToApprove.getName(), true);
                    JOptionPane.showMessageDialog(this, "User approved successfully!");
                    updateTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error approving user: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "User is not pending approval.");
            }
        } else if (e.getSource() instanceof JButton && ((JButton) e.getSource()).getText().equals("Reject User")) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to reject.");
                return;
            }
            int userId = (int) userTable.getValueAt(selectedRow, 0);
            User userToReject = users.stream().filter(user -> user.getId() == userId).findFirst().orElse(null);
            if (userToReject != null && userToReject.getStatus() == UserStatus.PENDING) {
                userToReject.setStatus(UserStatus.REJECTED);
                try {
                    DatabaseManager.saveUser(userToReject);
                    // TODO: Send rejection email notification once JavaMail is added
                    // EmailUtility.sendApprovalNotification(userToReject.getEmail(), userToReject.getName(), false);
                    JOptionPane.showMessageDialog(this, "User rejected successfully!");
                    updateTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error rejecting user: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "User is not pending approval.");
            }
        }
    }
}
