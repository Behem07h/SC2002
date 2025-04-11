package org.Applicant;

public class RegularApplicant implements Applicant {
    private String userID;
    private String password;
    private int age;
    private String maritalStatus;
    private ApplicantPermissions perms;

    public RegularApplicant(String userID, String password, int age, String maritalStatus) {
        this.userID = userID;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.perms = new ApplicantPermissions(0); // Default user type: applicant
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public ApplicantPermissions getPermissions() {
        return perms;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Incorrect old password.");
        }
    }

    @Override
    public String toString() {
        return "UserID: " + userID + ", Age: " + age + ", Marital Status: " + maritalStatus;
    }
}