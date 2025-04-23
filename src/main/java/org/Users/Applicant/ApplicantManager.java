/**
 * The ApplicantManager class is responsible for managing Applicant objects.
 *
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.Users.Applicant;


import org.Users.GenericManager;
import org.Users.user;

/**
 * It extends the GenericManager class and provides specific implementations
 * for parsing and adding Applicant users to the system.
 */
public class ApplicantManager extends GenericManager<user> {
    /**
     * Parses a CSV line into an Applicant object.
     * Expected CSV format: userID,username,password,maritalStatus,age,permissionLevel
     *
     * @param line The CSV line to parse
     * @return A new Applicant object if parsing succeeds, null otherwise
     */
   @Override
   protected Applicant parseUser(String line) {
       String[] details = line.split(",");
       if (details.length != 6) return null;
       for (int i = 0; i < details.length; i++) {
           details[i] = details[i].trim();
       }


       int age = Integer.parseInt(details[4]);
       user.PermissionLevel perms = user.PermissionLevel.valueOf(details[5]);


       // CSV: userID, username, password, maritalStatus, age
       return new Applicant(details[0], details[1], details[2], details[3], age, perms);
   }

    /**
     * Adds a new Applicant to the user database.
     * The password is set to "password" by default, and permission level is set to APPLICANT.
     *
     * @param userID The unique ID for the new applicant
     * @param username The username for the new applicant
     * @param age The age of the new applicant (must be non-negative)
     * @param maritalStatus The marital status of the new applicant
     * @param unused An unused parameter (maintained for compatibility with parent class)
     */
   @Override
   public void addUser(String userID, String username, int age, String maritalStatus, String unused) {
       if (age < 0) {
           System.out.println("Invalid age.");
           return;
       }
       user.PermissionLevel perms = user.PermissionLevel.APPLICANT;
       userDB.add(new Applicant(userID, username, "password", maritalStatus, age, perms));
       System.out.println("Applicant added: " + userID);
   }
}
