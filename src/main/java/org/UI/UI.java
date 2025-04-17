package org.UI;
import org.Users.user;

import java.util.Map;
import java.util.Scanner;

public class UI {
    private final ui_main ui;
    public UI(String cfg_rt, user myuser, Scanner sc) {
        ConfigLDR ldr = new ConfigLDR();
        this.ui = new ui_main(ldr.ReadToMap(cfg_rt+"/ui.csv"),
                ldr.ReadToArrMap(cfg_rt+"/ui_connections.csv"),
                ldr.ReadToArrMap(cfg_rt+"/ui_fns.csv"),
                myuser, sc);
    }
    public void load_ui() {
        this.ui.load_ui();
    }
}


class ui_main {

    Map<String,String> ui_map;          //text to display in ui page
    //inputs are all 1 indexed as 0 is reserved for quitting
    Map<String,String[]> fn_map;        //fns in fn_arr to run when receiving input. may not be the same as the name of the next ui
    Map<String,String[]> next_ui_map;   //ui menu connections

    user currentUser;
    String[] ctx;
    String ctx_idx; //fn that the ctx was returned from
    String ui_idx; //the name of the ui menu
    Scanner sc;

    public ui_main(Map<String,String> ui_map,Map<String,String[]> next_ui_map,Map<String,String[]> fn_map, user currentUser, Scanner sc) {
        //load file path into ui_arr
        this.ui_map = ui_map;
        this.next_ui_map = next_ui_map;
        this.fn_map = fn_map;

        this.sc = sc;

    	this.currentUser = currentUser;
    	ctx = this.currentUser.act("Project_List"); //UI context starts on the projects list screen
        ctx_idx = "Project_List";
        ui_idx = "Project_List"; //defaults can be loaded from file
    }

    public void load_ui() {
        while (true) {
        	String ui_text = ui_map.get(ui_idx).replace("\\n"
                    ,"\n"); //get the current ui text

        	for (int i = 0; i < ctx.length; i++) {
        		ui_text = ui_text.replace(String.format("{%s[%d]}", ctx_idx, i), ctx[i]); //handle contextual text replacement
        	}
            System.out.println(ui_text); //display the ui
            
            int usr_in = sc.nextInt(); //get user input as int

            if ((usr_in-1 < next_ui_map.get(ui_idx).length) && (usr_in > 0)) { //convert input to zero index & verify
            	String next_alias = next_ui_map.get(ui_idx)[usr_in - 1]; //get the next menu

                if (ui_map.containsKey(next_alias)) { //check if next menu exists
                    //if the input was valid, run any relevant functions here and save the return values to a ctx local var
                	ctx_idx = fn_map.get(ui_idx)[usr_in-1];
                    ctx = currentUser.act(ctx_idx);
                    ui_idx = next_alias; //get the next ui idx
                }
            } else if (usr_in == 0) { //terminate program
                return;
            }
        }
    }
}