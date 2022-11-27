package Initilization;

import Network.Server;

import java.io.IOException;

public class ServerInitilization {
    public static void main(String[] args) {
        Server server = null;

        int port = 999999;
        String line = "a";
        while (port > 65536) {
            System.out.println(" pick a valid port between 0-65536 ");
            port = ss.utils.TextIO.getlnInt();
        }
        try {
            server = new Server(port);
            server.start();
            if (server.getPort() == 0) {
                System.out.println("thing is zero so you get port  -> " + server.getPort());
            }
            System.out.println(" server starting");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!line.equals("quit")) {
            line = ss.utils.TextIO.getlnString();
            if (line.equals("quit")) {
                server.stop();
                break;
            }
        }
    }
}
