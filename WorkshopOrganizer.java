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

public class WorkshopOrganizer extends JPanel implements ActionListener {
    private JTable workshopTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, manageParticipantsButton;
    private List<Workshop> workshops;
    private List<Student> students;
    private int nextId = 1;
    private boolean isStudent = false;
    private Timer refreshTimer;

    public WorkshopOrganizer(List<Student> students) {
        this(students, false);
    }

    public WorkshopOrganizer(List<Student> students, boolean isStudent) {
        this.students = students;
        this.isStudent = isStudent;
        this.workshops = SharedData.workshops;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Table setup
        String[] columnNames = {"ID", "Title", "Date & Time", "Description", "Max Participants", "Current Participants"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        workshopTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(workshopTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Workshop");
        editButton = new JButton("Edit Workshop");
        deleteButton = new JButton("Delete Workshop");
        manageParticipantsButton = new JButton("Manage Participants");

        if (isStudent) {
            addButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            manageParticipantsButton.setEnabled(false);
        }

        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        manageParticipantsButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(manageParticipantsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();

        // Start auto-refresh timer (every 5 seconds)
        refreshTimer = new Timer(5000, e -> refreshTable());
        refreshTimer.start();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Workshop w : workshops) {
            tableModel.addRow(new Object[]{
                w.getId(),
                w.getTitle(),
                w.getDateTime().format(formatter),
                w.getDescription(),
                w.getMaxParticipants(),
                w.getParticipantIds().size()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            showWorkshopDialog(null);
        } else if (e.getSource() == editButton) {
            int selectedRow = workshopTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Workshop w = workshops.stream()
                    .filter(workshop -> workshop.getId() == id)
                    .findFirst()
                    .orElse(null);
                showWorkshopDialog(w);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a workshop to edit.");
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = workshopTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                workshops.removeIf(w -> w.getId() == id);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Workshop deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a workshop to delete.");
            }
        } else if (e.getSource() == manageParticipantsButton) {
            int selectedRow = workshopTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Workshop w = workshops.stream()
                    .filter(workshop -> workshop.getId() == id)
                    .findFirst()
                    .orElse(null);
                showParticipantDialog(w);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a workshop to manage participants.");
            }
        }
    }

    private void showWorkshopDialog(Workshop workshop) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            workshop == null ? "Add Workshop" : "Edit Workshop", true);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextField titleField = new JTextField();
        JTextField dateField = new JTextField("yyyy-MM-dd");
        JTextField timeField = new JTextField("HH:mm");
        JTextArea descArea = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descArea);
        JTextField maxPartField = new JTextField();

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        if (workshop != null) {
            titleField.setText(workshop.getTitle());
            dateField.setText(workshop.getDateTime().toLocalDate().toString());
            timeField.setText(workshop.getDateTime().toLocalTime().toString());
            descArea.setText(workshop.getDescription());
            maxPartField.setText(String.valueOf(workshop.getMaxParticipants()));
        }

        saveButton.addActionListener(e -> {
            try {
                String title = titleField.getText();
                LocalDateTime dateTime = LocalDateTime.parse(dateField.getText() + " " + timeField.getText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String description = descArea.getText();
                int maxParticipants = Integer.parseInt(maxPartField.getText());

                if (workshop == null) {
                    workshops.add(new Workshop(nextId++, title, dateTime, description, maxParticipants, new ArrayList<>()));
                    JOptionPane.showMessageDialog(dialog, "Workshop added successfully.");
                } else {
                    workshop.setTitle(title);
                    workshop.setDateTime(dateTime);
                    workshop.setDescription(description);
                    workshop.setMaxParticipants(maxParticipants);
                    JOptionPane.showMessageDialog(dialog, "Workshop updated successfully.");
                }
                refreshTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Check date/time and max participants.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Date (yyyy-MM-dd):"));
        dialog.add(dateField);
        dialog.add(new JLabel("Time (HH:mm):"));
        dialog.add(timeField);
        dialog.add(new JLabel("Description:"));
        dialog.add(descScroll);
        dialog.add(new JLabel("Max Participants:"));
        dialog.add(maxPartField);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private void showParticipantDialog(Workshop workshop) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Manage Participants", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        DefaultListModel<Student> allStudentsModel = new DefaultListModel<>();
        for (Student s : students) {
            allStudentsModel.addElement(s);
        }
        JList<Student> allStudentsList = new JList<>(allStudentsModel);

        DefaultListModel<Student> participantsModel = new DefaultListModel<>();
        for (int id : workshop.getParticipantIds()) {
            students.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .ifPresent(participantsModel::addElement);
        }
        JList<Student> participantsList = new JList<>(participantsModel);

        JButton addButton = new JButton("Add >>");
        JButton removeButton = new JButton("<< Remove");
        JButton closeButton = new JButton("Close");

        addButton.addActionListener(e -> {
            Student selected = allStudentsList.getSelectedValue();
            if (selected != null && !workshop.getParticipantIds().contains(selected.getId())
                && workshop.getParticipantIds().size() < workshop.getMaxParticipants()) {
                workshop.getParticipantIds().add(selected.getId());
                participantsModel.addElement(selected);
                refreshTable();
                JOptionPane.showMessageDialog(dialog, "Participant added successfully.");
            } else if (workshop.getParticipantIds().size() >= workshop.getMaxParticipants()) {
                JOptionPane.showMessageDialog(dialog, "Workshop is full.");
            }
        });

        removeButton.addActionListener(e -> {
            Student selected = participantsList.getSelectedValue();
            if (selected != null) {
                workshop.getParticipantIds().remove(Integer.valueOf(selected.getId()));
                participantsModel.removeElement(selected);
                refreshTable();
                JOptionPane.showMessageDialog(dialog, "Participant removed successfully.");
            }
        });

        closeButton.addActionListener(e -> dialog.dispose());

        JPanel listsPanel = new JPanel(new GridLayout(1, 3));
        listsPanel.add(new JScrollPane(allStudentsList));
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(closeButton);
        listsPanel.add(buttonPanel);
        listsPanel.add(new JScrollPane(participantsList));

        dialog.add(new JLabel("Available Students          Participants", SwingConstants.CENTER), BorderLayout.NORTH);
        dialog.add(listsPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }
}
