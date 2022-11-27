package Network;

import GameLogic.Board;
import GameLogic.Game;
import GameLogic.HumanPlayer;
import GameLogic.Player;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Client class that makes a client that will send messages to Client Handler that which will handle it appropriate
 * in according to the server
 */
public class Client implements Runnable {
    /**
     * a variable of the ClientHandler which a client has to communicate with the server
     */
    private ClientHandler clientHandler;
    /**
     * a variable of type  Socket which the socket that the client puts in to connect to the server
     */
    private Socket socket;
    /**
     * a variable of type BufferedReader that will write to the client handler
     */
    private BufferedReader buffIn;
    /**
     * a variable of type BufferedWriter that will write what the ClientHandler transmits to client
     */
    private BufferedWriter buffOut;
    /**
     * a variable of type String which declares the clients name
     */
    private String name;
    /**
     * a variable of type Thread which will be a separate thread for the client( the run method takes use of the
     * buffered writer to read from ClientHandler)
     */
    private Thread thread;
    /**
     * a variable of type boolean to check weather or not is our move
     */
    private boolean ourTurn = false;
    /**
     * a variable of type Board to display the board to the local client
     */
    private Board board = new Board();

    /**
     * a variable of type boolean to check if the game is active
     */
    private boolean active;

    /**
     * A constructor for Class client that initializes a client to establish a connection before logging in therefore
     * no name or any other parameter needed here since we adjust it later on
     */
    public Client() {
    }

    /**
     * a setter method that sets the name
     *
     * @param name the name that will be set to this client
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * a getter method that returns whether the client is still active
     *
     * @return active status
     */
    public boolean getActive() {
        return active;
    }

    /**
     * a getter method that returns the board
     *
     * @return current board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * a getter method that returns if it is our Turn
     * @return ourTurn
     */
    public boolean getOurTurn(){
        return ourTurn;
    }

    /**
     * a method that initializes the connection between the server and client
     *
     * @param address the InetAddress of the server
     * @param port    the port of the server
     * @return true if successfully connected
     */
    public boolean connect(InetAddress address, int port) {
        try {
            socket = new Socket(address, port);
            buffIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            buffOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            System.out.println(" could not connect the socket is wrong");
        }
        return socket.isConnected();
    }

    /**
     * a method that closes the connection between server( closes the socket and stops the thread)
     */
    public void close() {
        // gracefully terminate
        System.exit(0);
//        try {
//            socket.close();
//            thread.join();
//        } catch (IOException | InterruptedException e) {
//            System.out.println(" socket failed to close");
//        }
    }


    /**
     * a method that sends to the server through the set ClientHandler  a hello statement to confirm handshake
     *
     * @param description the description of the server
     */
    public void helloClient(String description) {
        try {
            buffOut.write("HELLO" + "~" + description);
            buffOut.newLine();
            buffOut.flush();
        } catch (IOException e) {
            System.out.println(" Could not send hello to the server ");
        }
    }

    /**
     * a method that sends to the server through the set ClientHandler a login statement to log in
     */
    public void loginClient() {
        try {
            buffOut.write("LOGIN" + "~" + this.name);
            buffOut.newLine();
            buffOut.flush();
        } catch (IOException e) {
            System.out.println(" username could not log in ");
        }
    }

    /**
     * a method that sends to the server through the set ClientHandler a list command that will ask to send back the
     * list of clients in the server
     */
    public void listClient() {
        try {
            buffOut.write("LIST");
            buffOut.newLine();
            buffOut.flush();
        } catch (IOException e) {
            System.out.println(" could not retrieve the list of clients ");
        }
    }

    /**
     * a method that sends to the server through the set ClientHandler a queue command to queue the client up to
     * play the game
     */
    public void queueClient() {
        try {
            buffOut.write("QUEUE");
            buffOut.newLine();
            buffOut.flush();
        } catch (IOException e) {
            System.out.println(" could not queue ");
        }
    }

    /**
     * a method that sends to the server through the set ClientHandler a move command to send the move of the
     * current player
     *
     * @param index    the index where the move should be pressed
     * @param rotation the rotation index of how the board should be rotated
     */
    public void moveClient(String index, String rotation) {
        try {
            ourTurn = true;
            buffOut.write("MOVE" + "~" + index + "~" + rotation);
            buffOut.newLine();
            buffOut.flush();
        } catch (IOException e) {
            System.out.println(" ERROR || move not sent");
        }
    }

    /**
     * a method that sends to the server through the set ClientHandler  a ping command to check if the connection
     * is still running
     */
    public void pingClient() {
        try {
            buffOut.write("PING");
            buffOut.newLine();
            buffOut.flush();
        } catch (IOException e) {
            System.out.println(" could not do the ping");
        }
    }

    /**
     * a method that sends to the server through the set ClientHandler a quit command in order to quit from the server
     */
    public void quitClient() {
        try {
            buffOut.write("QUIT");
            buffOut.newLine();
            buffOut.flush();
        } catch (IOException e) {
            System.out.println(" could not properly quit ");
        }
    }

    /**
     * a method that sends to the server through the set ClientHandler an unknown command which will let the server
     * know that the command doesn't exist
     */
    public void uknownCommanClient() {
        try {
            buffOut.write("WRONGCOMMAND");
            buffOut.newLine();
            buffOut.flush();
        } catch (IOException e) {
            System.out.println(" could not do the ping");
        }
    }

    /**
     * a run method for the thread to monitor output of the set client handler and act accordingly based on the
     * output from the set client handler
     */
    @Override
    public void run() {
        active = true;
        String line;
        String lineArr[];
        try {
            line = buffIn.readLine();
            lineArr = line.split("~");
            if (lineArr[0].equals("HELLO")) {
                System.out.println(line);
                while (active) {
                    line = buffIn.readLine();
                    lineArr = line.split("~");
                    if (lineArr[0].equals("LOGIN")) {
                        System.out.println(line);
                    } else if (lineArr[0].equals("ALREADYLOGGEDIN")) {
                        System.out.println(line);
                    } else if (lineArr[0].equals("QUIT")) {
                        close();
                    } else if (lineArr[0].equals("PONG")) {
                        System.out.println(line);
                    } else if (lineArr[0].equals("LIST")) {
                        System.out.println(line);
                    } else if (lineArr[0].equals("NEWGAME")) {
                        if (lineArr[1].equals(this.name)) {
                            System.out.println(lineArr[0] + "~" + lineArr[1] + "~" + lineArr[2]);
                            System.out.println(board.toString());
                            System.out.println("YOU ARE PLAYER 1 MAKE A MOVE");
                            ourTurn = true;
                        } else if (lineArr[2].equals(this.name)) {
                            System.out.println(lineArr[0] + "~" + lineArr[1] + "~" + lineArr[2]);
                            System.out.println(board.toString());
                            System.out.println("YOU ARE PLAYER 2 WAIT FOR PLAYER 1 TO MAKE A MOVE");
                            ourTurn = false;
                        }
                    } else if (lineArr[0].equals("MOVE")) {
                        System.out.println(line);
                        if (ourTurn) {
                            board.placeMark(Integer.parseInt(lineArr[1]), Integer.parseInt(lineArr[2]), 1);
                            System.out.println(board.toString());
                            ourTurn = false;
                        } else {
                            board.placeMark(Integer.parseInt(lineArr[1]), Integer.parseInt(lineArr[2]), 2);
                            System.out.println(board.toString());
                            ourTurn = true;
                        }
                    } else if (lineArr[0].equals("WRONGCOMMAND")) {
                        System.out.println(line);
                    } else if (lineArr[0].equals("GAMEOVER")) {
                        System.out.println(line);
                        board.reset();

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" buffered reader can not read -> assumed server closed ");
        }
    }
}

