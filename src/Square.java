/**
 * Clasa pentru mentinerea legaturii piesa - casuta
 */
public class Square {
    private Piece piece;

    public Square() {
        piece = null;
    }

    public Square(Piece piece) {
        this.piece = piece;
    }

    public boolean isEmpty() {
        return this.piece == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return square.piece.equals(piece);
    }

    //Gettere, settere
    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
