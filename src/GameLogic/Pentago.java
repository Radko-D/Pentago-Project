package GameLogic;


/**
 * this class has a main method that we can use to test out and play the game
 */
public class Pentago {

    public static void main(String[] args) {

        String fight;
        Player player1, player2;
        String name1, name2;
        System.out.println("type in computers to see computers fight or anything to 1v1 with ur friend");
        fight = utils.TextIO.getlnString();


        if (fight.equals("computers")) {
            player1 = new ComputerPlayer(1);
            player2 = new ComputerPlayer(2);
        } else {
            System.out.println("Type ur names one by one");
            name1 = utils.TextIO.getlnString();
            name2 = utils.TextIO.getlnString();

            player1 = new HumanPlayer(name1, 1);
            player2 = new HumanPlayer(name2, 2);
        }
//        Player player1 = new ComputerPlayer(1);
//        Player player2 = new ComputerPlayer(2);

        Game game = new Game(player1, player2);
        game.players[0] = player1;
        game.players[1] = player2;
        game.start();

    }
}
