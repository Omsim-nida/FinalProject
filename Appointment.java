import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private int studentId;
    private String counselor;
    private LocalDateTime dateTime;
    private String status; // e.g., "Scheduled", "Completed", "Cancelled"
    private String notes;

    public Appointment(int id, int studentId, String counselor, LocalDateTime dateTime, String status, String notes) {
        this.id = id;
        this.studentId = studentId;
        this.counselor = counselor;
        this.dateTime = dateTime;
        this.status = status;
        this.notes = notes;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getCounselor() { return counselor; }
    public void setCounselor(String counselor) { this.counselor = counselor; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
