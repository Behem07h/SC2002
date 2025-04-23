/**
 * Package for HDB (Housing Development Board) Officer user management functionality.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.Users.HDBOfficer;


import org.UI.ConfigLDR;
import org.Users.GenericManager;
import org.Users.user;
import org.action.registration.Register;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * HDBOfficer contains personal information about the manager, their authentication
 * credentials, permission level, and information about their currently assigned project.
 *
 */
public class HDBOfficerManager extends GenericManager<user> {

    /**
     * Parses a CSV line into an HDBOfficer object.
     * Expected CSV format: userID,username,password,maritalStatus,age,permissionLevel
     *
     * @param line The CSV line to parse
     * @return A new HDBOfficer object or null if the line cannot be properly parsed
     */
   @Override
   protected HDBOfficer parseUser(String line) {
       String[] details = line.split(",");
       if (details.length != 6) return null;
       for (int i = 0; i < details.length; i++) {
           details[i] = details[i].trim();
       }

       int age = Integer.parseInt(details[4]);
       user.PermissionLevel perms = user.PermissionLevel.valueOf(details[5]);


       // CSV: userID, username, password, maritalStatus, age
       return new HDBOfficer(details[0], details[1], details[2], details[3], age, perms);
   }

    /**
     * Adds a new HDB Officer to the system with default values.
     * This method creates a new HDBOfficer with the provided details and a default
     * password of "password". The permission level is set to OFFICER automatically.
     *
     * @param userID The unique ID for the officer
     * @param username The login username for the officer
     * @param age The officer's age in years
     * @param maritalStatus The officer's marital status (or department information)
     * @param unused2 A parameter that is not used for HDBOfficer creation
     */
   @Override
   public void addUser(String userID, String username, int age, String maritalStatus, String unused2) {
       user.PermissionLevel perms = user.PermissionLevel.OFFICER;
       userDB.add(new HDBOfficer(userID, username, "password", maritalStatus, age, perms));
       System.out.println("HDB Officer added: " + userID);
   }
}
