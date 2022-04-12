import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Se realizeaza conexiunea cu xboard, iar comenzile se trimit
 * mai departe catre Receiver
 */
public class Main {
    public static void main(String[] args) {
        InputStreamReader stream = null;
        BufferedReader stdin = null;
        stream = new InputStreamReader(System.in);
        stdin = new BufferedReader(stream);
        String line;
        boolean flag = true;

        try {
            do {
                line = stdin.readLine();
                flag = Receiver.process(line);
            } while (flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
