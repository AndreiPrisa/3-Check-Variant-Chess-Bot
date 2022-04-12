import java.util.ArrayList;

/**
 * Clasa pentru evaluarea pozitionarii fiecarei piese
 */
public class Evaluation {
    private static Evaluation obj = null;
    private Float[][] pawnEval = {
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
            {5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f},
            {1.0f, 1.0f, 2.0f, 3.0f, 3.0f, 2.0f, 1.0f, 1.0f},
            {0.5f, 0.5f, 1.0f, 2.5f, 2.5f, 1.0f, 0.5f, 0.5f},
            {0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f},
            {0.5f, -0.5f, -1.0f, 0.0f, 0.0f, -1.0f, -0.5f, 0.5f},
            {0.5f, 1.0f, 1.0f, -2.0f, -2.0f, 1.0f, 1.0f, 0.5f},
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
    };

    private Float[][] bishopEval = {
            {-2.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -2.0f},
            {-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f},
            {-1.0f, 0.0f, 0.5f, 1.0f, 1.0f, 0.5f, 0.0f, -1.0f},
            {-1.0f, 0.5f, 0.5f, 1.0f, 1.0f, 0.5f, 0.5f, -1.0f},
            {-1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, -1.0f},
            {-1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f},
            {-1.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, -1.0f},
            {-2.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -2.0f}
    };

    private Float[][] knightEval = {
            {-5.0f, -4.0f, -3.0f, -3.0f, -3.0f, -3.0f, -4.0f, -5.0f},
            {-4.0f, -2.0f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, -4.0f},
            {-3.0f, 0.0f, 1.0f, 1.5f, 1.5f, 1.0f, 0.0f, -3.0f},
            {-3.0f, 0.5f, 1.5f, 2.0f, 2.0f, 1.5f, 0.5f, -3.0f},
            {-3.0f, 0.0f, 1.5f, 2.0f, 2.0f, 1.5f, 0.0f, -3.0f},
            {-3.0f, 0.5f, 1.0f, 1.5f, 1.5f, 1.0f, 0.5f, -3.0f},
            {-4.0f, -2.0f, 0.0f, 0.5f, 0.5f, 0.0f, -2.0f, -4.0f},
            {-5.0f, -4.0f, -3.0f, -3.0f, -3.0f, -3.0f, -4.0f, -5.0f}
    };

    private Float[][] rookEval = {
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
            {0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.5f},
            {0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f}
    };

    private Float[][] kingEval = {
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-2.0f, -3.0f, -3.0f, -4.0f, -4.0f, -3.0f, -3.0f, -2.0f},
            {-1.0f, -2.0f, -2.0f, -2.0f, -2.0f, -2.0f, -2.0f, -1.0f},
            {2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 2.0f},
            {2.0f, 3.0f, 1.0f, 0.0f, 0.0f, 1.0f, 3.0f, 2.0f}
    };

    private Float[][] queenEval = {
            {-2.0f, -1.0f, -1.0f, -0.5f, -0.5f, -1.0f, -1.0f, -2.0f},
            {-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f},
            {-1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -1.0f},
            {-0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -0.5f},
            {0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -0.5f},
            {-1.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -1.0f},
            {-1.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f},
            {-2.0f, -1.0f, -1.0f, -0.5f, -0.5f, -1.0f, -1.0f, -2.0f}
    };

    private ArrayList<ArrayList<Float[][]>> evalMatrix;

    private Evaluation() {
        evalMatrix = new ArrayList<>();
        evalMatrix.add(new ArrayList<>());
        evalMatrix.add(new ArrayList<>());

        evalMatrix.get(0).add(null);
        evalMatrix.get(0).add(pawnEval);
        evalMatrix.get(0).add(knightEval);
        evalMatrix.get(0).add(bishopEval);
        evalMatrix.get(0).add(rookEval);
        evalMatrix.get(0).add(queenEval);
        evalMatrix.get(0).add(kingEval);

        evalMatrix.get(1).add(null);
        evalMatrix.get(1).add(reverseEval(pawnEval));
        evalMatrix.get(1).add(reverseEval(knightEval));
        evalMatrix.get(1).add(reverseEval(bishopEval));
        evalMatrix.get(1).add(reverseEval(rookEval));
        evalMatrix.get(1).add(reverseEval(queenEval));
        evalMatrix.get(1).add(reverseEval(kingEval));
    }
    public static Evaluation getInstance() {
        if (obj == null) {
            obj = new Evaluation();
        }
        return obj;
    }

    public Float[][] reverseEval(Float[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        Float[][] temp = new Float[rows][cols];

        for (int i = rows - 1; i >= 0; --i) {
            for (int j = cols - 1; j >= 0; --j) {
                temp[rows - i - 1][cols - j - 1] = matrix[i][j];
            }
        }

        return temp;
    }

    public ArrayList<ArrayList<Float[][]>> getEvalMatrix() {
        return evalMatrix;
    }

    public void setEvalMatrix(ArrayList<ArrayList<Float[][]>> evalMatrix) {
        this.evalMatrix = evalMatrix;
    }

    public Float[][] getPawnEval() {
        return pawnEval;
    }

    public void setPawnEval(Float[][] pawnEval) {
        this.pawnEval = pawnEval;
    }

    public Float[][] getBishopEval() {
        return bishopEval;
    }

    public void setBishopEval(Float[][] bishopEval) {
        this.bishopEval = bishopEval;
    }

    public Float[][] getKnightEval() {
        return knightEval;
    }

    public void setKnightEval(Float[][] knightEval) {
        this.knightEval = knightEval;
    }

    public Float[][] getRookEval() {
        return rookEval;
    }

    public void setRookEval(Float[][] rookEval) {
        this.rookEval = rookEval;
    }

    public Float[][] getKingEval() {
        return kingEval;
    }

    public void setKingEval(Float[][] kingEval) {
        this.kingEval = kingEval;
    }

    public Float[][] getQueenEval() {
        return queenEval;
    }

    public void setQueenEval(Float[][] queenEval) {
        this.queenEval = queenEval;
    }
}
