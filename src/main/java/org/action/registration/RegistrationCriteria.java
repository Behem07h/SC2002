package org.action.registration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RegistrationCriteria {

    Scanner scanner = new Scanner(System.in);

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
//<<<<<<< HEAD //todo: what is this line for?
        String projectsFile = "projects.csv";
        List<List<String>> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(projectsFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                List<String> lineData = Arrays.asList(values);
                data.add(lineData);

                if (lineData.size() > 12) {
                    String[] officers = lineData.get(12).split(",");
                    for (String officer : officers) {
                        if (officer.trim().equalsIgnoreCase(user)) {
                            return false; // not a HDB officer for another project
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return true; // currently a HDB officer for another project
//======= //todo: what is this line for?
//        return true; //todo: add logic //unreachable statement
//>>>>>>> ae274b8892baff3ea01b9d6890431c11c6e661d7 //======= //todo: what is this line for?
    }
}