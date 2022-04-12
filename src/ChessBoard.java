import java.util.ArrayList;
import java.util.Random;


/**
 * Clasa pentru mentinarea tablei si a starii de joc
 */
public class ChessBoard {
    //vector pentru initializarea tablei de joc
    private final int[] pieceType = new int[]{Constants.ROOK, Constants.KNIGHT, Constants.BISHOP, Constants.QUEEN, Constants.KING,
                                            Constants.BISHOP, Constants.KNIGHT, Constants.ROOK};

    //matrice pentru tabla de joc
    private Square[][] board;

    //culoarea jucatorului curent
    private String currentPlayer;

    //listele cu piesele de joc
    private ArrayList<ArrayList<Piece>> whiteList;
    private ArrayList<ArrayList<Piece>> blackList;

    // pioni asupra carora se poate face En Passant
    private Piece whitePawnEP;
    private Piece blackPawnEP;

    // flag true daca se poate face promote
    private boolean flag;
    // piesa in care se face promote
    private int promotionType;

    // istoricul jocului(starile prin care a trecut jocul)
    private ArrayList<State> states;

    // contor miscari(pentru 50-rule)
    private int dummyMoves;

    // contoare sah regi(regula 3-check-chess)
    private int whiteChecked;
    private int blackChecked;

    private boolean whiteCastle;
    private boolean blackCastle;

    private static ChessBoard obj = null;
    private ChessBoard() {
        board = new Square[8][8];
        whiteList = new ArrayList<>();
        blackList = new ArrayList<>();
        states = new ArrayList<>();
    }
    public static ChessBoard getInstance() {
        if (obj == null) {
            obj = new ChessBoard();
        }
        return obj;
    }

    /**
     * Cand se citeste comanda new, se initializeaza tabla astfel:
     * -> se creeaza cei 2 vectori de liste;
     * -> se creeaza tabla interna de joc;
     * -> se seteaza starea initiala;
     */
    public void initializeBoard() {
        Bot.getInstance().getActions().clear();
        states.clear();
        whiteList.clear();
        blackList.clear();
        for (int i = 0 ; i < 6; i++) {
            whiteList.add(new ArrayList<>());
            blackList.add(new ArrayList<>());
        }

        //Initializam casutele cu piese + vectorii de liste pentru piese
        for (int col = 0; col < 8; col++) {
            board[0][col] = new Square(PieceFactory.factory(new Position(0, col), Constants.BLACK, pieceType[col]));
            board[7][col] = new Square(PieceFactory.factory(new Position(7, col), Constants.WHITE, pieceType[col]));
            board[1][col] = new Square(PieceFactory.factory(new Position(1, col), Constants.BLACK, Constants.PAWN));
            board[6][col] = new Square(PieceFactory.factory(new Position(6, col), Constants.WHITE, Constants.PAWN));
            whiteList.get(pieceType[col] - 1).add(board[7][col].getPiece());
            whiteList.get(Constants.PAWN - 1).add(board[6][col].getPiece());
            blackList.get(pieceType[col] - 1).add(board[0][col].getPiece());
            blackList.get(Constants.PAWN - 1).add(board[1][col].getPiece());
        }
        //Initializam casutele fara piese
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Square();
            }
        }

        //Se stabileste starea initiala de joc
        currentPlayer = Constants.WHITE;
        Bot.getInstance().setPlayingWith(Constants.BLACK);
        Bot.getInstance().setWait(false);

        //Pionii pentru EN-PASSANT
        whitePawnEP = null;
        blackPawnEP = null;

        // Se seteaza ce pioni va avea la dispozitie engine-ul
//        Bot.getInstance().setBlackPawn(this.getSquare(new Position(1, 0)).getPiece());
//        Bot.getInstance().setWhitePawn(this.getSquare(new Position(6, 1)).getPiece());

        dummyMoves = 0;
        blackChecked = 0;
        whiteChecked = 0;
        blackCastle = false;
        whiteCastle = false;
        addNewState();
    }

    /**
     * la fiecare mutare, se actualizeaza tabla astfel:
     * -> se muta piesa la pozitia indicata de from;
     * -> daca la coordonata from exista o piesa adversa, aceasta este capturata
     * prin eliminarea piesei din vectorul de liste al oponentului;
     * -> se verifica posibilitatea unei promovari a piesei mutate;
     * -> se adauga starea curenta in vectorul starilor
     */
    public void updateBoard(Position from, Position to) {
        dummyMoves++;
        flag = false;
        Piece piece = board[from.getRow()][from.getCol()].getPiece();
        String colour = piece.getColour();
        checkENPASSANT(from, to);

        //Se elibereaza square-ul vechi
        board[from.getRow()][from.getCol()].setPiece(null);

        //Se elimina piesa capturata
        if (!board[to.getRow()][to.getCol()].isEmpty()) {
            dummyMoves = 0;
            Piece capturedPiece = board[to.getRow()][to.getCol()].getPiece();
            if (capturedPiece.getColour().equals(Constants.BLACK))
                blackList.get(capturedPiece.getType() - 1).remove(capturedPiece);
            else
                whiteList.get(capturedPiece.getType() - 1).remove(capturedPiece);
        } else if (piece.getType() == Constants.PAWN) {         //En-Passant
            dummyMoves = 0;
            if (colour.equals(Constants.WHITE) && blackPawnEP != null) {
                if (blackPawnEP.getPosition().getRow() - to.getRow() == 1 && blackPawnEP.getPosition().getCol() == to.getCol()) {
                    blackList.get(Constants.PAWN - 1).remove(blackPawnEP);
                    board[blackPawnEP.getPosition().getRow()][blackPawnEP.getPosition().getCol()].setPiece(null);
                }
            } else if (colour.equals(Constants.BLACK) && whitePawnEP != null) {
                if (whitePawnEP.getPosition().getRow() - to.getRow() == -1 && whitePawnEP.getPosition().getCol() == to.getCol()) {
                    whiteList.get(Constants.PAWN - 1).remove(whitePawnEP);
                    board[whitePawnEP.getPosition().getRow()][whitePawnEP.getPosition().getCol()].setPiece(null);
                }
            }
        } else if (piece.getType() == Constants.KING) {         //rocade
            //stanga alb
            if (colour.equals(Constants.WHITE) && to.getCol() - from.getCol() == -2) {
                whiteList.get(Constants.ROOK - 1).get(0).setPosition(new Position(from.getRow(), from.getCol() - 1));
                board[from.getRow()][0].setPiece(null);
                board[from.getRow()][from.getCol() - 1].setPiece(whiteList.get(Constants.ROOK - 1).get(0));
                ((Rook)whiteList.get(Constants.ROOK - 1).get(0)).setMoved(true);
                whiteCastle = true;
            }

            //dreapta alb
            if (colour.equals(Constants.WHITE) && to.getCol() - from.getCol() == 2) {
                int index = Math.min(getWhiteList().get(Constants.ROOK - 1).size() - 1, 1);
                whiteList.get(Constants.ROOK - 1).get(index).setPosition(new Position(from.getRow(), from.getCol() + 1));
                board[from.getRow()][7].setPiece(null);
                board[from.getRow()][from.getCol() + 1].setPiece(whiteList.get(Constants.ROOK - 1).get(index));
                ((Rook)whiteList.get(Constants.ROOK - 1).get(index)).setMoved(true);
                whiteCastle = true;
            }


            //stanga negru
            if (colour.equals(Constants.BLACK) && to.getCol() - from.getCol() == -2) {
                blackList.get(Constants.ROOK - 1).get(0).setPosition(new Position(from.getRow(), from.getCol() - 1));
                board[from.getRow()][0].setPiece(null);
                board[from.getRow()][from.getCol() - 1].setPiece(blackList.get(Constants.ROOK - 1).get(0));
                ((Rook)blackList.get(Constants.ROOK - 1).get(0)).setMoved(true);
                blackCastle = true;
            }

            //dreapta negru
            if (colour.equals(Constants.BLACK) && to.getCol() - from.getCol() == 2) {
                int index = Math.min(getBlackList().get(Constants.ROOK - 1).size() - 1, 1);
                blackList.get(Constants.ROOK - 1).get(index).setPosition(new Position(from.getRow(), from.getCol() + 1));
                board[from.getRow()][7].setPiece(null);
                board[from.getRow()][from.getCol() + 1].setPiece(blackList.get(Constants.ROOK - 1).get(index));
                ((Rook)blackList.get(Constants.ROOK - 1).get(index)).setMoved(true);
                blackCastle = true;
            }
        }

        //Actualizez tabla interna, precum si coordonatele piesei mutate
        board[to.getRow()][to.getCol()].setPiece(piece);
        piece.setPosition(to);

        flag = checkPromotion(board[to.getRow()][to.getCol()]);
        if (flag) {
            promoteTo(board[to.getRow()][to.getCol()]);
        }

        //setam rook+king true
        if (piece.getType() == Constants.KING) {
            ((King) piece).setMoved(true);
        } else if (piece.getType() == Constants.ROOK) {
            ((Rook) piece).setMoved(true);
        }

        // verificare sah
        if (currentPlayer.equals(Constants.BLACK)) {
            if (isCheck(to, to, Constants.WHITE)) {
                whiteChecked++;
            }
        } else {
            if (isCheck(to, to, Constants.BLACK)) {
                blackChecked++;
            }
        }

        //Celalalt jucator trece la mutare
        if (currentPlayer.equals(Constants.WHITE)) {
            currentPlayer = Constants.BLACK;
            blackPawnEP = null;
        } else {
            currentPlayer = Constants.WHITE;
            whitePawnEP = null;
        }

        addNewState();

        //In caz ca optiunea force este dezactivata, engine-ul devine activ
        if (!Bot.getInstance().isWait()) {
            if (Bot.getInstance().getPlayingWith().equals(currentPlayer))
//                Bot.getInstance().moveRandom();
                 // Pentru a incerca En Passant
//                Bot.getInstance().movePawn();
                Bot.getInstance().play();
            else {
                Sender.sendMove(Position.encode(new Position[]{from, to}));
            }
        }
    }

    /**
     * Metoda pentru actualizarea tablei de joc
     * cu starea data ca parametru
     */
    public void stateToBoard(State state) {
        board = new Square[8][8];
        blackList.clear();
        whiteList.clear();

        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                board[row][col] = new Square();
            }
        }

        for (int i = 0; i < 6; ++i) {
            whiteList.add(new ArrayList<>());
            blackList.add(new ArrayList<>());
            for (Piece piece : state.getWhiteList().get(i)) {
                Piece newPiece = copyPiece(piece);
                board[piece.getPosition().getRow()][piece.getPosition().getCol()] = new Square(newPiece);
                whiteList.get(i).add(newPiece);
            }

            for (Piece piece : state.getBlackList().get(i)) {
                Piece newPiece = copyPiece(piece);
                board[piece.getPosition().getRow()][piece.getPosition().getCol()] = new Square(newPiece);
                blackList.get(i).add(newPiece);
            }
        }

        blackPawnEP = state.getBlackPawnEP();
        whitePawnEP = state.getWhitePawnEP();
        currentPlayer = state.getCurrentPlayer();
        flag = state.isFlag();
        promotionType = state.getPromotionType();
        dummyMoves = state.getDummyMoves();
        whiteChecked = state.getWhiteChecked();
        blackChecked = state.getBlackChecked();
        whiteCastle = state.isWhiteCastle();
        blackCastle = state.isBlackCastle();

    }

    /**
     * Metoda pentru verificarea finalizarii
     * meciului
     */
    public int gameFinished() {
        // Threefold repetition
        for (State state : states) {
            if (state.getAppearances() == 3) {
                return Constants.DRAW;
            }
        }

        // 50Moves repetition
        if (dummyMoves == 100) {
            return Constants.DRAW;
        }

        // Stalemate
        King king;
        boolean noMovesLeft = true;
        if (currentPlayer.equals(Constants.WHITE)) {
            king = (King) whiteList.get(Constants.KING - 1).get(0);

            for (int i = 0; i < 6; ++i) {
                for (Piece piece : whiteList.get(i)) {
                    piece.possibleMoves();
                    if (!piece.getMovesList().isEmpty()) {
                        noMovesLeft = false;
                        break;
                    }
                }
                if (!noMovesLeft) {
                    break;
                }
            }
        } else {
            king = (King) blackList.get(Constants.KING - 1).get(0);

            for (int i = 0; i < 6; ++i) {
                for (Piece piece : blackList.get(i)) {
                    piece.possibleMoves();
                    if (!piece.getMovesList().isEmpty()) {
                        noMovesLeft = false;
                        break;
                    }
                }
                if (!noMovesLeft) {
                    break;
                }
            }
        }

        if (noMovesLeft) {
            if (!isCheck(king.getPosition(), king.getPosition(), king.getColour())) {
                return Constants.DRAW;
            } else {
                return Constants.WIN;
            }
        }

        // 3-check-chess
        if (whiteChecked == 3 || blackChecked == 3) {
            return Constants.WIN;
        }

        return 0;
    }

    /**
     * Afisare tabla + cei 2 vectori
     */
    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            System.out.print("#");
            for (int col = 0; col < 8; col++) {
                if (board[row][col].isEmpty()) {
                    System.out.print("0 ");
                } else {
                    System.out.print(board[row][col].getPiece().getType() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("# " + dummyMoves + " " + blackChecked + " " + whiteChecked);
    }

    /**
     * Metoda pentru generarea si adaugarea unei noi stari la istoric
     */
    public void addNewState() {
        State s = new State();
        for (int i = 0; i < 6; ++i) {
            s.getBlackList().add(new ArrayList<>());
            s.getWhiteList().add(new ArrayList<>());
            for (Piece piece : blackList.get(i)) {
                s.getBlackList().get(i).add(copyPiece(piece));
            }
            for (Piece piece : whiteList.get(i)) {
                s.getWhiteList().get(i).add(copyPiece(piece));
            }

        }

        King whiteKing = (King) whiteList.get(Constants.KING - 1).get(0);
        King blackKing = (King) blackList.get(Constants.KING - 1).get(0);
        ArrayList<Piece> whiteRookList = whiteList.get(Constants.ROOK - 1);
        ArrayList<Piece> blackRookList = blackList.get(Constants.ROOK - 1);

        if (!whiteKing.isMoved()) {
            for (int i = 0; i < whiteRookList.size(); ++i) {
                if (!((Rook) whiteRookList.get(i)).isMoved()) {
                    if (whiteRookList.get(i).getPosition().equals(new Position(7, 0))) {
                        s.setWhiteCastlingBig(true);
                    } else {
                        s.setWhiteCastlingSmall(true);
                    }
                }
            }
        }

        if (!blackKing.isMoved()) {
            for (int i = 0; i < blackRookList.size(); ++i) {
                if (!((Rook) blackRookList.get(i)).isMoved()) {
                    if (blackRookList.get(i).getPosition().equals(new Position(0, 0))) {
                        s.setBlackCastlingBig(true);
                    } else {
                        s.setBlackCastlingSmall(true);
                    }
                }
            }
        }

        s.setBlackPawnEP(blackPawnEP);
        s.setWhitePawnEP(whitePawnEP);

        s.setCurrentPlayer(currentPlayer);
        s.setFlag(flag);
        s.setPromotionType(promotionType);

        if (whitePawnEP != null) {
            s.setWhiteEPflag(true);
        }
        if (blackPawnEP != null) {
            s.setBlackEPflag(true);
        }

        s.setDummyMoves(dummyMoves);
        s.setWhiteChecked(whiteChecked);
        s.setBlackChecked(blackChecked);
        s.setPromotionType(promotionType);

        s.setWhiteCastle(whiteCastle);
        s.setBlackCastle(blackCastle);

        Bot.getInstance().getActions().push(s);

        for (State state : states) {
            if (s.equals(state)) {
                state.setAppearances(state.getAppearances() + 1);
                return;
            }
        }

        states.add(s);
    }

    /**
     *
     */
    public Piece copyPiece(Piece piece) {
        Piece newPiece = PieceFactory.factory(piece.getPosition(), piece.getColour(), piece.getType());
        if (newPiece.getType() == Constants.KING) {
            King king = (King) piece;
            King newPiece1 = (King) newPiece;
            newPiece1.setMoved(king.isMoved());
            return newPiece1;
        } else if (newPiece.getType() == Constants.ROOK) {
            Rook rook = (Rook) piece;
            Rook newPiece1 = (Rook) newPiece;
            newPiece1.setMoved(rook.isMoved());
            return newPiece1;
        }
        return newPiece;
    }

    /**
     * Verificare miscare EN-PASSANT
     */
    public void checkENPASSANT(Position from, Position to) {
        Piece piece = board[from.getRow()][from.getCol()].getPiece();

        if (piece.getType() == Constants.PAWN) {
            if (Math.abs(to.getRow() - from.getRow()) == 2) {
                if (piece.getColour().equals(Constants.WHITE)) {
                    whitePawnEP = piece;
                } else {
                    blackPawnEP = piece;
                }
            }
        }
    }

    /**
     * Verificare eligibilitate promovare pion
     */
    public boolean checkPromotion(Square target) {
        Piece piece = target.getPiece();
        if (piece.getType() == Constants.PAWN && (piece.getPosition().getRow() == 0 || piece.getPosition().getRow() == 7)) {
            return true;
        }
        return false;
    }

    /**
     * Stabilire aleatorie promovare pion
     */
    public void promoteTo(Square target) {
        Piece piece = target.getPiece();
        target.setPiece(PieceFactory.factory(piece.getPosition(), piece.getColour(), promotionType));
        if (piece.getColour().equals(Constants.WHITE)) {
            whiteList.get(Constants.PAWN - 1).remove(piece);
            whiteList.get(promotionType - 1).add(target.getPiece());
        }
        else {
            blackList.get(Constants.PAWN - 1).remove(piece);
            blackList.get(promotionType - 1).add(target.getPiece());
        }
    }

    /**
     * Functie pentru posibilitatea ca regele sa se afle in sah
     */
    public boolean isCheck(Position from, Position to, String colour) {
        Piece king, piece;
        if (colour.equals(Constants.BLACK)) {
            king = blackList.get(Constants.KING - 1).get(0);
        } else {
            king = whiteList.get(Constants.KING - 1).get(0);
        }

        int kingRow, kingCol;

        Piece start = board[from.getRow()][from.getCol()].getPiece();
        Piece end = board[to.getRow()][to.getCol()].getPiece();

        // Se elibereaza square-ul vechi + actualizez temporar tabla
        board[from.getRow()][from.getCol()].setPiece(null);
        board[to.getRow()][to.getCol()].setPiece(start);

        if (start.getType() == Constants.KING && start.getColour().equals(colour)) {
            kingRow = to.getRow();
            kingCol = to.getCol();
        } else {
            kingRow = king.getPosition().getRow();
            kingCol = king.getPosition().getCol();
        }

        if (colour.equals(Constants.BLACK)) {
            //caii albi
            int knightSize = whiteList.get(Constants.KNIGHT - 1).size();
            for (int i = 0; i < knightSize; ++i) {
                Piece knight = whiteList.get(Constants.KNIGHT - 1).get(i);
                int knightRow = knight.getPosition().getRow();
                int knightCol = knight.getPosition().getCol();
                if (!start.getColour().equals(colour) || !knight.getPosition().equals(to)) {
                    if (Math.abs(kingRow - knightRow) == 2 && Math.abs(kingCol - knightCol) == 1) {
                        //revert + true
                        board[from.getRow()][from.getCol()].setPiece(start);
                        board[to.getRow()][to.getCol()].setPiece(end);
                        return true;

                    } else if (Math.abs(kingRow - knightRow) == 1 && Math.abs(kingCol - knightCol) == 2) {
                        //revert + true
                        board[from.getRow()][from.getCol()].setPiece(start);
                        board[to.getRow()][to.getCol()].setPiece(end);
                        return true;
                    }
                }
            }

            //pionii albi
            if (kingRow + 1 <= 7 && kingCol - 1 >= 0) {
                piece = board[kingRow + 1][kingCol - 1].getPiece();
                if (piece != null && piece.getType()== Constants.PAWN && !piece.getColour().equals(colour)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                }
            }

            if (kingRow + 1 <= 7 && kingCol + 1 <= 7) {
                piece = board[kingRow + 1][kingCol + 1].getPiece();
                if (piece != null && piece.getType() == Constants.PAWN && !piece.getColour().equals(colour)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                }
            }


            //regele alb
            Piece whiteKing = whiteList.get(Constants.KING - 1).get(0);
            int whiteKingRow = whiteKing.getPosition().getRow();
            int whiteKingCol = whiteKing.getPosition().getCol();
            if (Math.abs(kingRow - whiteKingRow) <= 1 && Math.abs(kingCol - whiteKingCol) <= 1) {
                //revert + true
                board[from.getRow()][from.getCol()].setPiece(start);
                board[to.getRow()][to.getCol()].setPiece(end);
                return true;
            }

        } else if (colour.equals(Constants.WHITE)){
            //caii negri
            int knightSize = blackList.get(Constants.KNIGHT - 1).size();
            for (int i = 0; i < knightSize; ++i) {
                Piece knight = blackList.get(Constants.KNIGHT - 1).get(i);
                int knightRow = knight.getPosition().getRow();
                int knightCol = knight.getPosition().getCol();
                if (!start.getColour().equals(colour) || !knight.getPosition().equals(to)) {
                    if (Math.abs(kingRow - knightRow) == 2 && Math.abs(kingCol - knightCol) == 1) {
                        //revert + true
                        board[from.getRow()][from.getCol()].setPiece(start);
                        board[to.getRow()][to.getCol()].setPiece(end);
                        return true;

                    } else if (Math.abs(kingRow - knightRow) == 1 && Math.abs(kingCol - knightCol) == 2) {
                        //revert + true
                        board[from.getRow()][from.getCol()].setPiece(start);
                        board[to.getRow()][to.getCol()].setPiece(end);
                        return true;

                    }
                }
            }

            //pionii negri
            if (kingRow - 1 >= 0 && kingCol - 1 >= 0) {
                piece = board[kingRow - 1][kingCol - 1].getPiece();
                if (piece != null && piece.getType() == Constants.PAWN && !piece.getColour().equals(colour)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                }
            }

            if (kingRow - 1 >= 0 && kingCol + 1 <= 7) {
                piece = board[kingRow - 1][kingCol + 1].getPiece();
                if (piece != null && piece.getType() == Constants.PAWN && !piece.getColour().equals(colour)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                }
            }

            //regele negru
            Piece blackKing = blackList.get(Constants.KING - 1).get(0);
            int blackKingRow = blackKing.getPosition().getRow();
            int blackKingCol = blackKing.getPosition().getCol();
            if (Math.abs(kingRow - blackKingRow) <= 1 && Math.abs(kingCol - blackKingCol) <= 1) {
                //revert + true
                board[from.getRow()][from.getCol()].setPiece(start);
                board[to.getRow()][to.getCol()].setPiece(end);
                return true;
            }
        }

        // cazuri pe linii

        // stanga
        for (int i = kingCol - 1; i >= 0; i--) {
            piece = board[kingRow][i].getPiece();
            if (piece != null) {
                if (!piece.getColour().equals(colour) && (piece.getType() == Constants.QUEEN || piece.getType() == Constants.ROOK)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                } else
                    break;
            }
        }

        // dreapta
        for (int i = kingCol + 1; i <= 7; i++) {
            piece = board[kingRow][i].getPiece();
            if (piece != null) {
                if (!piece.getColour().equals(colour) && (piece.getType() == Constants.QUEEN || piece.getType() == Constants.ROOK)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                } else
                    break;
            }
        }

        // sus
        for (int i = kingRow - 1; i >= 0; i--) {
            piece = board[i][kingCol].getPiece();
            if (piece != null) {
                if (!piece.getColour().equals(colour) && (piece.getType() == Constants.QUEEN || piece.getType() == Constants.ROOK)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                } else
                    break;
            }
        }

        // jos
        for (int i = kingRow + 1; i <= 7; i++) {
            piece = board[i][kingCol].getPiece();
            if (piece != null) {
                if (!piece.getColour().equals(colour) && (piece.getType() == Constants.QUEEN || piece.getType() == Constants.ROOK)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                } else
                    break;
            }
        }

        // cazuri diagonala

        // dreapta-sus
        for (int i = 1; kingCol + i <= 7 && kingRow - i >= 0; i++) {
            piece = board[kingRow - i][kingCol + i].getPiece();
            if (piece != null) {
                if (!piece.getColour().equals(colour) && (piece.getType() == Constants.QUEEN || piece.getType() == Constants.BISHOP)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                } else
                    break;
            }
        }

        // dreapta-jos
        for (int i = 1; kingCol + i <= 7 && kingRow + i <= 7; i++) {
            piece = board[kingRow + i][kingCol + i].getPiece();
            if (piece != null) {
                if (!piece.getColour().equals(colour) && (piece.getType() == Constants.QUEEN || piece.getType() == Constants.BISHOP)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                } else
                    break;
            }
        }

        // stanga-sus
        for (int i = 1; kingCol - i >= 0 && kingRow - i >= 0; i++) {
            piece = board[kingRow - i][kingCol - i].getPiece();
            if (piece != null) {
                if (!piece.getColour().equals(colour) && (piece.getType() == Constants.QUEEN || piece.getType() == Constants.BISHOP)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                } else
                    break;
            }
        }

        // stanga-jos
        for (int i = 1; kingCol - i >= 0 && kingRow + i <= 7; i++) {
            piece = board[kingRow + i][kingCol - i].getPiece();
            if (piece != null) {
                if (!piece.getColour().equals(colour) && (piece.getType() == Constants.QUEEN || piece.getType() == Constants.BISHOP)) {
                    // revert + true
                    board[from.getRow()][from.getCol()].setPiece(start);
                    board[to.getRow()][to.getCol()].setPiece(end);
                    return true;
                } else
                    break;
            }
        }

        board[from.getRow()][from.getCol()].setPiece(start);
        board[to.getRow()][to.getCol()].setPiece(end);
        return false;
    }

    //Gettere, settere
    public boolean isWhiteCastle() {
        return whiteCastle;
    }

    public void setWhiteCastle(boolean whiteCastle) {
        this.whiteCastle = whiteCastle;
    }

    public boolean isBlackCastle() {
        return blackCastle;
    }

    public void setBlackCastle(boolean blackCastle) {
        this.blackCastle = blackCastle;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    public int getWhiteChecked() {
        return whiteChecked;
    }

    public void setWhiteChecked(int whiteChecked) {
        this.whiteChecked = whiteChecked;
    }

    public int getBlackChecked() {
        return blackChecked;
    }

    public void setBlackChecked(int blackChecked) {
        this.blackChecked = blackChecked;
    }

    public Piece getWhitePawnEP() {
        return whitePawnEP;
    }

    public void setWhitePawnEP(Piece whitePawnEP) {
        this.whitePawnEP = whitePawnEP;
    }

    public Piece getBlackPawnEP() {
        return blackPawnEP;
    }

    public void setBlackPawnEP(Piece blackPawnEP) {
        this.blackPawnEP = blackPawnEP;
    }

    public Square getSquare(Position position) {
        return board[position.getRow()][position.getCol()];
    }

    public ArrayList<ArrayList<Piece>> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(ArrayList<ArrayList<Piece>> whiteList) {
        this.whiteList = whiteList;
    }

    public ArrayList<ArrayList<Piece>> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<ArrayList<Piece>> blackList) {
        this.blackList = blackList;
    }

    public Square[][] getBoard() {
        return board;
    }

    public void setBoard(Square[][] board) {
        this.board = board;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public int getDummyMoves() {
        return dummyMoves;
    }

    public void setDummyMoves(int dummyMoves) {
        this.dummyMoves = dummyMoves;
    }
}

/**
 * Factory pentru piesele de joc
 */
class PieceFactory {
    public static Piece factory(Position position, String colour, int type) {
        switch (type) {
            case Constants.ROOK:
                return new Rook(position, colour);
            case Constants.KNIGHT:
                return new Knight(position, colour);
            case Constants.BISHOP:
                return new Bishop(position, colour);
            case Constants.QUEEN:
                return new Queen(position, colour);
            case Constants.KING:
                return new King(position, colour);
            case Constants.PAWN:
                return new Pawn(position, colour);
            default:
                throw new IllegalArgumentException("Not a valid piece");
        }
    }
}