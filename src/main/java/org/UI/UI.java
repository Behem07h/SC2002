/**
 * This package contains classes for handling the user interface of the BTO Management System.
 * It provides a dynamic menu system that adapts based on user permissions and configuration files.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.UI;
import org.Users.user;

import java.util.*;

/**
 * The UI class serves as a wrapper for the ui_main class, providing configuration loading
 * and initialization for the user interface. It reads configuration files for UI elements,
 * connections, functions, and visibility settings from specified file paths and initializes
 * the main UI system with these configurations.
 */
public class UI {
    /** The main UI controller instance */
    private final ui_main ui;
    /**
     * Constructs a UI object with the specified configuration root path, user, and scanner.
     *
     * <p>Loads UI configurations from files and initializes the ui_main controller.</p>
     *
     * @param cfg_rt The root path to configuration files
     * @param myuser The authenticated user accessing the system
     * @param sc The Scanner object for reading user input
     */
    public UI(String cfg_rt, user myuser, Scanner sc) {
        ConfigLDR ldr = new ConfigLDR();
        this.ui = new ui_main(ldr.ReadToMap(cfg_rt+"/ui.csv"),
                ldr.ReadToArrMap(cfg_rt+"/ui_connections.csv"),
                ldr.ReadToArrMap(cfg_rt+"/ui_fns.csv"),
                ldr.ReadToArrMap(cfg_rt+"/ui_visibility.csv"),
                myuser, sc);
    }
    /**
     * Loads and starts the user interface.
     * Delegates to the ui_main controller to manage the UI flow.
     */
    public void load_ui() {
        this.ui.load_ui();
    }
}

/**
 * The ui_main class is the core controller for the BTO Management System's user interface.
 *
 * <p>This class manages the UI flow, handles user input, processes function calls, and
 * controls navigation between different UI screens based on configuration settings and user permissions.</p>
 *
 * <p>It maintains context across UI screens and supports dynamic rendering of UI elements
 * with contextual text replacement.</p>
 */
class ui_main {

    /** Map containing UI text for each screen */
    Map<String,String> ui_map;          //text to display in ui page
    /** Map containing functions to execute for each menu option */
    Map<String,String[]> fn_map;        //fns in fn_arr to run when receiving input. may not be the same as the name of the next ui
    /** Map containing navigation connections between UI screens */
    Map<String,String[]> next_ui_map;   //ui menu connections
    /** Map containing visibility settings for menu options based on user permissions */
    Map<String,String[]> vis_map;       //ui option visibility
    /** List of menu options currently visible to the user */
    List<Integer> allowed_options = new ArrayList<>();      //visible options as list

    /** Context object managing application state and user sessions */
    Context context;
    /** List containing contextual data from function execution */
    List<String> ctx;
    /** Temporary list for storing function return values */
    List<String> tmpList;
    /** Identifier of the function fn that the ctx was returned from */
    String ctx_idx; //fn that the ctx was returned from
    /** Temporary string storage */
    String tmp;
    /** Identifier of the name of the ui menu */
    String ui_idx; //the name of the ui menu
    /** Scanner for reading user input */
    Scanner sc;

    /**
     * Constructs a ui_main object with the specified UI configuration maps, user, and scanner.
     *
     * <p>Initializes the UI controller with configuration settings and sets the initial context
     * and UI screen.</p>
     *
     * @param ui_map Map containing UI text for each screen
     * @param next_ui_map Map containing navigation connections between UI screens
     * @param fn_map Map containing functions to execute for each menu option
     * @param vis_map Map containing visibility settings for menu options
     * @param currentUser The authenticated user accessing the system
     * @param sc The Scanner object for reading user input
     */
    public ui_main(Map<String,String> ui_map,Map<String,String[]> next_ui_map,Map<String,String[]> fn_map,Map<String,String[]> vis_map, user currentUser, Scanner sc) {
        //load file path into ui_arr
        this.ui_map = ui_map;
        this.next_ui_map = next_ui_map;
        this.fn_map = fn_map;
        this.vis_map = vis_map;

        this.sc = sc;

    	this.context = new Context(currentUser);
        ctx_idx = fn_map.get("DEFAULT")[0];
        ctx = context.act(ctx_idx,this.sc); //UI context starts on the projects list screen
        ui_idx = next_ui_map.get("DEFAULT")[0]; //defaults can be loaded from file
    }

    /**
     * Starts the UI interaction loop.
     *
     * <p>This method handles rendering UI screens, collecting user input, executing
     * associated functions, and navigating between screens according to the configuration.</p>
     *
     * <p>It also manages visibility of menu options based on user permissions and handles
     * contextual text replacement in the UI.</p>
     */
    public void load_ui() {
        while (true) {
        	String ui_text = ui_map.get(ui_idx).replace("\\n","\n"); //get the current ui text
            for (int i = 0; i < vis_map.get(ui_idx).length; i++) {
                if (vis_map.get(ui_idx)[i].contains(String.valueOf(context.getCurrentUser().getPerms())) || Objects.equals(vis_map.get(ui_idx)[i], "ALL")) {
                    allowed_options.add(i);
                } else {
                    ui_text = ui_text.replaceAll(String.format("\\[%s\\] - ",i+1)+".*"+"\n","");
                    //replace options we dont want the user to see
                }
            }
        	for (int i = 0; i < ctx.size(); i++) {
        		ui_text = ui_text.replace(String.format("{%s[%d]}", ctx_idx, i), ctx.get(i)); //handle contextual text replacement
        	}
            System.out.println(ui_text); //display the ui
            
            int usr_in = sc.nextInt(); //get user input as int
            sc.nextLine(); //clear buffer of newline

            if (!allowed_options.contains(usr_in-1) && usr_in > 0) {
                System.out.printf("[%s] is not a valid option\n", usr_in);
                continue;
            }

            if ((usr_in-1 < next_ui_map.get(ui_idx).length) && (usr_in > 0)) { //convert input to zero index & verify
            	String next_alias = next_ui_map.get(ui_idx)[usr_in - 1]; //get the next menu

                if (ui_map.containsKey(next_alias)) { //check if next menu exists
                    //if the input was valid, run any relevant functions here and save the return values to a ctx local var
                	tmp = fn_map.get(ui_idx)[usr_in-1];
                    tmpList = context.act(tmp,this.sc);
                    if (!tmpList.get(0).trim().isEmpty()) {
                        System.out.println("Query returned blank");
                        ctx_idx = tmp;
                        ctx = tmpList;
                        ui_idx = next_alias; //get the next ui idx
                    }

                } else if (Objects.equals(next_alias, "NIL")) {
                    //if the next menu is NIL, that means the ui doesn't move but fns are run.
                    ctx = context.act(fn_map.get(ui_idx)[usr_in-1],this.sc);
                }
            } else if (usr_in == 0) { //terminate program
                context.endContext();
                return;
            }
        }
    }
}