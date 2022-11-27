package Network;

import GameLogic.Player;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    /**
     * a variable of type Server to indicate the server where the ClientHandler connects
     */
    private Server server;

    /**
     * a variable of type BufferedReader that is used to read from the client
     */
    private BufferedReader bufferedReader;
    /**
     * a variable of type BufferedWritter that is used to write back to the client
     */
    private BufferedWriter bufferedWriter;

    /**
     * a variable of type socket the is used to connect
     */
    private Socket socket;

    /**
     * a variable of type  String that indicates the name of the client handler
     */
    private String name;
    /**
     * a variable of type Player that indicates the player of the current client handler
     */
    protected Player player;
    /**
     * a variable of type boolean to check wetter on not the client handler is logged int
     */
    private boolean loggedIn = false;
    /**
     * a variable of type TwoClientConnection that can relay 2 messages to 2 clients from 1 client handler
     */
    private TwoClientConnection twoClientConnection;
    /**
     * a variable of type Object that we synchronise on
     */
    private final Object object = new Object();
    /**
     * a variable of type boolean to check whether the client has won the game
     */
    private boolean wonTheGame = false;
    /**
     * a variable of type boolean to check whether the client has a draw case in the game
     */
    private boolean draw = false;
    /**
     * a variable of type boolean to check whether on not the client is in game
     */
    boolean inGame;

    /**
     * a variable of type boolean to check whether the game is active
     */
    private boolean active;


    /**
     * a constructor that constructs an object of the ClientHandler that relies on the info from and to client and
     * executes the needed commands
     *
     * @param socket the current set socket
     * @param server the current set server
     * @throws IOException an Exception that we get thrown if we have issues with the writers and readers
     */
    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        new Thread(this).start();
    }

    /**
     * a getter method to retrieve the case if the client handler has won the game
     *
     * @return current status of if the client has won a game
     */
    public boolean getWinner() {
        return wonTheGame;
    }

    /**
     * a getter method to retrieve the case if the client handler has a draw case in the game
     *
     * @return current status of if the client has a draw case in the game
     */
    public boolean getDraw() {
        return draw;
    }

    /**
     * a setter method that sets the winner to true
     */
    public void setWinnder() {
        wonTheGame = true;
    }

    /**
     * a setter method that sets the draw case to true
     */
    public void setDraw() {
        draw = true;
    }

    /**
     * a setter method that sets the winning case of the client handler to false
     */
    public void unSetWinnder() {
        wonTheGame = false;
    }

    /**
     * a setter method that sets the draw case of the client handler to false
     */
    public void unSetDraw() {
        draw = false;
    }

    /**
     * a getter method that returns the name of the client handler
     *
     * @return the current name of the client handler
     */
    public String getName() {
        return this.name;
    }

    /**
     * a getter method that returns the loggedIn state
     *
     * @return loggedIn state
     */
    public boolean getLoggedInStatus() {
        return loggedIn;
    }

    /**
     * a setter method that sets the inGame value to true
     */
    public void setLoggedInStatusTrue() {
        loggedIn = true;
    }


    /**
     * a method that sends back to the client if the handshake has happened
     */
    public synchronized void helloServer() {
        try {
            bufferedWriter.write("HELLO" + "~" + server.getServerName());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("could not send hello");
        }
    }

    /**
     * a method that sends back to the client if the login sequence happened and the client is logged in
     *
     * @param name the name chosen by the user
     */
    public synchronized void loginServer(String name) {
        try {
            bufferedWriter.write("LOGIN");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("error in log in");
        }

    }

    /**
     * a method that checks weather or not there is already a user with the wanted name to log in with
     *
     * @param name the name chosen by the user
     * @return true if an user with this name exits and false f the user does not exist
     */
    public boolean alreadyLoggedIn(String name) {
        try {
            int temporrary = 0;
            for (int i = 0; i < server.getClients().size(); i++) {
                if ((server.getClientFromClients(i).getLoggedInStatus()) &&
                        server.getClientFromClients(i).getName().equals(name)) {
                    loggedIn = false;
                    bufferedWriter.write("ALREADYLOGGEDIN");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    return true;
                } else {
                    temporrary = i;
                }
            }

            server.getClientFromClients(temporrary).setLoggedInStatusTrue();
        } catch (IOException e) {
            System.out.println("ALREADYLOGGED IN ERROR");
        }
        return false;
    }

    /**
     * a method that lists the list of clients to the user upon requesting to
     */
    public void listServer() {
        try {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < server.getClients().size(); i++) {
                if (server.getClientFromClients(i).getLoggedInStatus()) {
                    result.append("~").append(server.getClientFromClients(i).getName());
                }
            }
            bufferedWriter.write("LIST" + result);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(" could not send back the list");
        }
    }

    /**
     * a method that sends back new game message to the clients to which are in the set game after queueing up to the
     * game and starting it
     *
     * @param ch0 one of the client handlers participating gin the game
     * @param ch1 one of the client handlers participating in the game
     */
    public synchronized void newGame(ClientHandler ch0, ClientHandler ch1) {
        try {
            inGame = true;
            bufferedWriter.write("NEWGAME" + "~" + server.getQueuedClientFromClients(0).getName() +
                    "~" + server.getQueuedClientFromClients(1).getName());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(" could not start the game");
        }
    }

    /**
     * a method that is called when making a move and relays the move back to the client
     *
     * @param index         the chosen index to send on the board
     * @param rotation      the chosen sub board to rotate ( the index to chose which board and how to rotate)
     * @param clientHandler the current client handler that chose the move
     */
    public void moveServer(String index, String rotation, ClientHandler clientHandler) {
        try {
            bufferedWriter.write("MOVE" + "~" + index + "~" + rotation);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(" could not send move");
        }
    }

    /**
     * a method that is called when queueing up tp the game ( the current client handler is added to the queue)
     * if he is in the queue he is removed
     *
     * @param clientHandler the current client handler
     */
    public void queueServer(ClientHandler clientHandler) {
        if (clientHandler.getLoggedInStatus() && !server.getQueuedClients().contains(clientHandler)) {
            server.addToQueue(clientHandler);
        } else if (server.getQueuedClients().contains(clientHandler)) {
            server.getQueuedClients().remove(clientHandler);
        }
    }

    /**
     * a method that is called when the client wants to quit the server (game)
     */
    public void quitServer() {
        try {
            loggedIn = false;
            active = false;
            bufferedWriter.write("QUIT");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(" could not quit");
        }
    }

    /**
     * a ping method that sends back ping to check if the client is still alvie (connected to the server)
     */
    public void pingServer() {
        try {
            bufferedWriter.write("PONG");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(" could not reply to ping");
        }
    }

    /**
     * a method that is called when the game is over and sends the line that includes whu the game has finished
     *
     * @param line a variable of type String that includes the info on why the game has ended
     */
    public void gameOverServer(String line) {
        try {
            inGame = false;
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(" could not send game over");
        }
    }

    /**
     * a method that establishes a connection between two client handlers
     *
     * @param twoClientConnection the set connection between two clients
     */
    public void setTwoClientConnection(TwoClientConnection twoClientConnection) {
        this.twoClientConnection = twoClientConnection;
    }

    /**
     * a method that responds to the client with "WRONGCOMMAND" when the command is uknown
     */
    public void uknownCommanServert() {
        try {
            bufferedWriter.write("WRONGCOMMAND");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(" could not reply to ping");
        }
    }

    public void wrongMove() {
        try {
            bufferedWriter.write("ILLGEALMOVE");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(" could not say ILLGEALMOVE");
        }
    }


    /**
     * a run method that reads from the client (the send commands) and acts accordingly
     */
    @Override
    public void run() {
        active = true;
        String line;
        String[] lineArr;
        try {
            line = bufferedReader.readLine();
            lineArr = line.split("~");
            if (lineArr[0].equals("HELLO")) {
                helloServer();
            }
            while (active) {
                line = bufferedReader.readLine();
                lineArr = line.split("~");
                if (lineArr[0].equals("LOGIN")) {
                    synchronized (object) {
                        if (!alreadyLoggedIn(lineArr[1])) {
                            loggedIn = true;
                            this.name = lineArr[1];
                            loginServer(lineArr[1]);
                        }
                    }
                } else if (lineArr[0].equals("MOVE")) {
                    synchronized (object) {
                        if (inGame && loggedIn && (Integer.parseInt(lineArr[1]) >= 0 &&
                                Integer.parseInt(lineArr[1]) <= 35) &&
                                (Integer.parseInt(lineArr[2]) >= 0 &&
                                        Integer.parseInt(lineArr[2]) <= 7)) {
                            twoClientConnection.makeMoveFor2Clients(lineArr[1], lineArr[2], this);
                        }
                    }
                } else if (line.equals("QUEUE")) {
                    synchronized (object) {
                        if (loggedIn) {
                            queueServer(this);
                        }
                    }
                } else if (line.equals("QUIT")) {
                    quitServer();
                } else if (line.equals("PING")) {
                    pingServer();
                } else if (line.equals("LIST")) {
                    synchronized (object) {
                        if (loggedIn) {
                            listServer();
                        }
                    }
                } else if (line.equals("WRONGCOMMAND")) {
                    uknownCommanServert();
                }
            }
        } catch (IOException e) {
            loggedIn = false;
            System.out.println(" user left ");
            // game over here
        }
    }
}
