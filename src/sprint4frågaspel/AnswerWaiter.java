package sprint4frågaspel;


import java.io.BufferedReader;
import java.io.IOException;

public class AnswerWaiter {

    private BufferedReader in;

    public AnswerWaiter(BufferedReader in) {
        this.in = in;
    }

    public String waitForAnswer() {
        int timeout = 10000; // Timeout på 10 sekunder (10000 millisekunder)

        try {
            // Vänta på svar under angiven timeout
            for (int i = 0; i < timeout / 100; i++) {
                if (in.ready()) {
                    String answer = in.readLine(); // Läs spelarens svar
                    System.out.println("Svar mottaget från spelare: " + answer);
                    return answer; // Returnera svaret om det har mottagits
                }
                Thread.sleep(100); // Vänta 100 millisekunder innan vi kollar igen
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); // Fånga och skriv ut eventuella undantag
        }

        return "SVARADE INTE I TID"; // Om ingen svarade inom timeouten
    }
}






