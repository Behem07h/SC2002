package org.UI;
import org.Users.user;

import java.util.*;

public class UI {
    private final ui_main ui;
    public UI(String cfg_rt, user myuser, Scanner sc) {
        ConfigLDR ldr = new ConfigLDR();
        this.ui = new ui_main(ldr.ReadToMap(cfg_rt+"/ui.csv"),
                ldr.ReadToArrMap(cfg_rt+"/ui_connections.csv"),
                ldr.ReadToArrMap(cfg_rt+"/ui_fns.csv"),
                ldr.ReadToArrMap(cfg_rt+"/ui_visibility.csv"),
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
    Map<String,String[]> vis_map;       //ui option visibility
    List<Integer> allowed_options = new ArrayList<>();      //visible options as list

    Context context;
    List<String> ctx;
    List<String> tmpList;
    String ctx_idx; //fn that the ctx was returned from
    String tmp;
    String ui_idx; //the name of the ui menu
    Scanner sc;

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