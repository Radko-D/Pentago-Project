package GameLogic;

/**
 * a class that houses the implementation of a human player and how they act int the game
 */
public class HumanPlayer extends Player {

    /**
     * @param name name of the player
     * @param mark mark of the player
     */
    public HumanPlayer(String name, int mark) {
        super(name, mark);
    }

    /**
     * a method of which intent is to determine the move of a human player
     *
     * @param board the board object
     * @return a string which will be the set move of the player
     */
    //@ requires board != null;
    @Override
    public String determineMove(Board board) {
        String prompt = "> " + getName() + " (" + getMark() + ")" + ", what is your choice? use this protocol ! ->(MOVE~<Index>~<Rotation>)\n\n";
        String rotations = "0 -> rotates the top left subboard counter-clockwise\n" +
                "1 -> rotates the top left subboard clockwise\n" +
                "2 -> rotates  he top right subboard counter-clockwise\n" +
                "3 -> rotates the top right subboard clockwise\n" +
                "4 -> rotates the bottom left subboard counter-clockwise\n" +
                "5 -> rotates the bottom left subboard clockwise\n" +
                "6 -> rotates the bottom right subboard counter-clockwise\n" +
                "7 -> rotates the bottom right subboard clockwise";
        System.out.println(prompt + rotations);
        String choice = utils.TextIO.getlnString();

        while (choice.length() <= 1) { // add so it does show error eve if right amount, but so it must be int
            System.out.println(" invalid input input again");
            choice = utils.TextIO.getlnString();
        }
        String[] arr = choice.split("~"); // why can't we do try here

        int z = Integer.parseInt(arr[1]);
        int rotation = Integer.parseInt(arr[2]);
        boolean valid = board.isEmpty(z);

        while (!valid) {
            System.out.println("ERROR: field " + choice
                    + " is no valid choice.");
            System.out.println(prompt);
            choice = utils.TextIO.getlnString();
            while (choice.length() <= 1) {
                System.out.println(" invalid input input again");
                choice = utils.TextIO.getlnString();
            }
            arr = choice.split("~"); // why cant we do try here
            z = Integer.parseInt(arr[1]);
            valid = board.isEmpty(z);
        }

        return choice;
    }
}
