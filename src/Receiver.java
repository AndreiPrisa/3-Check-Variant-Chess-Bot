import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Procesam comenzile date la stdin de catre xboard engine-ului nostru
 */
public class Receiver {
    public static boolean process(String command) {
        StringTokenizer tokenizer = new StringTokenizer(command, " ");
        ArrayList<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }

        switch (tokens.get(0)) {
            case "xboard" :
                System.out.println("#Connected to xboard");
                break;
            case "protover" :
                System.out.println("#Received protover 2");
                Sender.feature();
                break;
            case "new" :
                System.out.println("#New command received");
                Sender.newGame();
                break;
            case "usermove" :   
                System.out.println("#Am primit " + tokens.get(1));
                Sender.processMove(tokens.get(1));
                break;
            case "go" :
                System.out.println("#Go received");
                Bot.getInstance().setWait(false);
                Bot.getInstance().setPlayingWith(ChessBoard.getInstance().getCurrentPlayer());
//                Bot.getInstance().moveRandom();
                // Pentru a incerca En Passant
//                Bot.getInstance().movePawn();
                Bot.getInstance().play();
                break;
            case "force" :
                System.out.println("#Force received");
                Bot.getInstance().setWait(true);
                break;
            case "quit" :
                System.out.println("#Received quit");
                return false;
            default: break;
        }
        return true;
    }
}
