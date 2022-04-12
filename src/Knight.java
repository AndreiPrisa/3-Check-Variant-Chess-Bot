import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Position position, String colour) {
        super(position, colour, Constants.KNIGHT);
        setValue(Constants.KNIGHT_VALUE);
    }

    @Override
    ArrayList<Position> possibleMoves() {
        Square[][] board = ChessBoard.getInstance().getBoard();
        int row = this.getPosition().getRow();
        int col = this.getPosition().getCol();
        int rowAux, colAux;
        this.getMovesList().clear();
        setAttack(0);
        setProtect(0);

        for (int dc = -1; dc <= 1; dc += 2)
            for(int dr = -1; dr <= 1; dr += 2)
                for (int ord = 1; ord <= 2; ord++) {
                    colAux = dc * (3 - ord) + col;
                    rowAux = dr * ord + row;
                    if (colAux >= 0 && colAux <= 7 && rowAux >= 0 && rowAux <= 7) {
                        Piece piece = board[rowAux][colAux].getPiece();
                        if (piece == null) {
                            // spatiu liber
                            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(rowAux, colAux), this.getColour())) {
                                this.getMovesList().add(new Position(rowAux, colAux));
                            }
                        } else if (!piece.getColour().equals(this.getColour())) {
                            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(rowAux, colAux), this.getColour())) {
                                this.getMovesList().add(new Position(rowAux, colAux));
                                setAttack(getAttack() + Constants.attacking[board[rowAux][colAux].getPiece().getType()]);
                            }
                        } else {
                            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(rowAux, colAux), this.getColour())) {
                                setProtect(getProtect() + Constants.protection[board[rowAux][colAux].getPiece().getType()]);
                            }
                        }
                    }
                }

        return this.getMovesList();
    }
}
