package GameLogic;

import java.util.Random;

/**
 * a class that makes a computer player to play on the server (ordered tactic, put marks in an order 0-36)
 */
public class ServerOrdered implements Strategy {

    /**
     * a variable of type String that states the name of the tactic
     */
    private final String name = "SERVER STRUCTURED";

    /**
     * a variable of type int[] that has an empty board which is used to determine the next move
     */
    int[] emptyFields = new int[36];

    /**
     * a getter method that returns the name of the strategy
     * @return name of the strategy
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * a method that determines the next possible move with the described strategy
     * @param board the current board object
     * @param mark  the current set mark for a computer player
     * @return the next plausible calculate move
     */
    @Override
    public String determineMove(Board board, int mark) {
        for (int i = 0; i < emptyFields.length; i++) {
            if (board.field[i] == board.playerMark) {
                emptyFields[i] = 0;
            } else {
                emptyFields[i] = -1;
            }
        }

        for (int i = 0; i < emptyFields.length; i++) {
            if (emptyFields[i] == 0) {
                int rndRotation = new Random().nextInt(8);
                return i + "~" + rndRotation;
            }
        }
        return "0" + "~" + "0";
    }
}
