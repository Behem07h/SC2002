/**
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.Users.HDBManager;


import org.Users.GenericManager;
import org.Users.user;

/**
 * The ManagerController class extends GenericManager to provide specific functionality
 * for managing HDB managers in the system. It handles user parsing from storage and
 * adding new managers to the system.
 *
 */
public class ManagerController extends GenericManager<user> {
    /**
     * Parses a line of text into an HDBManager object.
     * Expected CSV format: userID,username,password,maritalStatus,age,permissionLevel,activeProjectId
     *
     * @param line The comma-separated string containing manager details
     * @return A new HDBManager object created from the parsed data, or null if the format is invalid
     */
    @Override
   protected HDBManager parseUser(String line) {
       String[] details = line.split(",");
       if (details.length != 6) return null;
       for (int i = 0; i < details.length; i++) {
           details[i] = details[i].trim();
       }

       int age = Integer.parseInt(details[4]);
       user.PermissionLevel perms = user.PermissionLevel.valueOf(details[5]);

       // CSV: userID, username, password
       return new HDBManager(details[0], details[1], details[2], details[3], age, perms);
   }

    /**
     * Parses a HDBManager object into a String[].
     * String[] format: user ID, username,password,maritalStatus,age,permissionLevel
     *
     * @param usr The user object to parse
     * @return A String[], or null if parsing fails
     */
    @Override
    protected String[] toStrList(user usr) {
        return new String[]{usr.getUserID(), usr.getUsername(), usr.getPassword(), usr.getMaritalStatus(), String.valueOf(usr.getAge()), String.valueOf(usr.getPerms())};
    }
    /**
     * Adds a new manager to the user database with default permission level MANAGER
     * and an empty active project ID.
     *
     * @param userID The unique ID for the new manager
     * @param username The username for the new manager
     * @param age The age of the new manager
     * @param maritalStatus The marital status of the new manager
     * @param unused2 An unused parameter (maintained for compatibility with parent class)
     */
   @Override
   public void addUser(String userID, String username, int age, String maritalStatus, String unused2) {
       user.PermissionLevel perms = user.PermissionLevel.MANAGER;
       userDB.add(new HDBManager(userID, username, "password", maritalStatus, age, perms));
       System.out.println("Manager added: " + userID);
   }
}
