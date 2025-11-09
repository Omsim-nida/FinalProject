import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class CaseTracker extends JPanel implements ActionListener {
    private JTable caseTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private List<Case> cases;
    private List<Student> students;
    private int nextId = 1;
    private boolean isStudent = false;
    private Timer refreshTimer;

    public CaseTracker(List<Student> students) {
        this(students, false);
    }

    public CaseTracker(List<Student> students, boolean isStudent) {
        this.students = students;
        this.isStudent = isStudent;
        this.cases = SharedData.cases;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Table setup
        String[] columnNames = {"ID", "Student", "Description", "Status", "Wellness Metrics", "Follow-up Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        caseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(caseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Case");
        editButton = new JButton("Edit Case");
        deleteButton = new JButton("Delete Case");

        if (isStudent) {
            addButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();

        // Start auto-refresh timer (every 5 seconds)
        refreshTimer = new Timer(5000, e -> refreshTable());
        refreshTimer.start();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Case c : cases) {
            String studentName = students.stream()
                .filter(s -> s.getId() == c.getStudentId())
                .map(Student::getName)
                .findFirst()
                .orElse("Unknown");
            tableModel.addRow(new Object[]{
                c.getId(),
                studentName,
                c.getDescription(),
                c.getStatus(),
                c.getWellnessMetrics(),
                c.getFollowUpNotes()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            showCaseDialog(null);
        } else if (e.getSource() == editButton) {
            int selectedRow = caseTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Case c = cases.stream()
                    .filter(caseItem -> caseItem.getId() == id)
                    .findFirst()
                    .orElse(null);
                showCaseDialog(c);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a case to edit.");
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = caseTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                cases.removeIf(c -> c.getId() == id);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Case deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a case to delete.");
            }
        }
    }

    private void showCaseDialog(Case caseItem) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            caseItem == null ? "Add Case" : "Edit Case", true);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JComboBox<Student> studentCombo = new JComboBox<>();
        for (Student s : students) {
            studentCombo.addItem(s);
        }

        JTextArea descriptionArea = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        JTextField statusField = new JTextField();
        JTextArea wellnessArea = new JTextArea(2, 20);
        JScrollPane wellnessScroll = new JScrollPane(wellnessArea);
        JTextArea followUpArea = new JTextArea(3, 20);
        JScrollPane followUpScroll = new JScrollPane(followUpArea);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        if (caseItem != null) {
            studentCombo.setSelectedItem(students.stream()
                .filter(s -> s.getId() == caseItem.getStudentId())
                .findFirst()
                .orElse(null));
            descriptionArea.setText(caseItem.getDescription());
            statusField.setText(caseItem.getStatus());
            wellnessArea.setText(caseItem.getWellnessMetrics());
            followUpArea.setText(caseItem.getFollowUpNotes());
        }

        saveButton.addActionListener(e -> {
            Student selectedStudent = (Student) studentCombo.getSelectedItem();
            if (selectedStudent == null) {
                JOptionPane.showMessageDialog(dialog, "Please select a student.");
                return;
            }
            String description = descriptionArea.getText();
            String status = statusField.getText();
            String wellness = wellnessArea.getText();
            String followUp = followUpArea.getText();

            if (caseItem == null) {
                cases.add(new Case(nextId++, selectedStudent.getId(), description, status, wellness, followUp));
                JOptionPane.showMessageDialog(dialog, "Case added successfully.");
            } else {
                caseItem.setStudentId(selectedStudent.getId());
                caseItem.setDescription(description);
                caseItem.setStatus(status);
                caseItem.setWellnessMetrics(wellness);
                caseItem.setFollowUpNotes(followUp);
                JOptionPane.showMessageDialog(dialog, "Case updated successfully.");
            }
            refreshTable();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(new JLabel("Student:"));
        dialog.add(studentCombo);
        dialog.add(new JLabel("Description:"));
        dialog.add(descScroll);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusField);
        dialog.add(new JLabel("Wellness Metrics:"));
        dialog.add(wellnessScroll);
        dialog.add(new JLabel("Follow-up Notes:"));
        dialog.add(followUpScroll);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }
}
