/**
 * Se realizeaza conexiunea dinspre engine catre xboard
 */
public class Sender {
    public static void feature() {
        String features = "feature sigint=0 myname=\"A3\" usermove=1";
        System.out.println(features);
    }

    public static void newGame() {
        ChessBoard.getInstance().initializeBoard();
    }

    /**
     * Optiune pentru debug pentru a afisa tabla de joc
     */
    public static void showBoard() {
        ChessBoard.getInstance().printBoard();
    }

    /**
     * Cere actualizarea tablei de joc pe baza comenzii date de xboard
     */
    public static void processMove(String move) {
        Position[] pair = Position.decode(move);
        Position from = pair[0];
        Position to = pair[1];
        ChessBoard.getInstance().updateBoard(from, to);
    }

    public static void resign() {
        System.out.println("resign");
    }

    public static void sendMove(String message) {
        System.out.println("move " + message);
        ChessBoard chessBoard = ChessBoard.getInstance();
        int state = chessBoard.gameFinished();
        switch (state) {
            case Constants.DRAW:
                System.out.println("1/2-1/2 {Draw bot}");
                break;
            case Constants.WIN:
                if (chessBoard.getCurrentPlayer().equals(Constants.WHITE)) {
                    System.out.println("0-1 {Black mates}");
                } else {
                    System.out.println("1-0 {White mates}");
                }
                break;
            default: break;
        }
    }
}
