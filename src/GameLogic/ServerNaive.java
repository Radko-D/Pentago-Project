package GameLogic;

import java.util.Random;

/**
 * a class that makes a computer player to play on the server
 */
public class ServerNaive implements Strategy {

    private String name = "SERVER NAIVE";

    int[] emptyFields = new int[36];

    @Override
    public String getName() {
        return name;
    }

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
