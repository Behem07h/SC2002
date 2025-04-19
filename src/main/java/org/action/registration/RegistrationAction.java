package org.action.registration;
import org.Users.user;

public interface RegistrationAction{
    void approveRegistration(Register registration, user usr);
    void rejectRegistration(Register registration, user usr);
    void registerProject(Register registration, user usr);
    void viewRegistrationProject(Register registration, user usr);
}


