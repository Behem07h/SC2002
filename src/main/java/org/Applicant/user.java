package org.Applicant;

public interface user {
    
    String getUserID();

    void setUserID(String userID);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getMaritalStatus();

    void setMaritalStatus(String maritalStatus);

    int getAge();

    void setAge(int age);


    boolean changePassword(String oldPassword, String newPassword);

    String[] act(String action); //wrapper fn that handles user inputs for all actions
}


