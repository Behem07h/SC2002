/**
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.action.enquiry;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents the enquiry in the BTO system.
 * This class stores information about the enquiry, which include its content,
 * and related metadata such as timestamps and user information.
 */
public class Enquiries {
    /** The text content of the enquiry */
    private String text;

    /** The reply to the enquiry*/
    private String reply;

    /** The unique identifier for the enquiry */
    private final String ID;

    /** The timestamp when the enquiry was created */
    private final LocalDateTime timestamp;

    /** The user ID of the person who created the enquiry */
    private final String userID;

    /** The username of the person who created the enquiry */
    private final String username;

    /** The project ID for this enquiry */
    private final String projectID;

    /**
     * Constructor for creating a new enquiry object.
     *
     * @param id The unique ID for the enquiry
     * @param projectID The project ID for this enquiry
     * @param userId The user ID of the person who created the enquiry
     * @param username The username of the person who created the enquiry
     * @param text The text content of the enquiry
     * @param reply The reply to the enquiry
     * @param timestamp The timestamp when the enquiry was created
     */
    public Enquiries(String id, String projectID, String userId, String username, String text, String reply, LocalDateTime timestamp) {
        this.text = text;
        this.ID = id;
        this.reply = reply;
        this.timestamp = timestamp;
        this.userID = userId;
        this.username = username;
        this.projectID = projectID;
    }

    /**
     * Gets the text content of the enquiry.
     *
     * @return The text content of the enquiry
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text content of the enquiry.
     *
     * @param text The new text content for the enquiry
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the reply to the enquiry.
     *
     * @return The reply text
     */
    public String getReply() {
        return reply;
    }

    /**
     * Sets the reply to the enquiry.
     *
     * @param reply The reply text to set
     */
    public void setReply(String reply) {
        this.reply = reply;
    }

    /**
     * Gets the unique identifier for the enquiry.
     *
     * @return The unique ID of the enquiry
     */
    public String getID() {
        return ID;
    }

    /**
     * Gets the timestamp when the enquiry was created.
     *
     * @return The creation timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the user ID of the person who created the enquiry.
     *
     * @return The user ID of the creator
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Gets the project ID that this enquiry is related to.
     *
     * @return The project ID
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Gets the username of the person who created the enquiry.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns a string representation of the enquiry with a shortened text.
     * Used for displaying enquiries in lists.
     *
     * @return A formatted string representation of the enquiry
     */
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

    /**
     * Returns a full string representation of the enquiry including the complete text and reply.
     *
     * @return A formatted string with the complete enquiry details
     */
    public String view_full() {
        return String.format("Enquiry ID: %s | Posted by: %s at %s\nQ: %s\nR: %s", ID, username, timestamp, text, (reply.isEmpty() ? "[No reply]" : reply));
    }

    /**
     * Filters the enquiry based on the provided parameters.
     *
     * @param userId Filter by user ID (empty string to ignore this filter)
     * @param projectId Filter by project ID (empty string to ignore this filter)
     * @param enquiryId Filter by enquiry ID (empty string to ignore this filter)
     * @return true if the enquiry matches the filter criteria, false otherwise
     */
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
