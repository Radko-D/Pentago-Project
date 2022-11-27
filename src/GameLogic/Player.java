package GameLogic;

/**
 * abstract class of a player that will be used in making a human or computer player
 */
public abstract class Player {

    /**
     * declared string for the name of the player
     */
    private String name;
    /**
     * declared integer for the mark of the player 1 or 2
     */
    private int mark;

    /**
     * constructor that constructs a player object
     *
     * @param name the name of the player
     * @param mark the mark of the player
     */
    public Player(String name, int mark) {
        this.name = name;
        this.mark = mark;
    }

    /**
     * a get method that gets the name of the player
     *
     * @return returns the name of the wanted player
     */
    //@ pure
    public String getName() {
        return name;
    }

    /**
     * a get method that gets the mark of the player
     *
     * @return the mark of the wanted player
     */
    //@ pure
    public int getMark() {
        return mark;
    }

    /**
     * an abstract method of which intent is to determine the move
     *
     * @param board the board object
     * @return a string which will be the set move of the player
     */
    //@ requires board != null;
    public abstract String determineMove(Board board);

    /**
     * this method makes the move and sends it to the board
     *
     * @param board  the current board object
     * @param player the current player
     */
    //@ requires board !=null && player != null;
    public void makeMove(Board board, Player player) {
        String choice;
        String arr[];
        int index;
        int rotation;
        if (player.getName().equals(" Naive ")) {
            choice = determineMove(board);
            arr = choice.split(" ");
            index = Integer.parseInt(arr[0]);
            rotation = Integer.parseInt(arr[1]);
            board.placeMark(index, rotation, player.getMark());
            System.out.println("ComputerPlayer" + player.getMark() + " picked index index-> "
                    + index + " and rotated -> " + rotation);
        } else {
            choice = determineMove(board);
            System.out.println(choice);
            arr = choice.split("~");
            index = Integer.parseInt(arr[1]);
            rotation = Integer.parseInt(arr[2]);
            board.placeMark(index, rotation, player.getMark());
        }

    }


}
