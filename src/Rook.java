import java.util.ArrayList;

public class Rook extends Piece {
    private boolean moved;
    public Rook(Position position, String colour) {
        super(position, colour, Constants.ROOK);
        setValue(Constants.ROOK_VALUE);
    }

    @Override
    ArrayList<Position> possibleMoves() {
        Square[][] board = ChessBoard.getInstance().getBoard();
        int row = this.getPosition().getRow();
        int col = this.getPosition().getCol();
        this.getMovesList().clear();
        setAttack(0);
        setProtect(0);

        //stanga
        for (int i = col - 1; i >= 0; --i) {
            Piece piece = board[row][i].getPiece();
            if (piece != null) {
                //caz capturare
                if (!piece.getColour().equals(this.getColour())) {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row, i), this.getColour())) {
                        this.getMovesList().add(new Position(row, i));
                        setAttack(getAttack() + Constants.attacking[board[row][i].getPiece().getType()]);
                    }
                } else {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row, i), this.getColour())) {
                        setProtect(getProtect() + Constants.protection[board[row][i].getPiece().getType()]);
                    }
                }
                //piesa de-a mea
                break;
            }
            //casuta libera
            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row, i), this.getColour()))
                this.getMovesList().add(new Position(row, i));
        }

        //dreapta
        for (int i = col + 1; i <= 7; ++i) {
            Piece piece = board[row][i].getPiece();
            if (piece != null) {
                //caz capturare
                if (!piece.getColour().equals(this.getColour())) {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row, i), this.getColour())) {
                        this.getMovesList().add(new Position(row, i));
                        setAttack(getAttack() + Constants.attacking[board[row][i].getPiece().getType()]);
                    }
                } else {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row, i), this.getColour())) {
                        setProtect(getProtect() + Constants.protection[board[row][i].getPiece().getType()]);
                    }
                }
                //piesa de-a mea
                break;
            }
            //casuta libera
            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row, i), this.getColour()))
                this.getMovesList().add(new Position(row, i));
        }

        //sus
        for (int i = row - 1; i >= 0; --i) {
            Piece piece = board[i][col].getPiece();
            if (piece != null) {
                //caz capturare
                if (!piece.getColour().equals(this.getColour())) {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(i, col), this.getColour())) {
                        this.getMovesList().add(new Position(i, col));
                        setAttack(getAttack() + Constants.attacking[board[i][col].getPiece().getType()]);
                    }
                } else {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(i, col), this.getColour())) {
                        setProtect(getProtect() + Constants.protection[board[i][col].getPiece().getType()]);
                    }
                }
                //piesa de-a mea
                break;
            }
            //casuta libera
            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(i, col), this.getColour()))
                this.getMovesList().add(new Position(i, col));
        }

        //jos
        for (int i = row + 1; i <= 7; ++i) {
            Piece piece = board[i][col].getPiece();
            if (piece != null) {
                //caz capturare
                if (!piece.getColour().equals(this.getColour())) {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(i, col), this.getColour())) {
                        this.getMovesList().add(new Position(i, col));
                        setAttack(getAttack() + Constants.attacking[board[i][col].getPiece().getType()]);
                    }
                } else {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(i, col), this.getColour())) {
                        setProtect(getProtect() + Constants.protection[board[i][col].getPiece().getType()]);
                    }
                }
                //piesa de-a mea
                break;
            }
            //casuta libera
            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(i, col), this.getColour()))
                this.getMovesList().add(new Position(i, col));
        }

        return this.getMovesList();
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
