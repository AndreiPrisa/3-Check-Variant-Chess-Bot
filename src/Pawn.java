import java.util.ArrayList;

public class Pawn extends Piece {


    public Pawn(Position position, String colour) {
        super(position, colour, Constants.PAWN);
        setValue(Constants.PAWN_VALUE);
    }

    @Override
    ArrayList<Position> possibleMoves() {
        Square[][] board = ChessBoard.getInstance().getBoard();
        int row = this.getPosition().getRow();
        int col = this.getPosition().getCol();
        this.getMovesList().clear();
        setAttack(0);
        setProtect(0);

        //Miscari posibile pion alb
        if (this.getColour().equals(Constants.WHITE)) {
            //Capturare EN-PASSANT
            Piece blackPawnEP = ChessBoard.getInstance().getBlackPawnEP();
            if (blackPawnEP != null && Math.abs(blackPawnEP.getPosition().getCol() - col) == 1 && blackPawnEP.getPosition().getRow() == row) {
                if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - 1, blackPawnEP.getPosition().getCol()), Constants.WHITE)) {
                    this.getMovesList().add(new Position(row - 1, blackPawnEP.getPosition().getCol()));
                    setAttack(getAttack() + Constants.attacking[Constants.PAWN]);
                }
            }

            //Se verifica daca exista posibilitatea de capturare diagonal-stanga/diagonal-dreapta
            if ((row - 1 >= 0 && col - 1 >= 0) && !board[row - 1][col - 1].isEmpty()) {
                if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - 1, col - 1), Constants.WHITE)) {
                    if (board[row - 1][col - 1].getPiece().getColour().equals(Constants.BLACK)) {
                        this.getMovesList().add(new Position(row - 1, col - 1));
                        setAttack(getAttack() + Constants.attacking[board[row - 1][col - 1].getPiece().getType()]);
                    } else {
                        setProtect(getProtect() + Constants.protection[board[row - 1][col -1 ].getPiece().getType()]);
                    }
                }
            }
            if ((row - 1 >= 0 && col + 1 < 8) && !board[row - 1][col + 1].isEmpty()) {
                if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - 1, col + 1), Constants.WHITE)) {
                    if (board[row - 1][col + 1].getPiece().getColour().equals(Constants.BLACK)) {
                        this.getMovesList().add(new Position(row - 1, col + 1));
                        setAttack(getAttack() + Constants.attacking[board[row - 1][col + 1].getPiece().getType()]);
                    } else {
                        setProtect(getProtect() + Constants.protection[board[row - 1][col + 1].getPiece().getType()]);
                    }
                }
            }
            //Se verifica daca se poate avansa cu cel putin o casuta
            if ((row - 1 >= 0) && board[row - 1][col].isEmpty()) {
                if (row == 6) {
                    //Se verifica daca se poate avansa cu 2 casute
                    if (board[row - 2][col].isEmpty()) {
                        if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - 2, col), Constants.WHITE))
                            this.getMovesList().add(new Position(row - 2, col));
                    }
                }
                if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - 1, col), Constants.WHITE))
                    this.getMovesList().add(new Position(row - 1, col));
            }
            //Miscari posibile pion negru; analog pion alb
        } else {
            //Capturare EN-PASSANT
            Piece whitePawnEP = ChessBoard.getInstance().getWhitePawnEP();
            if (whitePawnEP != null && Math.abs(whitePawnEP.getPosition().getCol() - col) == 1 && whitePawnEP.getPosition().getRow() == row) {
                if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + 1, whitePawnEP.getPosition().getCol()), Constants.BLACK)) {
                    this.getMovesList().add(new Position(row + 1, whitePawnEP.getPosition().getCol()));
                    setAttack(getAttack() + Constants.attacking[Constants.PAWN]);
                }
            }

            if ((row + 1 < 8 && col - 1 >= 0) && !board[row + 1][col - 1].isEmpty()) {
                if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + 1, col - 1), Constants.BLACK)) {
                    if (board[row + 1][col - 1].getPiece().getColour().equals(Constants.WHITE)) {
                        this.getMovesList().add(new Position(row + 1, col - 1));
                        setAttack(getAttack() + Constants.attacking[board[row + 1][col - 1].getPiece().getType()]);
                    } else {
                        setProtect(getProtect() + Constants.protection[board[row + 1][col - 1].getPiece().getType()]);
                    }
                }

            }
            if ((row + 1 < 8 && col + 1 < 8) && !board[row + 1][col + 1].isEmpty()) {
                if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + 1, col + 1), Constants.BLACK)) {
                    if (board[row + 1][col + 1].getPiece().getColour().equals(Constants.WHITE)) {
                        this.getMovesList().add(new Position(row + 1, col + 1));
                        setAttack(getAttack() + Constants.attacking[board[row + 1][col + 1].getPiece().getType()]);
                    } else {
                        setProtect(getProtect() + Constants.protection[board[row + 1][col + 1].getPiece().getType()]);
                    }
                }
            }
            if ((row + 1 < 8) && board[row + 1][col].isEmpty()) {
                if (row == 1) {
                    if (board[row + 2][col].isEmpty()) {
                        if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + 2, col), Constants.BLACK))
                            this.getMovesList().add(new Position(row + 2, col));
                    }
                }
                if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + 1, col), Constants.BLACK))
                    this.getMovesList().add(new Position(row + 1, col));
            }
        }

        return this.getMovesList();
    }

}
