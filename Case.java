public class Case {
    private int id;
    private int studentId;
    private String description;
    private String status; // e.g., "Open", "Closed", "In Progress"
    private String wellnessMetrics; // e.g., "Stress Level: High, Academic Performance: Average"
    private String followUpNotes;

    public Case(int id, int studentId, String description, String status, String wellnessMetrics, String followUpNotes) {
        this.id = id;
        this.studentId = studentId;
        this.description = description;
        this.status = status;
        this.wellnessMetrics = wellnessMetrics;
        this.followUpNotes = followUpNotes;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getWellnessMetrics() { return wellnessMetrics; }
    public void setWellnessMetrics(String wellnessMetrics) { this.wellnessMetrics = wellnessMetrics; }

    public String getFollowUpNotes() { return followUpNotes; }
    public void setFollowUpNotes(String followUpNotes) { this.followUpNotes = followUpNotes; }
}
