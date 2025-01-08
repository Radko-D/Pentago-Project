package Initilization;

import GameLogic.*;
import Network.*;
import utils.TextIO;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * a class that initializes the clients
 */
public class ClientInitilization {

    public static void main(String[] args) {

        Client client = new Client();
        System.out.println(" Please type in the desired ip address");
        String address = TextIO.getlnString();
        InetAddress parsedAddress = null;
        try {
            parsedAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            System.out.println(" uknown host");
        }
        System.out.println(" Please input the desired port ");
        int port = TextIO.getlnInt();

        try {
            while (!client.connect(parsedAddress, port) && (new Socket(address, port) != null)) {
                System.out.println(" Please type in the correct!  ip address");
                address = TextIO.getlnString();
                parsedAddress = null;
                try {
                    parsedAddress = InetAddress.getByName(address);
                } catch (UnknownHostException exception) {
                    System.out.println(" wrong, or illegal InetAddress");
                }
                System.out.println(" Please input the correct! port ");
                port = TextIO.getlnInt();
            }
        } catch (IOException exception) {
            System.out.println(" restart the client initialization and please input correct port and ip");
        }


        System.out.println("Would you prefer to play if so type in 0 \n" +
                "if you want a computer with strategy Naive(random) to play type 1 \n" +
                "if you want a computer with strategy Ordered(place marks in an orderly manner ) to play type 2");
        int whoPlays = TextIO.getlnInt();
        String userInput;
        String[] userInputArr;
        if (whoPlays == 0) {
            String description = " client";
            client.connect(parsedAddress, port);
            client.helloClient(description);
            System.out.println("To log in please chose your username");
            String username = TextIO.getlnString();
            client.setName(username);
            client.loginClient();
            userInput = TextIO.getlnString();
            while (!userInput.equals("QUIT")) {
                userInputArr = userInput.split("~");
                if (userInputArr[0].equals("MOVE")) {
                    if (client.getOurTurn() && (client.getBoard().isEmpty(Integer.parseInt(userInputArr[1])))) {
                        client.moveClient(userInputArr[1], userInputArr[2]);
                    }
                } else if (userInput.equals("PING")) {
                    client.pingClient();
                } else if (userInput.equals("LIST")) {
                    client.listClient();
                } else if (userInput.equals("QUEUE")) {
                    client.queueClient();
                } else {
                    client.uknownCommanClient();
                }
                userInput = TextIO.getlnString();
            }
            client.quitClient();
        } else if (whoPlays == 1) {
            String description = " computer naive ";
            client.connect(parsedAddress, port);
            client.helloClient(description);
            System.out.println("To log in your ai  chose your username");
            String username = TextIO.getlnString();
            client.setName(username);
            client.loginClient();
            client.queueClient();
            while (client.getActive()) {
                Player naivePc = new ComputerPlayer(new ServerNaive(), 1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (client.getOurTurn()) {
                    String[] naiveMove = naivePc.determineMove(client.getBoard()).split("~");
                    client.moveClient(naiveMove[0], naiveMove[1]);
                }
            }

            client.quitClient();
        } else if (whoPlays == 2) {
            String description = " computer ordered ";
            client.connect(parsedAddress, port);
            client.helloClient(description);
            System.out.println("To log in your ai  chose your username");
            String username = TextIO.getlnString();
            client.setName(username);
            client.loginClient();
            client.queueClient();
            while (client.getActive()) {
                Player orderedPc = new ComputerPlayer(new ServerOrdered(), 1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (client.getOurTurn()) {
                    String[] naiveMove = orderedPc.determineMove(client.getBoard()).split("~");
                    client.moveClient(naiveMove[0], naiveMove[1]);
                }
            }
            client.quitClient();
        }

    }
}



