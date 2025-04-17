package org.UI;

import org.Users.user;
import org.action.ApplicationManager;
import org.action.enquiry.EnquiriesManager;

import java.util.Scanner;

public class Context {
    private final EnquiriesManager enqMan;
    private final ApplicationManager appMan;
    private final user currentUser;
    public Context(user currentUser) {
        this.currentUser = currentUser;
        enqMan = new EnquiriesManager();
        appMan = new ApplicationManager();
    }

    public void endContext() {
        enqMan.storeEnquiries();
        appMan.storeApplications();
    }

    public String[] act(String action, Scanner sc) {
        return currentUser.act(action, sc, enqMan, appMan);
    }

    public user getCurrentUser() {
        return currentUser;
    }

    public ApplicationManager getAppMan() {
        return appMan;
    }

    public EnquiriesManager getEnqMan() {
        return enqMan;
    }
}
