import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class AppointmentManager extends JPanel implements ActionListener {
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private List<Appointment> appointments;
    private List<Student> students;
    private int nextId = 1;
    private boolean isStudent = false;
    private Timer refreshTimer;

    public AppointmentManager(List<Student> students) {
        this(students, false);
    }

    public AppointmentManager(List<Student> students, boolean isStudent) {
        this.students = students;
        this.isStudent = isStudent;
        this.appointments = SharedData.appointments;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Table setup
        String[] columnNames = {"ID", "Student", "Counselor", "Date & Time", "Status", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        appointmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Appointment");
        editButton = new JButton("Edit Appointment");
        deleteButton = new JButton("Delete Appointment");

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Appointment appt : appointments) {
            String studentName = students.stream()
                .filter(s -> s.getId() == appt.getStudentId())
                .map(Student::getName)
                .findFirst()
                .orElse("Unknown");
            tableModel.addRow(new Object[]{
                appt.getId(),
                studentName,
                appt.getCounselor(),
                appt.getDateTime().format(formatter),
                appt.getStatus(),
                appt.getNotes()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            showAppointmentDialog(null);
        } else if (e.getSource() == editButton) {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Appointment appt = appointments.stream()
                    .filter(a -> a.getId() == id)
                    .findFirst()
                    .orElse(null);
                showAppointmentDialog(appt);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an appointment to edit.");
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                appointments.removeIf(a -> a.getId() == id);
                SharedData.totalAppointments--;
                refreshTable();
                JOptionPane.showMessageDialog(this, "Appointment deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select an appointment to delete.");
            }
        }
    }

    private void showAppointmentDialog(Appointment appointment) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            appointment == null ? "Add Appointment" : "Edit Appointment", true);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JComboBox<Student> studentCombo = new JComboBox<>();
        for (Student s : students) {
            studentCombo.addItem(s);
        }

        JTextField counselorField = new JTextField();
        JTextField dateField = new JTextField("yyyy-MM-dd");
        JTextField timeField = new JTextField("HH:mm");
        JTextField statusField = new JTextField();
        JTextArea notesArea = new JTextArea(3, 20);
        JScrollPane notesScroll = new JScrollPane(notesArea);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        if (appointment != null) {
            studentCombo.setSelectedItem(students.stream()
                .filter(s -> s.getId() == appointment.getStudentId())
                .findFirst()
                .orElse(null));
            counselorField.setText(appointment.getCounselor());
            dateField.setText(appointment.getDateTime().toLocalDate().toString());
            timeField.setText(appointment.getDateTime().toLocalTime().toString());
            statusField.setText(appointment.getStatus());
            notesArea.setText(appointment.getNotes());
        }

        saveButton.addActionListener(e -> {
            try {
                Student selectedStudent = (Student) studentCombo.getSelectedItem();
                if (selectedStudent == null) {
                    JOptionPane.showMessageDialog(dialog, "Please select a student.");
                    return;
                }
                LocalDateTime dateTime = LocalDateTime.parse(dateField.getText() + " " + timeField.getText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String counselor = counselorField.getText();
                String status = statusField.getText();
                String notes = notesArea.getText();

                if (appointment == null) {
                    appointments.add(new Appointment(nextId++, selectedStudent.getId(), counselor, dateTime, status, notes));
                    SharedData.totalAppointments++;
                    JOptionPane.showMessageDialog(dialog, "Appointment added successfully.");
                } else {
                    appointment.setStudentId(selectedStudent.getId());
                    appointment.setCounselor(counselor);
                    appointment.setDateTime(dateTime);
                    appointment.setStatus(status);
                    appointment.setNotes(notes);
                    JOptionPane.showMessageDialog(dialog, "Appointment updated successfully.");
                }
                refreshTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date/time format. Use yyyy-MM-dd and HH:mm.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(new JLabel("Student:"));
        dialog.add(studentCombo);
        dialog.add(new JLabel("Counselor:"));
        dialog.add(counselorField);
        dialog.add(new JLabel("Date (yyyy-MM-dd):"));
        dialog.add(dateField);
        dialog.add(new JLabel("Time (HH:mm):"));
        dialog.add(timeField);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusField);
        dialog.add(new JLabel("Notes:"));
        dialog.add(notesScroll);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }
}
