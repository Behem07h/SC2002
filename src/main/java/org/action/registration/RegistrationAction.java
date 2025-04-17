package org.action.registration;

public interface RegistrationAction{
    void approveRegistration(Register registration);
    void rejectRegistration(Register registration);
    void registerProject(Register registration);
    void viewRegistrationProject(Register registration);
}


