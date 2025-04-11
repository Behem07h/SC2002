/*
package org.UI;
import org.Applicant.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UI {
    public static void main(String[] args) {
        ui_main ui = new ui_main(" ");
        ui.load_ui();
    }
}


class ui_main {
    int idx = 0;
    int idx_in = 0;
    String[] ui_arr = {
    		"Projects: \n{Project_List[0]}\n\n[1] - View a project\n[0] - Log out",
    		"Project details:\n{Project_Details[0]}\n\n[3] - Send application\n[2] - Enquiries\n[1] - Back\n[0] - Log out",
    		"c \n1 to go to a, 2 to go to b"
    }; //text to display in ui page
    
    String[][] ui_arr2 = {
    		{"Project_List", "Projects: \n{Project_List[0]}\n\n[1] - View a project\n[0] - Log out"},
    		{"Project_Details","Project details:\n{Project_Details[0]}\n\n[3] - Send application\n[2] - Enquiries\n[1] - Back\n[0] - Log out"},
    		{"next","c \n1 to go to a, 2 to go to b"}
    }; //text to display in ui page
    
    Map<String,String> ui_map = parse(ui_arr2);
    //todo: use format strings to display variable text

    //inputs are all 1 indexed as 0 is reserved for quitting
    String[][] ui_fns = {{"Project_Details"},{"Project_List","Project_Enquiries","Send_Application"},{"-1","-1"}}; //fns in fn_arr to run when receiving input, -1 if none
    //todo: add code to make the fns run when switching ui menus, and for taking inputs
    int[][] next_ui = {{1},{0,2,-1},{0,1}}; //ui in ui_arr to go to next when receiving input, -1 if no change
    String[][] next_ui2 = {{"Project_Details"},{"Project_List","next",""},{"Project_List","Project_Details"}};

    user currentUser;
    String[] ctx = {};
    String ctx_idx = "";
    
    public Map<String, String> parse(String[][] mylist) { //convert array into map
    	Map<String,String> map = new HashMap<String, String>();
    	for (String[] row : mylist) {
    		String key = row[0];
    		String item = row[1];
    		map.put(key, item);
    	}
    	return map;
    }

    public ui_main(String filepath) {
        //load file path into ui_arr
    	//get user 
    	ctx = currentUser.act("Project_List"); //UI context starts on the projects list screen
        ctx_idx = "Project_List";
    }

    public void load_ui() {
        Scanner sc = new Scanner(System.in);
        
        while (true) {
        	String ui_text = ui_arr[idx];
        	for (int i = 0; i < ctx.length; i++) {
        		ui_text = ui_text.replace(String.format("{%s[%d]}", ctx_idx, i), ctx[i]);
        	}
            System.out.println(ui_text);
            int idx_in = sc.nextInt();
            if ((idx_in-1 < next_ui[idx].length) && (idx_in > 0)) { //convert input to zero index & verify
                if (next_ui[idx][idx_in-1] >= 0) {
                    //if the input was valid, run any relevant functions here
                    // and save the return values to a ctx local var
                	ctx_idx = ui_fns[idx][idx_in-1];
                    ctx = currentUser.act(ctx_idx);
                    idx = next_ui[idx][idx_in-1]; //get the next ui idx
                }
            } else if (idx_in == 0) { //terminate program
                return;
            }
        }
    }
}
*/