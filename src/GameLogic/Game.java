package GameLogic;

import java.util.Arrays;

/**
 * a game class that monitors the progress of the games
 */
public class Game implements Runnable {
    private Thread thread;

    /**
     * integer declared to measure the amount of players in a game
     */
    public static final int NUMBER_PLAYERS = 2;
    /**
     * an object that represents the board
     */
    private Board board;
    /**
     * an array filled with players of current game
     */
    public Player[] players;
    /**
     * integer declared to measure which's players move is it
     */
    private int current;

    /**
     * the constructor for the game itself
     *
     * @param s0 player object
     * @param s1 player object
     */
    public Game(Player s0, Player s1) {
        board = new Board();
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
    }

    public Player getPlayer1() {
        return players[0];
    }

    public Player getPlayer2() {
        return players[1];
    }

    public int getCurrentPlayer() {
        return current;
    }

    /**
     * A getter method for board that returns the board
     *
     * @return this.board (the current board)
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * this method starts an instance of the game itself
     */
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * this method resets the game
     */
    //@ ensures board != \old(board);
    private synchronized void reset() {
        current = 0;
        board.reset();
    }

    /**
     * this method keeps initiating the moves until the game is over
     */
    private synchronized void play() {
        update();
        while (!board.gameOver()) {
            players[current].makeMove(board, players[current]);
            current = (current + 1) % 2;
            update();
        }
        printResult();
    }

    /**
     * this method prints out the updated board (after every move and rotation)
     */
    private synchronized void update() {
        System.out.println("\ncurrent game situation: \n\n" + board.toString()
                + "\n");
    }

    /**
     * this method prints out the result of the game if it has a winner or a draw
     */
    private synchronized void printResult() {
        if (board.hasWinner()) {
            Player winner = board.isWinner(players[0].getMark()) ? players[0]
                    : players[1];
            System.out.println("Player " + winner.getName() + " ("
                    + winner.getMark() + ") has won!");
        } else {
            System.out.println("Draw. There is no winner!");
        }
    }

    @Override
    public void run() {
        boolean continueGame = true;
        while (continueGame) {
            reset();
            play();
            System.out.println("\n> Play another time? (y/n)?");
            continueGame = utils.TextIO.getBoolean();
        }
    }
}
