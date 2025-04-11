package org.action.enquiry;
import org.action.*;

import java.time.LocalDateTime;

public class Enquiries {
    private String text;
    private String reply;
    private int id;
    private LocalDateTime timestamp;
    private String username;
    private String projectID;

    public Enquiries(String text, int id, String reply, LocalDateTime timestamp, String username, String projectID) {
        this.text = text;
        this.id = id;
        this.reply = reply;
        this.timestamp = timestamp;
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
    public int getId() {
        return id;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getUsername() {
        return username;
    }
    public String getProjectID() {
        return projectID;
    }
    @Override
    public String toString() {
        return "Enquiry ID: " + id + " by " + username + " at " + timestamp + "for Project ID: " + projectID + "\n"
                + "Question is " + text + "\n" +
                (reply.isEmpty() ? "[No reply yet]" : "Reply: " + reply);
    }
}
