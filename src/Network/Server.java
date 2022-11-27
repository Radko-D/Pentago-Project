package Network;

import GameLogic.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Server which houses the implementation of the server itself
 */
public class Server implements Runnable {

    /**
     * a variable of type ServerSocket to give server a server socket
     */
    private ServerSocket serverSocket;
    /**
     * a variable of type Thread to allow adding client handlers or removing them and checking if the server is still
     * alive
     */
    private Thread thread;
    /**
     * a variable of type List with ClientHandlers in it to have a list of client handlers
     */
    private List<ClientHandler> clients = new ArrayList<ClientHandler>();
    /**
     * a variable of type List with queued ClientHandlers to have a list of client handlers who have queued for the
     * game itself
     */
    private List<ClientHandler> queuedClientHandlers = new ArrayList<ClientHandler>();
    /**
     * a variable of type List to house the Games which are catered to work on a server as well as with a list it
     * is possible to have multiple instances of the game running
     */
    private List<ServerGame> games = new ArrayList<ServerGame>();
    /**
     * a variable of type String that declares the server name
     */
    private String serverName = "Blue-12 server";


    /**
     * a constructor that initializes the server itself
     * @param port the port of the server
     * @throws IOException throws this if there is an IOException caught while initializing a server
     */
    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.thread = new Thread(this);
    }

    /**
     * a getter method that retursn the name of the server
     * @return the current set name of the server
     */
    public String getServerName()
    {
        return this.serverName;
    }

    /**
     * a start method to start the thread of the server
     */
    public void start() {
        this.thread.start();
    }

    /**
     * a getter Method to retrieve the port of the server
     * @return the current localPort
     */
    public int getPort() {
        return serverSocket.getLocalPort();
    }

    /**
     * a getter Method to retrieve the clients list
     * @return the clients list
     */
    public List getClients() {
        return this.clients;
    }

    /**
     * a getter Method to retrieve the client from the client list with the set index
     * @param index the current index of wanted client
     * @return the client with the wanted index
     */
    public ClientHandler getClientFromClients(int index) {
        return clients.get(index);
    }

    /**
     * a getter Method to retrieve the list of queuedClientHandlers
     * @return the list of queuedClient handlers
     */
    public List getQueuedClients() {
        return this.queuedClientHandlers;
    }

    /**
     *  a getter Method to retrieve the queuedClient from the queuedClient list with the set index
     * @param index the current index of wanted queuedClient
     * @return the queuedClient with the wanted index
     */
    public ClientHandler getQueuedClientFromClients(int index) {
        return queuedClientHandlers.get(index);
    }

    /**
     * a method that stops the server
     */
    public void stop() {
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
                thread.join();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(" Couldn't stop properly ");
        }
    }

    /**
     * a runt method that monitors weather or not the server is running and adds new client handlers upong accepting a
     * new connection
     */
    @Override
    public void run() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                addClient(clientHandler);
                new Thread(clientHandler);
            }
            if (serverSocket.isClosed()) {
                System.out.println(" socket has been closed !");

            }
        } catch (IOException e) {
            System.out.println(" cant make a connection  (class server run method)");
        }


    }

    /**
     * a method that adds the current client handler to the queue
     * @param clientHandler the current set client handler that needs to be added to the queue
     */
    public synchronized void addToQueue(ClientHandler clientHandler) {
        queuedClientHandlers.add(clientHandler);
        System.out.println(clientHandler.getName() + " queued up");
        if (queuedClientHandlers.size() >= 2) {

            ClientHandler player1 = queuedClientHandlers.get(0);
            ClientHandler player2 = queuedClientHandlers.get(1);

            Player clientHandlerPlayer1 = new HumanPlayer(player1.getName(), 1);
            Player clientHandlerPlayer2 = new HumanPlayer(player2.getName(), 2);


            ServerGame tempGame = new ServerGame(clientHandlerPlayer1, clientHandlerPlayer2);

            games.add(tempGame);

            player1.newGame(player1, player2);
            player2.newGame(player1, player2);

            new Thread(new TwoClientConnection(player1, player2, this)).start();


            System.out.println(queuedClientHandlers.get(0).getName() + " dequeued ");
            queuedClientHandlers.remove(0);
            System.out.println(queuedClientHandlers.get(0).getName() + " dequeued ");
            queuedClientHandlers.remove(0);
        }
    }

    /**
     * a method that goes through the board finds the current board and places the move accordingly
     * @param index the current index where we want to put the mark
     * @param rotation the rotation index of how we want to rotate the board
     * @param clientHandler the current client handler that wants to make the move
     */
    public synchronized void makeMoveOnServer(String index, String rotation, ClientHandler clientHandler) {
        for (ServerGame game : games) {
            if (game.getPlayer1().getName().equals(clientHandler.getName()) ||
                    game.getPlayer2().getName().equals(clientHandler.getName())) {

                if (game.getPlayer1().getName().equals(clientHandler.getName())) {
                    game.getBoard().placeMark(Integer.parseInt(index), Integer.parseInt(rotation), 1);
                } else {
                    game.getBoard().placeMark(Integer.parseInt(index), Integer.parseInt(rotation), 2);
                }
                if (game.getBoard().hasWinner()) {
                    clientHandler.setWinnder();
                    game.getBoard().reset();
                } else if (game.getBoard().Draw()) {
                    clientHandler.setDraw();
                    game.getBoard().reset();
                }
            }
        }

    }

    /**
     * a method that adds the given client handler to the client list
     * @param client the set client handler that we want to add
     */
    public synchronized void addClient(ClientHandler client) {
        clients.add(client);
    }

    /**
     * a method that removes the given client handler from the client list
     * @param client the set client handler that we want to add
     */
    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }

}

