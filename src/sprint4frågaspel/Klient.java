package sprint4frågaspel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
public class Klient extends JFrame implements ActionListener {
    private JPanel p = new JPanel();
    private JLabel fråga = new JLabel("");
    private JRadioButton b1 = new JRadioButton("", false);
    private JRadioButton b2 = new JRadioButton("", false);
    private JRadioButton b3 = new JRadioButton("", false);
    private JRadioButton b4 = new JRadioButton("", false);
    private ButtonGroup group = new ButtonGroup();
    private String fromServer;
    private String correctAnswer;
    private String fromUser = "";  // Användarens val

    public Klient() {
        try (Socket socket = new Socket("127.0.0.1", 55555);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Skapa grafiskt gränssnitt
            p.setLayout(new GridLayout(5, 1));
            fråga.setFont(new Font("Arial", Font.BOLD, 20));
            fråga.setForeground(Color.red);
            p.add(fråga);
            p.add(b1);
            p.add(b2);
            p.add(b3);
            p.add(b4);
            setTitle("Frågespel");
            this.add(p);

            // Lägg till ActionListener för varje radioknapp
            b1.addActionListener(this);
            b2.addActionListener(this);
            b3.addActionListener(this);
            b4.addActionListener(this);

            // Gruppera radioknapparna för att endast ett kan väljas
            group.add(b1);
            group.add(b2);
            group.add(b3);
            group.add(b4);

            // Visa fönstret
            pack();
            setVisible(true);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Ta emot fråga från servern och sätt frågetexten
            fromServer = in.readLine();
            fråga.setText(fromServer);
            fråga.revalidate();
            fråga.repaint();

            // Ta emot alternativ från servern och uppdatera radioknapparna
            String options = in.readLine();
            String[] answers = options.split(",");

            if (answers.length > 0) b1.setText(answers[0]);
            b1.revalidate();
            b1.repaint();
            if (answers.length > 1) b2.setText(answers[1]);
            b2.revalidate();
            b2.repaint();
            if (answers.length > 2) b3.setText(answers[2]);
            b3.revalidate();
            b3.repaint();
            if (answers.length > 3) b4.setText(answers[3]);
            b4.revalidate();
            b4.repaint();

            // Vänta på användarens svar tills det inte är tomt
            while (fromUser.isEmpty()) {
                try {
                    Thread.sleep(100); // Liten paus för att inte blockera
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Slutgiltigt val: " + fromUser); // Logga användarens val

            // Skicka användarens val till servern
            out.println(fromUser);
            System.out.println("Skickat val till servern: " + fromUser); // Logga skickad data

            // Ta emot resultat från servern och visa det
            String result = in.readLine();
            System.out.println("Mottaget resultat: " + result);

            // Visa resultatet i en popup-dialog
            JOptionPane.showMessageDialog(null, result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            fromUser = b1.getText(); // Stockholm
        } else if (e.getSource() == b2) {
            fromUser = b2.getText();  // Oslo
        } else if (e.getSource() == b3) {
            fromUser = b3.getText();  // Köpenhamn
        } else if (e.getSource() == b4) {
            fromUser = b4.getText();  // Helsingfors
        }

        System.out.println("Valt alternativ: " + fromUser); // Logga användarens val
    }

    public static void main(String[] args) {
        new Klient();  // Skapa och starta klienten
    }
}