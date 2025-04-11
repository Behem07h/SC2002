package org.UI;
import org.Applicant.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UI2 {
    private ui_main ui;
    public UI2(String path, user myuser, Scanner sc) {
        this.ui = new ui_main(path, myuser, sc);
    }
    public void load_ui() {
        this.ui.load_ui();
    }
}


class ui_main {
    String[][] ui_arr = { //key, text
    		{"Project_List", "Projects: \n{Project_List[0]}\n\n[2] - View application\n[1] - View a project\n[0] - Log out"},
    		{"Project_Details","Project details:\n{Project_Details[0]}\n\n[3] - Send application\n[2] - Enquiries\n[1] - Back\n[0] - Log out"},
    		{"Project_Enquiries","Project Enquiries:\nProject: {List_Enquiries[0]}\n{List_Enquiries[1]}\n\n[3] - View Enquiry\n[2] - Submit Enquiry\n[1] - Back\n[0] - Log out"},
            {"Enquiry_View","Enquiry Details:\nProject: {View_Enquiry[0]}\n{View_Enquiry[1]}\n\n[3] - Delete Enquiry\n[2] - Edit Enquiry\n[1] - Back\n[0] - Log out"},
            {"Application_View","Application Status:\n{View_Application[0]}\n\n[3] - Submit Flat Booking\n[2] - Request Application Withdrawal\n[1] - Back\n[0] - Log out"}
    }; //text to display in ui page
    Map<String,String> ui_map = parse(ui_arr);

    //inputs are all 1 indexed as 0 is reserved for quitting
    String[][][] ui_fns = {
    		{{"Project_List"},{"Project_Details","View_Application"}},
    		{{"Project_Details"},{"List_Projects","List_Enquiries","Send_Application"}},
    		{{"Project_Enquiries"},{"Project_Details","Submit_Enquiry","View_Enquiry"}},
            {{"Enquiry_View"},{"List_Enquiries","Edit_Enquiry","Delete_Enquiry"}},
            {{"Application_View"},{"List_Projects","Withdraw_Application","Submit_Booking"}}
    }; 
    Map<String,String[]> fn_map = parse(ui_fns);
    //fns in fn_arr to run when receiving input. may not be the same as the name of the next ui
    //
    String[][][] next_ui = {
    		{{"Project_List"},{"Project_Details","Application_View"}}, //key, values[]
    		{{"Project_Details"},{"Project_List","Project_Enquiries","NIL"}},
    		{{"Project_Enquiries"},{"Project_Details","NIL"}},
            {{"Enquiry_View"},{"Project_Enquiries","NIL","NIL"}},
            {{"Application_View"},{"Project_List","NIL","NIL"}}
    };
    Map<String,String[]> next_ui_map = parse(next_ui);
    

    user currentUser;
    String[] ctx;
    String ctx_idx; //fn that the ctx was returned from
    String ui_idx; //the name of the ui menu
    Scanner sc;
    
    public Map<String, String> parse(String[][] my_list) { //convert array into map
    	Map<String,String> map = new HashMap<>();
    	for (String[] row : my_list) {
    		String key = row[0];
    		String item = row[1];
    		map.put(key, item);
    	}
    	return map;
    }
    public Map<String, String[]> parse(String[][][] my_list) { //convert array into map
    	Map<String,String[]> map = new HashMap<>();
    	for (String[][] row : my_list) {
    		String key = row[0][0];
    		String[] item = row[1];
    		map.put(key, item);
    	}
    	return map;
    }

    public ui_main(String filepath, user currentUser, Scanner sc) {
        //load file path into ui_arr
        this.sc = sc;
    	this.currentUser = currentUser;
    	ctx = this.currentUser.act("Project_List"); //UI context starts on the projects list screen
        ctx_idx = "Project_List";
        ui_idx = "Project_List"; //defaults can be loaded from file
    }

    public void load_ui() {
        while (true) {
        	String ui_text = ui_map.get(ui_idx); //get the current ui text

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