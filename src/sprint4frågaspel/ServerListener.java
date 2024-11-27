package sprint4frågaspel;

import java.io.*;
import java.net.*;

public class ServerListener {
    public ServerListener() {
        try (ServerSocket serverSocket = new ServerSocket(55555)) {
            System.out.println("Servern väntar på spelare...");

            while (true) {
                // Vänta på första spelaren
                Socket player1Socket = serverSocket.accept();
                System.out.println("Spelare 1 ansluten.");

                // Vänta på andra spelaren
                Socket player2Socket = serverSocket.accept();
                System.out.println("Spelare 2 ansluten.");

                // Skapa och starta en ny Server-tråd för dessa spelare
                Server server = new Server(player1Socket, player2Socket);
                server.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServerListener();
    }
}
