package org.UI;
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
            "Projects: \nProject 1\nProject2\nProject3\n\n[1] - View a project\n[0] - Log out",
            "Project details:\n%s\n\n[3] - Send application\n[2] - Enquiries\n[1] - Back\n[0] - Log out",
            "c \n1 to go to a, 2 to go to b"
    }; //text to display in ui page
    //todo: use format strings to display variable text

    //inputs are all 1 indexed as 0 is reserved for quitting
    int[][] ui_fns = {{0},{-1,1,2},{-1,-1}}; //fns in fn_arr to run when receiving input, -1 if none
    //todo: add code to make the fns run when switching ui menus, and for taking inputs
    int[][] next_ui = {{1},{0,2,-1},{0,1}}; //ui in ui_arr to go to next when receiving input, -1 if no change
    Runnable[] fn_arr = new Runnable[10]; //arr of ui-exposed fns
    //fn_arr[0] = () -> number(); //run with fn_arr[0].run() //use a case statement if this doesnt work
    //this only really works if actions are derived from some actor class which handles account perms as an input

    public ui_main(String filepath) {
        //load file path into ui_arr
    }

    public void load_ui() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(ui_arr[idx]);
            int idx_in = sc.nextInt();
            if ((idx_in-1 < next_ui[idx].length) && (idx_in > 0)) { //convert input to zero index & verify
                if (next_ui[idx][idx_in-1] >= 0) {
                    idx = next_ui[idx][idx_in-1]; //get the next ui idx
                }
            } else if (idx_in == 0) { //terminate program
                return;
            }
        }
    }
}