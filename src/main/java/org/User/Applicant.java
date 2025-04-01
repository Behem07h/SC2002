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
}
