package org.HDBManager;

public interface user {
    
    String getUserID();

    String getUsername();

    String getPassword();

    String getMaritalStatus();

    int getAge();

    void setUserID(String userID);

    void setUsername(String username);

    void setPassword(String password);

    void setMaritalStatus(String maritalStatus);

    void setAge(int age);

    boolean changePassword(String oldPassword, String newPassword);
}

