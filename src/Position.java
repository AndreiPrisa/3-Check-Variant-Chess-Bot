/**
 * Clasa pentru retinerea coordonatelor
 */
public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Transforma mutarea din formatul "e2e4" in coordonate
     */
    public static Position[] decode(String move) {
        Position[] result = new Position[2];
        result[0] = new Position((8 - (move.charAt(1) - '0')), (move.charAt(0) - 'a'));
        result[1] = new Position((8 - (move.charAt(3) - '0')), (move.charAt(2) - 'a'));

        // luam tipul piesei in care s-a promovat adversarul
        if (move.length() == 5) {
            switch (move.charAt(4)) {
                case 'n' :
                    ChessBoard.getInstance().setPromotionType(Constants.KNIGHT);
                    break;
                case 'b' :
                    ChessBoard.getInstance().setPromotionType(Constants.BISHOP);
                    break;
                case 'r' :
                    ChessBoard.getInstance().setPromotionType(Constants.ROOK);
                    break;
                case 'q' :
                    ChessBoard.getInstance().setPromotionType(Constants.QUEEN);
                    break;
                default : break;
            }
        }

        return result;
    }

    /**
     * Transpune coordonatele in formatul "e2e4"
     */
    public static String encode(Position[] move) {
        StringBuilder s = new StringBuilder();
        s.append((char) (move[0].col + 'a')).append((char) (8 - move[0].row + '0'));
        s.append((char) (move[1].col + 'a')).append((char) (8 - move[1].row + '0'));

        // trimitem tipul piesei in care a promovat bot-ul
        if (ChessBoard.getInstance().isFlag()) {
            int type = ChessBoard.getInstance().getPromotionType();
            switch (type) {
                case Constants.KNIGHT :
                    s.append("n");
                    break;
                case Constants.BISHOP :
                    s.append("b");
                    break;
                case Constants.ROOK :
                    s.append("r");
                    break;
                case Constants.QUEEN :
                    s.append("q");
                    break;
                default : break;
            }
        }

        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    //Gettere, settere + toString
    public String toString() {
        return "Position: [" + row + ", " + col + "]";
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
