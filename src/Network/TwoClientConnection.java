package Network;

import GameLogic.Board;
import GameLogic.ComputerPlayer;
import GameLogic.Game;
import GameLogic.HumanPlayer;

import javax.sound.midi.Soundbank;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

/**
 * A class that establishes a connection between two client handlers in order tow transmit 2 messages to both clients
 */
public class TwoClientConnection implements Runnable {

    /**
     * a variable of type server that indicates the server
     */
    Server server;
    /**
     * a variable of type ClientHandler that indicates one of the clientHandlers in the game
     */
    private ClientHandler clientHandler1;
    /**
     * a variable of type ClientHandler that indicates one of the clientHandlers in the game
     */
    private ClientHandler clientHandler2;

    /**
     * a constructor that makes an object that knows about the two client handles that play a game
     *
     * @param ch1    one of the client handlers that play a game
     * @param ch2    one fo the client handles that play a game
     * @param server the current server
     */
    public TwoClientConnection(ClientHandler ch1, ClientHandler ch2, Server server) {
        this.clientHandler1 = ch1;
        this.clientHandler2 = ch2;
        this.server = server;
        this.clientHandler1.setTwoClientConnection(this);
        this.clientHandler2.setTwoClientConnection(this);
    }

    /**
     * a method that places a mark of the game on the server and sends back moves sent to both clients, so they
     * are aware of the moves that are being made
     *
     * @param index         the chosen index where to place the mark
     * @param rotation      the chosen rotation of the sub board
     * @param clientHandler the client handlers that wants to place the mark
     */
    public synchronized void makeMoveFor2Clients(String index, String rotation, ClientHandler clientHandler) {
        server.makeMoveOnServer(index, rotation, clientHandler);
        clientHandler1.moveServer(index, rotation, clientHandler1);
        clientHandler2.moveServer(index, rotation, clientHandler2);
        if (clientHandler1.getWinner()) {
            String line = "GAMEOVER~VICTORY~" + clientHandler1.getName();
            clientHandler1.gameOverServer(line);
            clientHandler2.gameOverServer(line);
            clientHandler1.unSetWinnder();
            clientHandler2.unSetWinnder();
        } else if (clientHandler2.getWinner()) {
            String line = "GAMEOVER~VICTORY~" + clientHandler2.getName();
            clientHandler1.gameOverServer(line);
            clientHandler2.gameOverServer(line);
            clientHandler1.unSetWinnder();
            clientHandler2.unSetWinnder();
        } else if (clientHandler.getDraw()) {
            String line = "GAMEOVER~DRAW~";
            clientHandler1.gameOverServer(line);
            clientHandler2.gameOverServer(line);
            clientHandler1.unSetDraw();
            clientHandler2.unSetDraw();
        } else if (!clientHandler1.getLoggedInStatus() || !clientHandler2.getLoggedInStatus()) {
            String line = "GAMEOVER~DISCONECTED";
            clientHandler1.gameOverServer(line);
            clientHandler2.gameOverServer(line);
            clientHandler1.unSetWinnder();
            clientHandler2.unSetWinnder();
            clientHandler1.unSetDraw();
            clientHandler2.unSetDraw();
        }
    }

    /**
     * currently an empty run() method since we do not need to monitor anything, so far everything is being monitored
     * without this methods help
     */
    @Override
    public void run() {

    }
}
