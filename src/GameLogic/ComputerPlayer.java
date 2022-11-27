package GameLogic;

/**
 * a class that houses the implementation of a computer player and how they act int the game
 */
public class ComputerPlayer extends Player {

    /**
     * a strategy object which will determine the strategy of a player
     */
    Strategy strategy;

    /**
     * a constructor for a Computer player object
     *
     * @param strategy the set strategy for a computer player
     * @param mark     the set mark for a computer player
     */
    public ComputerPlayer(Strategy strategy, int mark) {
        super(strategy.getName(), mark);
        this.strategy = strategy;
        // this.strategy.getName(); instead of the name
    }

    /**
     * a constructor for a Computer player object with the NAIVE strategy
     *
     * @param mark the set mark for a computer player
     */
    public ComputerPlayer(int mark) {
        this(new Naive(), mark);
    }

    /**
     * a method of which intent is to determine the move of a computer player
     *
     * @param board the board object
     * @return a string which will be the set move of the player
     */
    //@ requires board != null;
    @Override
    public String determineMove(Board board) {
        return strategy.determineMove(board, getMark());
    }

    /**
     * a method that gets the set strategy
     *
     * @return returns the current strategy
     */
    //@ pure
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * a method that sets the wanted strategy
     *
     * @param strategy returns the set strategy
     */
    //@ requires strategy != null;
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }


}
