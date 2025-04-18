package org.action.enquiry;

import java.time.LocalDateTime;

public class Enquiries {
    private String text;
    private String reply;
    private final String id;
    private final LocalDateTime timestamp;
    private final String userId;
    private final String projectID;

    public Enquiries(String id, String projectID, String userId, String text, String reply, LocalDateTime timestamp) {
        this.text = text;
        this.id = id;
        this.reply = reply;
        this.timestamp = timestamp;
        this.userId = userId;
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
    public String getId() {
        return id;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getUserId() {
        return userId;
    }
    public String getProjectID() {
        return projectID;
    }
    @Override
    public String toString() {
        return "Enquiry ID: " + id + " by " + userId + " at " + timestamp + "for Project ID: " + projectID + "\n"
                + "Question is " + text + "\n" +
                (reply.isEmpty() ? "[No reply yet]" : "Reply: " + reply);
    }
}
