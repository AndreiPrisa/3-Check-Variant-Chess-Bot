import java.util.ArrayList;

public class King extends Piece {
    private boolean moved;


    public King(Position position, String colour) {
        super(position, colour, Constants.KING);
        setValue(Constants.KING_VALUE);
    }

    @Override
    ArrayList<Position> possibleMoves() {
        Square[][] board = ChessBoard.getInstance().getBoard();
        int row = this.getPosition().getRow();
        int col = this.getPosition().getCol();
        this.getMovesList().clear();
        int rowAux, colAux;
        ArrayList<Piece> whiteRookList = ChessBoard.getInstance().getWhiteList().get(Constants.ROOK - 1);
        ArrayList<Piece> blackRookList = ChessBoard.getInstance().getBlackList().get(Constants.ROOK - 1);

        // verificam daca se pot face rocadele
        if (!moved && !ChessBoard.getInstance().isCheck(this.getPosition(), this.getPosition(), this.getColour())) {
            if (this.getColour().equals(Constants.WHITE)) {
                for (int i = 0; i < whiteRookList.size(); ++i) {
                    boolean flag = false;
                    if (!((Rook) whiteRookList.get(i)).isMoved()) {
                        int direction = (whiteRookList.get(i).getPosition().getCol() - col) / Math.abs(col - whiteRookList.get(i).getPosition().getCol());
                        for (int j = col + direction; j != col + direction * 3; j += direction) {
                            if (!board[row][j].isEmpty()) {
                                flag = true;
                                break;
                            }
                            if (ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row, j), this.getColour())) {
                                flag = true;
                                break;
                            }
                        }
                        if (whiteRookList.get(i).getPosition().getCol() == 0) {
                            if (!board[row][col - 3].isEmpty()) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            this.getMovesList().add(new Position(row, col + direction * 2));
                        }
                    }
                }
            } else if (this.getColour().equals(Constants.BLACK)) {
                for (int i = 0; i < blackRookList.size(); ++i) {
                    boolean flag = false;
                    if (!((Rook) blackRookList.get(i)).isMoved()) {
                        int direction = (blackRookList.get(i).getPosition().getCol() - col) / Math.abs(col - blackRookList.get(i).getPosition().getCol());
                        for (int j = col + direction; j != col + direction * 3; j += direction) {
                            if (!board[row][j].isEmpty()) {
                                flag = true;
                                break;
                            }

                            if (ChessBoard.getInstance().isCheck(this.getPosition(), new Position(row, j), this.getColour())) {
                                flag = true;
                                break;
                            }
                        }
                        if (blackRookList.get(i).getPosition().getCol() == 0) {
                            if (!board[row][col - 3].isEmpty()) {
                                flag = true;
                            }
                        }

                        if (!flag) {
                            this.getMovesList().add(new Position(row, col + direction * 2));
                        }
                    }
                }
            }
        }

        // mutari specifice regelui
        for (int i = -1; i <= 1; ++i)
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0)
                    continue;
                rowAux = row + i;
                colAux = col + j;
                if (colAux >= 0 && colAux <= 7 && rowAux >= 0 && rowAux <= 7) {
                    Piece piece = board[rowAux][colAux].getPiece();
                    if (piece == null || !piece.getColour().equals(this.getColour())) {
                        //capturare + spatiu liber
                        if (!ChessBoard.getInstance().isCheck(this.getPosition(), new Position(rowAux, colAux), this.getColour())) {
                            this.getMovesList().add(new Position(rowAux, colAux));
                        }
                    }
                }
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
