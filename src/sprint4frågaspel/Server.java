package sprint4frågaspel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server extends Thread {
    private Socket player1Socket;
    private Socket player2Socket;

    public Server(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
    }
    @Override
    public void run() {
        try (
                PrintWriter outPlayer1 = new PrintWriter(player1Socket.getOutputStream(), true);
                BufferedReader inPlayer1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
                PrintWriter outPlayer2 = new PrintWriter(player2Socket.getOutputStream(), true);
                BufferedReader inPlayer2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()))
        ) {
            // Skicka fråga och alternativ till båda spelarna
            String question = "Vad är huvudstaden i Sverige?";
            String options = "Stockholm,Oslo,Köpenhamn,Helsingfors";

            outPlayer1.println(question);
            outPlayer1.println(options);
            outPlayer2.println(question);
            outPlayer2.println(options);

            // Vänta på svar från båda spelarna
            AnswerWaiter waiter1 = new AnswerWaiter(inPlayer1);
            String answer1 = waiter1.waitForAnswer();
            AnswerWaiter waiter2 = new AnswerWaiter(inPlayer2);
            String answer2 = waiter2.waitForAnswer();

            // Hantera svaren och skicka resultat till respektive spelare
            String[] result = hanteraSvar(answer1, answer2);
            if (result != null) {
                outPlayer1.println(result[0]);
                outPlayer2.println(result[1]); } else {
                outPlayer1.println("Något gick fel, försök igen");
                outPlayer2.println("Något gick fel, försök igen");
            }
        } catch (IOException e) {
            System.err.println("Kommunikationsfel med spelare: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String[] hanteraSvar(String answer1, String answer2) {
        String correctAnswer = "Stockholm";

        // Trimma svaren för att ta bort eventuella mellanslag och hantera ogiltiga eller tomma svar
        if (answer1 != null) {
            answer1 = answer1.trim();
        } else {
            answer1 = "";
        }
        if (answer2 != null) {
            answer2 = answer2.trim(); } else {
            answer2 = "";
        }
        // Kontrollera om båda spelarna svarade korrekt
        if (answer1.equalsIgnoreCase(correctAnswer) && answer2.equalsIgnoreCase(correctAnswer)) {
            return new String[]{"LIKA", "LIKA"};
        }
        // Kontrollera om bara spelare 1 svarade korrekt
        if (answer1.equalsIgnoreCase(correctAnswer)) {
            return new String[]{"DU VANN", "DU FÖRLORADE"};
        }

        // Kontrollera om bara spelare 2 svarade korrekt
        if (answer2.equalsIgnoreCase(correctAnswer)) {
            return new String[]{"DU FÖRLORADE", "DU VANN"};
        }

        // Om båda spelade fel
        return new String[]{"LIKA", "LIKA"};
    }
}