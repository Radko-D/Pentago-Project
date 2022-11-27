package GameLogic;

import java.util.Random;
/**
 * a naive strategy that is used in computer player to make random moves
 */
public class Naive implements Strategy {
    /**
     * a private string that declares the name of the strategy
     */
    private String name = " Naive ";
    /**
     * an array used to check which fields are empty
     */
    int[] emptyFields = new int[36];

    /**
     * a method that gets the name of the strategy
     *
     * @return returns the name of the strategy
     */
    //@ pure
    @Override
    public String getName() {
        return name;
    }

    /**
     * a method that determines a move in a random order
     *
     * @param board the current board object
     * @param mark  the current set mark for this computer player
     * @return returns the random index and the rotation to make a valid move
     */
    //@ requires board != null;
    //@ requires mark == 1 || mark == 2;
    @Override
    public String determineMove(Board board, int mark) {

        for (int i = 0; i < emptyFields.length; i++) {
            if (board.field[i] == board.playerMark) {
                emptyFields[i] = 0;
            } else {
                emptyFields[i] = -1;
            }
        }

        int rndIndex = new Random().nextInt(emptyFields.length);

        while (emptyFields[rndIndex] != 0) {
            rndIndex = new Random().nextInt(emptyFields.length);
        }
        int rndRotation = new Random().nextInt(8);
        return rndIndex + "~" + rndRotation;
    }
}
