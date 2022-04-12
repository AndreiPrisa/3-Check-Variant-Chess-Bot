import java.util.ArrayList;

/**
 * Clasa abstracta pentru reprezentarea pieselor de joc
 */
public abstract class Piece {
    private Position position;
    private String colour;
    private int type;
    private ArrayList<Position> movesList;
    private float value;
    private int attack;
    private int protect;

    public Piece(Position position, String colour, int type) {
        this.position = position;
        this.colour = colour;
        this.type = type;
        movesList = new ArrayList<>();
    }

    public float positionBasedValue() {
        if (colour.equals(Constants.WHITE)) {
            return Evaluation.getInstance().getEvalMatrix().get(0).get(type)[position.getRow()][position.getCol()];
        } else {
            return Evaluation.getInstance().getEvalMatrix().get(1).get(type)[position.getRow()][position.getCol()];
        }
    }

    public float calculateOverall() {
        return value + attack +protect;
    }

    //Metoda abstracta pentru generarea miscarilor posibile ale piesei respective
    abstract ArrayList<Position> possibleMoves();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return type == piece.type && position.equals(piece.position) && colour.equals(piece.colour);
    }

    //Gettere, settere + toString
    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getProtect() {
        return protect;
    }

    public void setProtect(int protect) {
        this.protect = protect;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public ArrayList<Position> getMovesList() {
        return movesList;
    }

    public void setMovesList(ArrayList<Position> movesList) {
        this.movesList = movesList;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String toString() {
        return "Type: " + type + "; Colour: " + colour + "; Position: [" + position.getRow() + ", " + position.getCol() + "]";
    }
}
