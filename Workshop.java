import java.time.LocalDateTime;
import java.util.List;

public class Workshop {
    private int id;
    private String title;
    private LocalDateTime dateTime;
    private String description;
    private int maxParticipants;
    private List<Integer> participantIds; // List of student IDs

    public Workshop(int id, String title, LocalDateTime dateTime, String description, int maxParticipants, List<Integer> participantIds) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.participantIds = participantIds;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public List<Integer> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<Integer> participantIds) { this.participantIds = participantIds; }
}
