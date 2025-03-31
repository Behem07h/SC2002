package org.example;

import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        ui_main ui = new ui_main("a");
        ui.load_ui();
    }
}

//ui main class - runs ui in a loop, calling menus based on a LUT which the next menu returns the index of
//class fns as array of submenu args - display text, selection values, return values
//generic submenu fn that takes these args to display a menu with interactivity

class ui_main {
    Integer idx = 0;
    Integer idx_in = 0;
    String[] ui_arr = {
            "a \n0 to go to b, 1 to go to c",
            "b \n0 to go to c, 1 to go to a",
            "c \n0 to go to a, 1 to go to b"
    }; //text to display in ui page
    Integer[][] ui_fns = {{0,0},{1,2},{2,1}}; //fns in fn_arr to run when receiving input
    Integer[][] next_ui = {{1,2},{2,0},{0,1}}; //ui in ui_arr to go to next when receiving input, -1 if no change
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
            idxin = sc.nextInt();
            if (idxin < next_ui[idx].length) {
                if (next_ui[idx][idx_in] >= 0) {
                    idx = next_ui[idx][idx_in]; //get the next ui idx
                }
            } else if (idxin == 5) { //terminate program (how can we make this take a -1?)
                return;
            }
        }
    }

}