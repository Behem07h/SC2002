package org.User;
import java.util.ArrayList;
import java.util.List;

public class Applicant implements User {
    private String usergroup;
    private String userID;
    private String password;
    private int age;
    private String martialStatus;
    private String applicationStatus;
    private String bookedFlatType;

    public Applicant(String userID, String password, int age, String martialStatus) {
        this.userID = userID;
        this.password = password;
        this.age = age;
        this.martialStatus = martialStatus;

        this.usergroup = martialStatus.equalsIgnoreCase("Single") ? "Single" : "Married";
        this.applicationStatus = null;
        this.bookedFlatType = null;
    }
    public String getUsergroup() {
        return usergroup;
    }
    public int getAge() {
        return age;
    }
    public String getMartialStatus() {
        return martialStatus;
    }
    public String getUserID() {
        return userID;
    }
    public String getApplicationStatus() {
        return applicationStatus;
    }
    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    public String getBookedFlatType() {
        return bookedFlatType;
    }
    public void setBookedFlatType(String bookedFlatType) {
        this.bookedFlatType = bookedFlatType;
    }
    public void changePassword(String newpassword) {
        this.password = newpassword;
    }
}
