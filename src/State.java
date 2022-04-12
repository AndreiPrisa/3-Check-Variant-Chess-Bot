import java.util.ArrayList;

/**
 * Clasa pentru retinerea starii jocului
 */
public class State {
    private ArrayList<ArrayList<Piece>> whiteList;
    private ArrayList<ArrayList<Piece>> blackList;
    private boolean whiteCastlingSmall;
    private boolean whiteCastlingBig;
    private boolean blackCastlingSmall;
    private boolean blackCastlingBig;
    private int appearances;
    private int dummyMoves;
    private int whiteChecked;
    private int blackChecked;
    private String currentPlayer;
    private Piece whitePawnEP;
    private Piece blackPawnEP;
    private boolean flag;
    private int promotionType;
    private boolean whiteEPflag;
    private boolean blackEPflag;
    private boolean whiteCastle;
    private boolean blackCastle;

    public State() {
        whiteList = new ArrayList<>();
        blackList = new ArrayList<>();
        whiteCastlingSmall = false;
        whiteCastlingBig = false;
        blackCastlingSmall = false;
        blackCastlingBig = false;
        appearances = 1;
        dummyMoves = 0;
        whiteChecked = 0;
        blackChecked = 0;
        whitePawnEP = null;
        blackPawnEP = null;
        flag = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return whiteList.equals(state.whiteList) && blackList.equals(state.blackList)
                && whiteCastlingBig == state.whiteCastlingBig && whiteCastlingSmall == state.whiteCastlingSmall
                && blackCastlingBig == state.blackCastlingBig && blackCastlingSmall == state.blackCastlingSmall
                && blackEPflag == state.blackEPflag && whiteEPflag == state.whiteEPflag;
    }

    // Gettere, settere
    public String toString() {
        return currentPlayer + "\n" + blackList + " \n LISTA WHITE: " + whiteList;
    }

    public boolean isWhiteEPflag() {
        return whiteEPflag;
    }

    public void setWhiteEPflag(boolean whiteEPflag) {
        this.whiteEPflag = whiteEPflag;
    }

    public boolean isBlackEPflag() {
        return blackEPflag;
    }

    public void setBlackEPflag(boolean blackEPflag) {
        this.blackEPflag = blackEPflag;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Piece getWhitePawnEP() {
        return whitePawnEP;
    }

    public void setWhitePawnEP(Piece whitePawnEP) {
        this.whitePawnEP = whitePawnEP;
    }

    public Piece getBlackPawnEP() {
        return blackPawnEP;
    }

    public void setBlackPawnEP(Piece blackPawnEP) {
        this.blackPawnEP = blackPawnEP;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public int getDummyMoves() {
        return dummyMoves;
    }

    public void setDummyMoves(int dummyMoves) {
        this.dummyMoves = dummyMoves;
    }

    public int getWhiteChecked() {
        return whiteChecked;
    }

    public void setWhiteChecked(int whiteChecked) {
        this.whiteChecked = whiteChecked;
    }

    public int getBlackChecked() {
        return blackChecked;
    }

    public void setBlackChecked(int blackChecked) {
        this.blackChecked = blackChecked;
    }

    public int getAppearances() {
        return appearances;
    }

    public void setAppearances(int appearances) {
        this.appearances = appearances;
    }

    public ArrayList<ArrayList<Piece>> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(ArrayList<ArrayList<Piece>> whiteList) {
        this.whiteList = whiteList;
    }

    public ArrayList<ArrayList<Piece>> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<ArrayList<Piece>> blackList) {
        this.blackList = blackList;
    }

    public boolean isWhiteCastle() {
        return whiteCastle;
    }

    public void setWhiteCastle(boolean whiteCastle) {
        this.whiteCastle = whiteCastle;
    }

    public boolean isBlackCastle() {
        return blackCastle;
    }

    public void setBlackCastle(boolean blackCastle) {
        this.blackCastle = blackCastle;
    }

    public boolean isWhiteCastlingSmall() {
        return whiteCastlingSmall;
    }

    public void setWhiteCastlingSmall(boolean whiteCastlingSmall) {
        this.whiteCastlingSmall = whiteCastlingSmall;
    }

    public boolean isWhiteCastlingBig() {
        return whiteCastlingBig;
    }

    public void setWhiteCastlingBig(boolean whiteCastlingBig) {
        this.whiteCastlingBig = whiteCastlingBig;
    }

    public boolean isBlackCastlingSmall() {
        return blackCastlingSmall;
    }

    public void setBlackCastlingSmall(boolean blackCastlingSmall) {
        this.blackCastlingSmall = blackCastlingSmall;
    }

    public boolean isBlackCastlingBig() {
        return blackCastlingBig;
    }

    public void setBlackCastlingBig(boolean blackCastlingBig) {
        this.blackCastlingBig = blackCastlingBig;
    }
}
