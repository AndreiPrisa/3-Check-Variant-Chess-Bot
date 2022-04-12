import javax.swing.*;
import java.util.*;

/**
 * Clasa ce coordoneaza actiunile engine-ului
 */
public class Bot {
    //Descriere starea engine-ului (i.e. false - engine-ul este activ)
    private boolean wait;
    //Culoarea cu care joaca engine-ul
    private String playingWith;
    //Pionii alesi la intamplare cu care va muta engine-ul (Etapa 1)
    private Piece whitePawn;
    private Piece blackPawn;

    // Retinem toate configuratiile tablei
    private Stack<State> actions;

    private int promotionType = Constants.QUEEN;

    private static Bot obj = null;
    private Bot() {
        playingWith = Constants.BLACK;
        wait = false;
        actions = new Stack<>();
    }
    public static Bot getInstance() {
        if (obj == null) {
            obj = new Bot();
        }
        return obj;
    }

    /**
     * Metoda pentru a avansa jocul cu miscarea data
     * Este in oglinda cu updateBoard din Chessboard
     */
    public void applyMove(Position from, Position to) {
        ChessBoard chessBoard = ChessBoard.getInstance();
        Square[][] board = chessBoard.getBoard();
        chessBoard.setDummyMoves(chessBoard.getDummyMoves() + 1);
        chessBoard.setFlag(false);
        Piece piece = board[from.getRow()][from.getCol()].getPiece();
        String colour = piece.getColour();
        chessBoard.checkENPASSANT(from, to);

        //Se elibereaza square-ul vechi
        board[from.getRow()][from.getCol()].setPiece(null);

        //Se elimina piesa capturata
        if (!board[to.getRow()][to.getCol()].isEmpty()) {
            chessBoard.setDummyMoves(0);
            Piece capturedPiece = board[to.getRow()][to.getCol()].getPiece();
            if (capturedPiece.getColour().equals(Constants.BLACK))
                chessBoard.getBlackList().get(capturedPiece.getType() - 1).remove(capturedPiece);
            else
                chessBoard.getWhiteList().get(capturedPiece.getType() - 1).remove(capturedPiece);
        } else if (piece.getType() == Constants.PAWN) {         //En-Passant
            chessBoard.setDummyMoves(0);
            if (colour.equals(Constants.WHITE) && chessBoard.getBlackPawnEP() != null) {
                if (chessBoard.getBlackPawnEP().getPosition().getRow() - to.getRow() == 1 && chessBoard.getBlackPawnEP().getPosition().getCol() == to.getCol()) {
                    chessBoard.getBlackList().get(Constants.PAWN - 1).remove(chessBoard.getBlackPawnEP());
                    board[chessBoard.getBlackPawnEP().getPosition().getRow()][chessBoard.getBlackPawnEP().getPosition().getCol()].setPiece(null);
                }
            } else if (colour.equals(Constants.BLACK) && chessBoard.getWhitePawnEP() != null) {
                if (chessBoard.getWhitePawnEP().getPosition().getRow() - to.getRow() == -1 && chessBoard.getWhitePawnEP().getPosition().getCol() == to.getCol()) {
                    chessBoard.getWhiteList().get(Constants.PAWN - 1).remove(chessBoard.getWhitePawnEP());
                    board[chessBoard.getWhitePawnEP().getPosition().getRow()][chessBoard.getWhitePawnEP().getPosition().getCol()].setPiece(null);
                }
            }
        } else if (piece.getType() == Constants.KING) {         //rocade
            //stanga alb
            if (colour.equals(Constants.WHITE) && to.getCol() - from.getCol() == -2) {
                chessBoard.getWhiteList().get(Constants.ROOK - 1).get(0).setPosition(new Position(from.getRow(), from.getCol() - 1));
                board[from.getRow()][0].setPiece(null);
                board[from.getRow()][from.getCol() - 1].setPiece(chessBoard.getWhiteList().get(Constants.ROOK - 1).get(0));
                ((Rook)chessBoard.getWhiteList().get(Constants.ROOK - 1).get(0)).setMoved(true);
                chessBoard.setWhiteCastle(true);
            }

            //dreapta alb
            if (colour.equals(Constants.WHITE) && to.getCol() - from.getCol() == 2) {
                int index = Math.min(chessBoard.getWhiteList().get(Constants.ROOK - 1).size() - 1, 1);
                chessBoard.getWhiteList().get(Constants.ROOK - 1).get(index).setPosition(new Position(from.getRow(), from.getCol() + 1));
                board[from.getRow()][7].setPiece(null);
                board[from.getRow()][from.getCol() + 1].setPiece(chessBoard.getWhiteList().get(Constants.ROOK - 1).get(index));
                ((Rook)chessBoard.getWhiteList().get(Constants.ROOK - 1).get(index)).setMoved(true);
                chessBoard.setWhiteCastle(true);
            }


            //stanga negru
            if (colour.equals(Constants.BLACK) && to.getCol() - from.getCol() == -2) {
                chessBoard.getBlackList().get(Constants.ROOK - 1).get(0).setPosition(new Position(from.getRow(), from.getCol() - 1));
                board[from.getRow()][0].setPiece(null);
                board[from.getRow()][from.getCol() - 1].setPiece(chessBoard.getBlackList().get(Constants.ROOK - 1).get(0));
                ((Rook)chessBoard.getBlackList().get(Constants.ROOK - 1).get(0)).setMoved(true);
                chessBoard.setBlackCastle(true);
            }

            //dreapta negru
            if (colour.equals(Constants.BLACK) && to.getCol() - from.getCol() == 2) {
                int index = Math.min(chessBoard.getBlackList().get(Constants.ROOK - 1).size() - 1, 1);
                chessBoard.getBlackList().get(Constants.ROOK - 1).get(index).setPosition(new Position(from.getRow(), from.getCol() + 1));
                board[from.getRow()][7].setPiece(null);
                board[from.getRow()][from.getCol() + 1].setPiece(chessBoard.getBlackList().get(Constants.ROOK - 1).get(index));
                ((Rook)chessBoard.getBlackList().get(Constants.ROOK - 1).get(index)).setMoved(true);
                chessBoard.setBlackCastle(true);
            }
        }

        //Actualizez tabla interna, precum si coordonatele piesei mutate
        board[to.getRow()][to.getCol()].setPiece(piece);
        piece.setPosition(to);

        chessBoard.setFlag(chessBoard.checkPromotion(board[to.getRow()][to.getCol()]));
        if (chessBoard.isFlag()) {
            chessBoard.setPromotionType(5);
            chessBoard.promoteTo(board[to.getRow()][to.getCol()]);
        }

        //setam rook+king true
        if (piece.getType() == Constants.KING) {
            ((King) piece).setMoved(true);
        } else if (piece.getType() == Constants.ROOK) {
            ((Rook) piece).setMoved(true);
        }

        // verificare sah
        if (chessBoard.getCurrentPlayer().equals(Constants.BLACK)) {
            if (chessBoard.isCheck(to, to, Constants.WHITE)) {
                chessBoard.setWhiteChecked(chessBoard.getWhiteChecked() + 1);
            }
        } else {
            if (chessBoard.isCheck(to, to, Constants.BLACK)) {
                chessBoard.setBlackChecked(chessBoard.getBlackChecked() + 1);
            }
        }


        //Celalalt jucator trece la mutare
        if (chessBoard.getCurrentPlayer().equals(Constants.WHITE)) {
            chessBoard.setCurrentPlayer(Constants.BLACK);
            chessBoard.setBlackPawnEP(null);
        } else {
            chessBoard.setCurrentPlayer(Constants.WHITE);
            chessBoard.setWhitePawnEP(null);
        }

        chessBoard.addNewState();

    }

    /**
     * Metoda pentru revenirea cu o mutare in urma
     * Utila la algoritmul NegaMax
     */
    public void undoMove() {
        ChessBoard chessBoard = ChessBoard.getInstance();
        State oldState = actions.pop();
        State newState;
        State temp = null;
        boolean flag = false;

        for (State state : chessBoard.getStates()) {
            if (oldState.equals(state)) {
                if (state.getAppearances() > 1) {
                    state.setAppearances(state.getAppearances() - 1);
                } else {
                    flag = true;
                    temp = state;
                }
            }
        }
        if (flag) {
            chessBoard.getStates().remove(temp);
        }

        newState = actions.peek();
        chessBoard.stateToBoard(newState);
    }

    /**
     * Metoda pentru evalurea starii curente
     * Utila pentru algoritmul NegaMax
     */
    public float evaluate() {
        ChessBoard chessBoard = ChessBoard.getInstance();

        if (chessBoard.gameFinished() == Constants.WIN) {
            return Constants.MIN_SCORE + 10;
        }

        if (chessBoard.gameFinished() == Constants.DRAW) {
            return 0;
        }


        float scoreWhite = 0;
        float scoreBlack = 0;
        float scorePenalties = 0;

        Piece king = chessBoard.getWhiteList().get(Constants.KING - 1).get(0);
        int row = king.getPosition().getRow();
        int col = king.getPosition().getCol();
        int penalty = 2;
        Square[][] board = chessBoard.getBoard();

        // Penalizari rege alb
        // diagonala dreapta sus
        int counter = 1;
        for (int i = row - 1; i >= 0; --i) {
            // depasire limita
            if (col + counter > 7) {
                break;
            }
            // am dat de o piesa
            if (!board[i][col + counter].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col + counter].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // diagonala dreapta jos
        counter = 1;
        for (int i = row + 1; i <= 7; ++i) {
            // depasire limita
            if (col + counter > 7) {
                break;
            }
            // am dat de o piesa
            if (!board[i][col + counter].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col + counter].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // diagonala stanga sus
        counter = 1;
        for (int i = row - 1; i >= 0; --i) {
            // depasire limita
            if (col - counter < 0) {
                break;
            }
            // am dat de o piesa
            if (!board[i][col - counter].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col - counter].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // diagonala stanga jos
        for (int i = row + 1; i <= 7; ++i) {
            // depasire limita
            if (col - counter < 7) {
                break;
            }
            // am dat de o piesa
            if (!board[i][col - counter].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col - counter].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // verticala sus
        for (int i = row - 1; i >= 0; --i) {
            // am dat de o piesa
            if (!board[i][col].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // verticala jos
        for (int i = row + 1; i <= 7; ++i) {
            // am dat de o piesa
            if (!board[i][col].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        scoreWhite -= scorePenalties;
        scorePenalties = 0;
        king = chessBoard.getBlackList().get(Constants.KING - 1).get(0);
        row = king.getPosition().getRow();
        col = king.getPosition().getCol();

        // Penalizari rege negru
        // diagonala dreapta sus
        counter = 1;
        for (int i = row - 1; i >= 0; --i) {
            // depasire limita
            if (col + counter > 7) {
                break;
            }
            // am dat de o piesa
            if (!board[i][col + counter].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col + counter].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // diagonala dreapta jos
        counter = 1;
        for (int i = row + 1; i <= 7; ++i) {
            // depasire limita
            if (col + counter > 7) {
                break;
            }
            // am dat de o piesa
            if (!board[i][col + counter].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col + counter].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // diagonala stanga sus
        counter = 1;
        for (int i = row - 1; i >= 0; --i) {
            // depasire limita
            if (col - counter < 0) {
                break;
            }
            // am dat de o piesa
            if (!board[i][col - counter].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col - counter].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // diagonala stanga jos
        for (int i = row + 1; i <= 7; ++i) {
            // depasire limita
            if (col - counter < 7) {
                break;
            }
            // am dat de o piesa
            if (!board[i][col - counter].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col - counter].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // verticala sus
        for (int i = row - 1; i >= 0; --i) {
            // am dat de o piesa
            if (!board[i][col].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        // verticala jos
        for (int i = row + 1; i <= 7; ++i) {
            // am dat de o piesa
            if (!board[i][col].isEmpty()) {
                // piesa de culoarea regelui
                if (board[i][col].getPiece().getColour().equals(king.getColour())) {
                    break;
                } else {
                    scorePenalties += penalty;
                }
            } else {
                scorePenalties += penalty;
            }
            break;
        }

        scoreBlack -= scorePenalties;

        // ROCADE
        if (chessBoard.isWhiteCastle()) {
            scoreWhite += 100;
        }
        if (chessBoard.isBlackCastle()) {
            scoreBlack += 100;
        }


        // BISHOPS
        if (chessBoard.getWhiteList().get(Constants.BISHOP - 1).size() == 2) {
            scoreWhite += 20;
        }
        if (chessBoard.getBlackList().get(Constants.BISHOP - 1).size() == 2) {
            scoreBlack += 20;
        }

        for (int i = 0; i < 6; ++i) {
            for (Piece piece : chessBoard.getWhiteList().get(i)) {
                scoreWhite += piece.positionBasedValue();
                scoreWhite += piece.calculateOverall();
            }
        }
        for (int i = 0; i < 6; ++i) {
            for (Piece piece : chessBoard.getBlackList().get(i)) {
                scoreBlack += piece.positionBasedValue();
                scoreBlack += piece.calculateOverall();
            }
        }

        if (chessBoard.getWhiteChecked() == 1) {
            scoreWhite -= 30;
        } else if (chessBoard.getWhiteChecked() == 2) {
            scoreWhite -= 90;
        }

        if (chessBoard.getBlackChecked() == 1) {
            scoreBlack -= 30;
        } else if (chessBoard.getBlackChecked() == 2) {
            scoreBlack -= 90;
        }

        if (chessBoard.getCurrentPlayer().equals(Constants.WHITE)) {
            return scoreWhite - scoreBlack;
        } else {
            return scoreBlack - scoreWhite;
        }
    }

    /**
     * Metoda pentru obtinerea miscarilor de
     * capturare si de dat sah
     * Utila pentru algoritmul Quiescence
     */
    public ArrayList<MyPair<Position, Position>> generateAttackingMoves() {
        ChessBoard chessBoard = ChessBoard.getInstance();
        ArrayList<MyPair<Position, Position>> moves = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            if (chessBoard.getCurrentPlayer().equals(Constants.WHITE)) {
                for (Piece piece : chessBoard.getWhiteList().get(i)) {
                    piece.possibleMoves();
                    for (int j = 0; j < piece.getMovesList().size(); ++j) {
                        MyPair<Position, Position> move = new MyPair<>(piece.getPosition(), piece.getMovesList().get(j));
                        if (!chessBoard.getSquare(move.getSnd()).isEmpty() || chessBoard.isCheck(move.getFst(), move.getSnd(), Constants.BLACK)) {
                            moves.add(move);
                        }
                    }
                }
            } else {
                for (Piece piece : chessBoard.getBlackList().get(i)) {
                    piece.possibleMoves();
                    for (int j = 0; j < piece.getMovesList().size(); ++j) {
                        MyPair<Position, Position> move = new MyPair<>(piece.getPosition(), piece.getMovesList().get(j));
                        if (!chessBoard.getSquare(move.getSnd()).isEmpty() || chessBoard.isCheck(move.getFst(), move.getSnd(), Constants.WHITE)) {
                            moves.add(move);
                        }
                    }
                }
            }
        }

        moves.sort(new Comparator<MyPair<Position, Position>>() {
            @Override
            public int compare(MyPair<Position, Position> t1, MyPair<Position, Position> t2) {
                ChessBoard chessBoard1 = ChessBoard.getInstance();
                Position to1 = t1.getSnd();
                Position to2 = t2.getSnd();
                Position from1 = t1.getFst();
                Position from2 = t2.getFst();

                Piece p12 = chessBoard1.getSquare(to1).getPiece();
                Piece p22 = chessBoard1.getSquare(to2).getPiece();
                Piece p11 = chessBoard1.getSquare(from1).getPiece();
                Piece p21 = chessBoard1.getSquare(from2).getPiece();
                Float[][] matrix1;
                Float[][] matrix2;

                if (p21.getColour().equals(Constants.WHITE)) {
                    matrix2 = Evaluation.getInstance().getEvalMatrix().get(0).get(p21.getType());
                } else {
                    matrix2 = Evaluation.getInstance().getEvalMatrix().get(1).get(p21.getType());
                }

                if (p11.getColour().equals(Constants.WHITE)) {
                    matrix1 = Evaluation.getInstance().getEvalMatrix().get(0).get(p11.getType());
                } else {
                    matrix1 = Evaluation.getInstance().getEvalMatrix().get(1).get(p11.getType());
                }

                if (p12 == null) {
                    if (p22 == null) {
                        if (matrix2[to2.getRow()][to2.getCol()] - matrix1[to1.getRow()][to1.getCol()] > 0) {
                            return 1;
                        } else if (matrix2[to2.getRow()][to2.getCol()] - matrix1[to1.getRow()][to1.getCol()] < 0) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } else {
                        if (p22.getValue() + matrix2[to2.getRow()][to2.getCol()] - matrix1[to1.getRow()][to1.getCol()] > 0) {
                            return 1;
                        } else if (p22.getValue() + matrix2[to2.getRow()][to2.getCol()] - matrix1[to1.getRow()][to1.getCol()] < 0) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }

                if (p22 == null) {
                    if (matrix2[to2.getRow()][to2.getCol()] - (p11.getValue() + matrix1[to1.getRow()][to1.getCol()]) > 0) {
                        return 1;
                    } else if (matrix2[to2.getRow()][to2.getCol()] - (p11.getValue() + matrix1[to1.getRow()][to1.getCol()]) < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }

                if (p22.getValue() + matrix2[to2.getRow()][to2.getCol()] - (p12.getValue() + matrix1[to1.getRow()][to1.getCol()]) > 0) {
                    return 1;
                } else if (p22.getValue() + matrix2[to2.getRow()][to2.getCol()] - (p12.getValue() + matrix1[to1.getRow()][to1.getCol()]) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return moves;
    }


    /**
     * Metoda pentru ubtinerea tuturor miscarilor
     * Utila pentru algoritmul NegaMax
     */
    public ArrayList<MyPair<Position, Position>> generateMoves() {
        ChessBoard chessBoard = ChessBoard.getInstance();
        ArrayList<MyPair<Position, Position>> moves = new ArrayList<>();
        for (int i = 0; i < 6; ++i) {
            if (chessBoard.getCurrentPlayer().equals(Constants.WHITE)) {
                for (Piece piece : chessBoard.getWhiteList().get(i)) {
                    piece.possibleMoves();
                    for (int j = 0; j < piece.getMovesList().size(); ++j) {
                        moves.add(new MyPair<>(piece.getPosition(), piece.getMovesList().get(j)));
                    }
                }
            } else {
                for (Piece piece : chessBoard.getBlackList().get(i)) {
                    piece.possibleMoves();
                    for (int j = 0; j < piece.getMovesList().size(); ++j) {
                        moves.add(new MyPair<>(piece.getPosition(), piece.getMovesList().get(j)));
                    }
                }
            }
        }

        moves.sort(new Comparator<MyPair<Position, Position>>() {
            @Override
            public int compare(MyPair<Position, Position> t1, MyPair<Position, Position> t2) {
                ChessBoard chessBoard1 = ChessBoard.getInstance();
                Position to1 = t1.getSnd();
                Position to2 = t2.getSnd();
                Position from1 = t1.getFst();
                Position from2 = t2.getFst();

                Piece p11 = chessBoard1.getSquare(from1).getPiece();
                Piece p21 = chessBoard1.getSquare(from2).getPiece();
                Float[][] matrix1;
                Float[][] matrix2;
                if (p21.getColour().equals(Constants.WHITE)) {
                    matrix2 = Evaluation.getInstance().getEvalMatrix().get(0).get(p21.getType());
                } else {
                    matrix2 = Evaluation.getInstance().getEvalMatrix().get(1).get(p21.getType());
                }

                if (p11.getColour().equals(Constants.WHITE)) {
                    matrix1 = Evaluation.getInstance().getEvalMatrix().get(0).get(p11.getType());
                } else {
                    matrix1 = Evaluation.getInstance().getEvalMatrix().get(1).get(p11.getType());
                }


                if (!chessBoard1.getSquare(to1).isEmpty()) {
                    if (!chessBoard1.getSquare(to2).isEmpty()) {
                        Piece p12 = chessBoard1.getSquare(to1).getPiece();
                        Piece p22 = chessBoard1.getSquare(to2).getPiece();

                        if (p22.getValue() + matrix2[to2.getRow()][to2.getCol()] - (p12.getValue() + matrix1[to1.getRow()][to1.getCol()]) > 0) {
                            return 1;
                        } else if (p22.getValue() + matrix2[to2.getRow()][to2.getCol()] - (p12.getValue() + matrix1[to1.getRow()][to1.getCol()]) < 0) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } else {
                        return -1;
                    }
                } else if (!chessBoard1.getSquare(to2).isEmpty()) {
                    return 1;
                }


                if (matrix2[to2.getRow()][to2.getCol()] - matrix1[to1.getRow()][to1.getCol()] > 0) {
                    return 1;
                } else if (matrix2[to2.getRow()][to2.getCol()] - matrix1[to1.getRow()][to1.getCol()] < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return moves;
    }

    /**
     * Metoda pentru aplicarea algoritmului Quiescence
     */
    public Float quiescence(float alpha, float beta) {
        float standPat = evaluate();
        if (standPat >= beta) {
            return beta;
        }
        if (alpha < standPat) {
            alpha = standPat;
        }

        ArrayList<MyPair<Position, Position>> allMoves = generateAttackingMoves();
        for (MyPair<Position, Position> move : allMoves) {
            applyMove(move.getFst(), move.getSnd());

            float score = -quiescence(-beta, -alpha);
            undoMove();

            if (score >= beta) {
                return beta;
            }
            if (score > alpha) {
                alpha = score;
            }
        }

        return alpha;
    }

    /**
     * Metoda pentru aplicarea algoritmului NegaMax
     * + taieri alpha-beta
     */
    public MyPair<Float, MyPair<Position, Position>> alphabetaNegamax(int depth, float alpha, float beta, MyPair<Position, Position> prevMove) {
        ChessBoard chessBoard = ChessBoard.getInstance();
        if (chessBoard.gameFinished() != 0) {
            return new MyPair<>(evaluate(), prevMove);
        }

        if (depth == 0) {
            return new MyPair<>(quiescence(alpha, beta), prevMove);
        }

        ArrayList<MyPair<Position, Position>> allMoves = generateMoves();
        MyPair<Position, Position> bestMove = null;
        float bestScore = Constants.MIN_SCORE;

        for (MyPair<Position, Position> move : allMoves) {
            applyMove(move.getFst(), move.getSnd());
            MyPair<Float, MyPair<Position, Position>> temp = alphabetaNegamax(depth - 1, -beta, -alpha, move);
            float score = -temp.getFst();
            undoMove();

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }

            if (bestScore > alpha) {
                alpha = bestScore;
            }

            if (alpha >= beta) {
                break;
            }
        }
        return new MyPair<>(bestScore, bestMove);
    }

    /**
     * Metoda wrapper pentru apelarea metodei alphabetaNegamax
     */
    public void play() {
        ChessBoard chessBoard = ChessBoard.getInstance();
        int state = chessBoard.gameFinished();
        switch (state) {
            case Constants.DRAW:
                System.out.println("1/2-1/2 {Draw player}");
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
        MyPair<Float, MyPair<Position, Position>> pair = alphabetaNegamax(Constants.DEPTH, Constants.MIN_SCORE, Constants.MAX_SCORE, null);
        if (pair.getSnd() == null) {
            Sender.resign();
            return;
        }

        chessBoard.setPromotionType(promotionType);
        chessBoard.updateBoard(pair.getSnd().getFst(), pair.getSnd().getSnd());
    }


    /**
     * Descrie actiunea engine-ului pe tura sa (Etapa 1)
     */
    public void movePawn() {
        ChessBoard chessBoard = ChessBoard.getInstance();
        int state = chessBoard.gameFinished();
        switch (state) {
            case Constants.DRAW:
                System.out.println("1/2-1/2 {Draw player}");
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

        //Mutarea cu pionul negru
        if (playingWith.equals(Constants.BLACK) && chessBoard.getBlackList().get(Constants.PAWN - 1).contains(blackPawn)) {
            ArrayList<Position> movesList = blackPawn.possibleMoves();
            //Daca exista o mutare posibila, o efectuam, altfel resign
            if (!movesList.isEmpty())
                chessBoard.updateBoard(blackPawn.getPosition(), movesList.get(0));
            else
                Sender.resign();
            //Analog pionului negru
        } else if (playingWith.equals(Constants.WHITE) && chessBoard.getWhiteList().get(Constants.PAWN - 1).contains(whitePawn)) {
            ArrayList<Position> movesList = whitePawn.possibleMoves();
            if (!movesList.isEmpty())
                chessBoard.updateBoard(whitePawn.getPosition(), movesList.get(0));
            else
                Sender.resign();
        } else {
            //Daca piesa este regina sau a fost capturata, dam resign
            Sender.resign();
        }
    }




    /**
     * Alegere aleatorie a mutarii engine-ului (Etapa 2)
     */
    public void moveRandom() {
        ArrayList<Position> castling = new ArrayList<>();
        ChessBoard chessBoard = ChessBoard.getInstance();

        int state = chessBoard.gameFinished();
        switch (state) {
            case Constants.DRAW:
                System.out.println("1/2-1/2 {Draw player}");
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

        Piece king;
        Random random = new Random();
        chessBoard.setPromotionType(Constants.forPromotion[random.nextInt(Constants.forPromotion.length)]);
        actions.peek().setPromotionType(chessBoard.getPromotionType());
        if (playingWith.equals(Constants.BLACK)) {
            king = chessBoard.getBlackList().get(Constants.KING - 1).get(0);
        } else {
            king = chessBoard.getWhiteList().get(Constants.KING - 1).get(0);
        }

        king.possibleMoves();

        // Prioritizam rocadele
        for (int i = 0; i < king.getMovesList().size() && i < 2; ++i) {
            if (Math.abs(king.getMovesList().get(i).getCol() - king.getPosition().getCol()) == 2) {
                castling.add(king.getMovesList().get(i));
            } else {
                break;
            }
        }

        // Executam intai rocada daca exista cel putin una
        if (!castling.isEmpty()) {
            Position to = castling.get(random.nextInt(castling.size()));
            chessBoard.updateBoard(king.getPosition(), to);
        } else {
            ArrayList<Piece> pieces = new ArrayList<>();
            // Selectam in functie de culoare piesele ce contin cel putin o miscare posibila
            if (playingWith.equals(Constants.BLACK)) {
                for (int i = 0; i < chessBoard.getBlackList().size(); ++i) {
                    for (Piece piece : chessBoard.getBlackList().get(i)) {
                        if (!piece.possibleMoves().isEmpty()) {
                            pieces.add(piece);
                        }
                    }
                }
            } else {
                for (int i = 0; i < chessBoard.getWhiteList().size(); ++i) {
                    for (Piece piece : chessBoard.getWhiteList().get(i)) {
                        if (!piece.possibleMoves().isEmpty()) {
                            pieces.add(piece);
                        }
                    }
                }
            }

            // Alegem la intamplare o piesa si dupa aceea o miscare a ei
            if (!pieces.isEmpty()) {
                Piece p = pieces.get(random.nextInt(pieces.size()));
                chessBoard.updateBoard(p.getPosition(), p.getMovesList().get(random.nextInt(p.getMovesList().size())));
            } else {
                Sender.resign(); // daca nu avem piese cu care sa mutam dam resign
            }
        }
    }

    //Gettere, settere
    public Stack<State> getActions() {
        return actions;
    }

    public void setActions(Stack<State> actions) {
        this.actions = actions;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public String getPlayingWith() {
        return playingWith;
    }

    public void setPlayingWith(String playingWith) {
        this.playingWith = playingWith;
    }

    public Piece getWhitePawn() {
        return whitePawn;
    }

    public void setWhitePawn(Piece whitePawn) {
        this.whitePawn = whitePawn;
    }

    public Piece getBlackPawn() {
        return blackPawn;
    }

    public void setBlackPawn(Piece blackPawn) {
        this.blackPawn = blackPawn;
    }
}

class MyPair<T, V> {
    private T fst;
    private V snd;

    public MyPair(T fst, V snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public T getFst() {
        return fst;
    }

    public void setFst(T fst) {
        this.fst = fst;
    }

    public V getSnd() {
        return snd;
    }

    public void setSnd(V snd) {
        this.snd = snd;
    }

    public String toString() {
        return fst.toString() + " " + snd.toString();
    }
}


