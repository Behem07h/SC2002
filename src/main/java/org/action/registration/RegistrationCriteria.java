package org.action.registration;

import java.util.Scanner;

public class RegistrationCriteria {

    Scanner scanner = new Scanner(System.in);

    // public RegistrationCriteria(boolean noIntention, boolean notHDBofficer){
    //     this.notHDBofficer = notHDBofficer;
    // }

    public boolean noIntention(String user, String projectID){
        System.out.println("Do you intend to apply for the project as an Applicant?");
        String intention = scanner.nextLine();

        if(intention == "No"){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean notHDBofficer(String user, String projectID){
        //
    }
}