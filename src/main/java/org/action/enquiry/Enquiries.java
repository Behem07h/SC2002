package org.action.enquiry;

import java.time.LocalDateTime;
import java.util.Objects;

public class Enquiries {
    private String text;
    private String reply;
    private final String ID;
    private final LocalDateTime timestamp;
    private final String userID;
    private final String username;
    private final String projectID;

    public Enquiries(String id, String projectID, String userId, String username, String text, String reply, LocalDateTime timestamp) {
        this.text = text;
        this.ID = id;
        this.reply = reply;
        this.timestamp = timestamp;
        this.userID = userId;
        this.username = username;
        this.projectID = projectID;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getReply() {
        return reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }
    public String getID() {
        return ID;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getUserID() {
        return userID;
    }
    public String getProjectID() {
        return projectID;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        String shortText;
        if (text.length() > 50) {
            shortText = text.substring(0,47) + "...";
        } else {
            shortText = text;
        }
        return String.format("Enquiry ID: %s | Posted by: %s\nQ: %s\n%s", ID, username, shortText, (reply.isEmpty() ? "[No reply]" : "[Answered]"));
    }
    public String view_full() {
        return String.format("Enquiry ID: %s | Posted by: %s at %s\nQ: %s\nR: %s", ID, username, timestamp, text, (reply.isEmpty() ? "[No reply]" : reply));
    }

    public boolean filter(String userId, String projectId, String enquiryId) {
        boolean out = true;
        if (!userId.isEmpty()) {
            out = Objects.equals(this.userID, userId);
        }
        if (!projectId.isEmpty()) {
            out = Objects.equals(this.projectID, projectId);
        }
        if (!enquiryId.isEmpty()) {
            out = out && Objects.equals(this.ID, enquiryId);
        }
        return out;
    }
}
