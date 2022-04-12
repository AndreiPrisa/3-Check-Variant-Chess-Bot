import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(Position position, String colour) {
        super(position, colour, Constants.QUEEN);
        setValue(Constants.QUEEN_VALUE);
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

        //diagonala stanga-sus
        for (int i = 1; col - i >= 0 && row - i >= 0; ++i) {
            Piece piece = board[row - i][col - i].getPiece();
            if (piece != null) {
                //caz de capturare
                if (!piece.getColour().equals(this.getColour())) {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - i, col - i), this.getColour())) {
                        this.getMovesList().add(new Position(row - i, col - i));
                        setAttack(getAttack() + Constants.attacking[board[row - i][col - i].getPiece().getType()]);
                    }
                } else {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - i, col - i), this.getColour())) {
                        setProtect(getProtect() + Constants.protection[board[row - i][col - i].getPiece().getType()]);
                    }
                }
                //piesa de-a mea
                break;
            }
            //casuta libera
            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - i, col - i), this.getColour()))
                this.getMovesList().add(new Position(row - i, col - i));
        }

        //diagonala stanga-jos
        for (int i = 1; col - i >= 0 && row + i <= 7; ++i) {
            Piece piece = board[row + i][col - i].getPiece();
            if (piece != null) {
                //caz de capturare
                if (!piece.getColour().equals(this.getColour())) {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + i, col - i), this.getColour())) {
                        this.getMovesList().add(new Position(row + i, col - i));
                        setAttack(getAttack() + Constants.attacking[board[row + i][col - i].getPiece().getType()]);
                    }
                } else {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + i, col - i), this.getColour())) {
                        setProtect(getProtect() + Constants.protection[board[row + i][col - i].getPiece().getType()]);
                    }
                }
                //piesa de-a mea
                break;
            }
            //casuta libera
            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + i, col - i), this.getColour()))
                this.getMovesList().add(new Position(row + i, col - i));
        }

        //diagonala dreapta-sus
        for (int i = 1; col + i <= 7 && row - i >= 0; ++i) {
            Piece piece = board[row - i][col + i].getPiece();
            if (piece != null) {
                //caz de capturare
                if (!piece.getColour().equals(this.getColour())) {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - i, col + i), this.getColour())) {
                        this.getMovesList().add(new Position(row - i, col + i));
                        setAttack(getAttack() + Constants.attacking[board[row - i][col + i].getPiece().getType()]);
                    }
                } else {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - i, col + i), this.getColour())) {
                        setProtect(getProtect() + Constants.protection[board[row - i][col + i].getPiece().getType()]);
                    }
                }
                //piesa de-a mea
                break;
            }
            //casuta libera
            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row - i, col + i), this.getColour()))
                this.getMovesList().add(new Position(row - i, col + i));
        }

        //diagonala dreapta-jos
        for (int i = 1; col + i <= 7 && row + i <= 7; ++i) {
            Piece piece = board[row + i][col + i].getPiece();
            if (piece != null) {
                //caz de capturare
                if (!piece.getColour().equals(this.getColour())) {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + i, col + i), this.getColour())) {
                        this.getMovesList().add(new Position(row + i, col + i));
                        setAttack(getAttack() + Constants.attacking[board[row + i][col + i].getPiece().getType()]);
                    }
                } else {
                    if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + i, col + i), this.getColour())) {
                        setProtect(getProtect() + Constants.protection[board[row + i][col + i].getPiece().getType()]);
                    }
                }
                //piesa de-a mea
                break;
            }
            //casuta libera
            if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row + i, col + i), this.getColour()))
                this.getMovesList().add(new Position(row + i, col + i));
        }

        return this.getMovesList();
    }
}
