package GameLogic;

/**
 * and interface for a strategy that a computer player will use
 */
public interface Strategy {

    /**
     * an abstract method to get the name of the strategy
     *
     * @return returns the name of the strategy
     */
    //@ pure
    public String getName();

    /**
     * an abstract method to determine a move for a computer player
     *
     * @param board the current board object
     * @param mark  the current set mark for a compuer player
     * @return returns the valid move for a computer player
     */
    //@ requires mark == 1 || mark == 2;
    public String determineMove(Board board, int mark);

}
