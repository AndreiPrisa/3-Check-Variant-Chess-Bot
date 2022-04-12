/**
 * Clasa pentru declararea constantelor
 */
public class Constants {
    static final int PAWN =  1;
    static final int KNIGHT = 2;
    static final int BISHOP = 3;
    static final int ROOK = 4;
    static final int QUEEN = 5;
    static final int KING = 6;
    static final String BLACK = "black";
    static final String WHITE = "white";
    static final int PRESETBLACKPAWN = 4;
    static final int PRESETWHITEPAWN = 5;
    static final int[] forPromotion = new int[]{Constants.KNIGHT, Constants.BISHOP, Constants.ROOK, Constants.QUEEN};
    static final int DRAW = 1;
    static final int WIN = 2;
    static final int DEPTH = 4;
    static final int PAWN_VALUE = 10;
    static final int BISHOP_VALUE = 30;
    static final int KNIGHT_VALUE = 30;
    static final int ROOK_VALUE = 50;
    static final int QUEEN_VALUE = 90;
    static final int KING_VALUE = 0;
    static final float MAX_SCORE = 999999;
    static final float MIN_SCORE = -999999;
    static final int[] protection = new int[]{-1, 0, 10, 10, 0, 0, 0};
    static final int[] attacking = new int[]{-1, 0, 10, 10, 20, 50, 40};
}
