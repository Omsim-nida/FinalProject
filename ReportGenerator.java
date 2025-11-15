import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerator extends JPanel implements ActionListener {
    private JTextArea reportArea;

    public JTextArea getReportArea() {
        return reportArea;
    }
    private JButton generateAppointmentsReport, generateCasesReport, generateWorkshopsReport, exportReport;
    private List<Appointment> appointments;
    private List<Case> cases;
    private List<Workshop> workshops;
    private List<Student> students;

    public ReportGenerator(List<Student> students, List<Appointment> appointments, List<Case> cases, List<Workshop> workshops) {
        this.students = students;
        this.appointments = appointments;
        this.cases = cases;
        this.workshops = workshops;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        reportArea = new JTextArea(20, 50);
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        generateAppointmentsReport = new JButton("Appointments Report");
        generateCasesReport = new JButton("Cases Report");
        generateWorkshopsReport = new JButton("Workshops Report");
        exportReport = new JButton("Export Report");

        generateAppointmentsReport.addActionListener(this);
        generateCasesReport.addActionListener(this);
        generateWorkshopsReport.addActionListener(this);
        exportReport.addActionListener(this);

        buttonPanel.add(generateAppointmentsReport);
        buttonPanel.add(generateCasesReport);
        buttonPanel.add(generateWorkshopsReport);
        buttonPanel.add(exportReport);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateAppointmentsReport) {
            generateAppointmentsReport();
        } else if (e.getSource() == generateCasesReport) {
            generateCasesReport();
        } else if (e.getSource() == generateWorkshopsReport) {
            generateWorkshopsReport();
        } else if (e.getSource() == exportReport) {
            exportReport();
        }
    }

    private void generateAppointmentsReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Appointments Report ===\n\n");
        report.append("Total Appointments: ").append(SharedData.totalAppointments).append("\n\n");

        for (Appointment appt : appointments) {
            Student student = students.stream()
                .filter(s -> s.getId() == appt.getStudentId())
                .findFirst()
                .orElse(null);
            String studentName = student != null ? student.getName() : "Unknown";
            String studentEmail = student != null ? student.getEmail() : "Unknown";
            String studentId = student != null ? student.getStudentId() : "Unknown";
            report.append("ID: ").append(appt.getId()).append("\n");
            report.append("Student Name: ").append(studentName).append("\n");
            report.append("Student Email: ").append(studentEmail).append("\n");
            report.append("Student ID: ").append(studentId).append("\n");
            report.append("Counselor: ").append(appt.getCounselor()).append("\n");
            report.append("Date & Time: ").append(appt.getDateTime()).append("\n");
            report.append("Status: ").append(appt.getStatus()).append("\n");
            report.append("Notes: ").append(appt.getNotes()).append("\n\n");
        }

        reportArea.setText(report.toString());
    }

    private void generateCasesReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Cases Report ===\n\n");
        report.append("Total Cases: ").append(SharedData.totalCases).append("\n\n");

        for (Case c : cases) {
            Student student = students.stream()
                .filter(s -> s.getId() == c.getStudentId())
                .findFirst()
                .orElse(null);
            String studentName = student != null ? student.getName() : "Unknown";
            String studentEmail = student != null ? student.getEmail() : "Unknown";
            String studentId = student != null ? student.getStudentId() : "Unknown";
            report.append("ID: ").append(c.getId()).append("\n");
            report.append("Student Name: ").append(studentName).append("\n");
            report.append("Student Email: ").append(studentEmail).append("\n");
            report.append("Student ID: ").append(studentId).append("\n");
            report.append("Description: ").append(c.getDescription()).append("\n");
            report.append("Status: ").append(c.getStatus()).append("\n");
            report.append("Wellness Metrics: ").append(c.getWellnessMetrics()).append("\n");
            report.append("Follow-up Notes: ").append(c.getFollowUpNotes()).append("\n\n");
        }

        reportArea.setText(report.toString());
    }

    private void generateWorkshopsReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Workshops Report ===\n\n");
        report.append("Total Workshops: ").append(SharedData.totalWorkshops).append("\n\n");

        for (Workshop w : workshops) {
            report.append("ID: ").append(w.getId()).append("\n");
            report.append("Title: ").append(w.getTitle()).append("\n");
            report.append("Date & Time: ").append(w.getDateTime()).append("\n");
            report.append("Description: ").append(w.getDescription()).append("\n");
            report.append("Max Participants: ").append(w.getMaxParticipants()).append("\n");
            report.append("Current Participants: ").append(w.getParticipantIds().size()).append("\n");
            report.append("Participants:\n");
            for (int id : w.getParticipantIds()) {
                Student student = students.stream()
                    .filter(s -> s.getId() == id)
                    .findFirst()
                    .orElse(null);
                String name = student != null ? student.getName() : "Unknown";
                String email = student != null ? student.getEmail() : "Unknown";
                String studentId = student != null ? student.getStudentId() : "Unknown";
                report.append("  - Name: ").append(name).append(", Email: ").append(email).append(", Student ID: ").append(studentId).append("\n");
            }
            report.append("\n");
        }

        reportArea.setText(report.toString());
    }

    private void exportReport() {
        String reportText = reportArea.getText();
        if (reportText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No report to export. Generate a report first.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("report.txt"));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(reportText);
                JOptionPane.showMessageDialog(this, "Report exported successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting report: " + e.getMessage());
            }
        }
    }
}
