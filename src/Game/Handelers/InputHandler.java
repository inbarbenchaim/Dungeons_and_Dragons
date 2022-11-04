package Game.Handelers;

import Game.DatabaseUnits;
import View.Input.InputProvider;

import java.util.Scanner;

public class InputHandler {

    private static String userInputRegex = "[" + InputProvider.CastAbility.getRegex() + "]";
    private static String menuRegex = "[1-" + DatabaseUnits.playerPool.size() + "]";

    // get the player choice
    public static InputProvider inputPlayerGame() {
        return InputProvider.FindByKey(inputCache(userInputRegex) + "");
    }

    // upload the options of the moves
    public static char inputMenu() {
        return inputCache(menuRegex);
    }

    // return the char of the input for the move
    private static char inputCache(String regex) {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        String input = "";
        do {
            input = myObj.nextLine();
        } while (!validateWithRegex(input, regex));
        return input.charAt(0);
    }

    // check if the move input is valid
    private static Boolean validateWithRegex(String s, String regex) {
        if (s.length() == 1)
            if (s.matches(regex))
                return true;
        return false;
    }
}