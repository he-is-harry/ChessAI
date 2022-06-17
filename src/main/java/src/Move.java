/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author harryhe
 */
public class Move implements Comparable<Move> {

    int sRow, sCol, dRow, dCol;
    double value;
    Piece remPiece;
    Pair newCastLoc;
    Pair oldCastLoc;
    int restorePassIndex; // For restoring others passant (back to true)
    boolean restoreFirstMove;
    boolean restoreSelfPassant; // This only restores back to false
    int evolutionSource;

    boolean restorebKCast;
    boolean restorebQCast;
    boolean restorewKCast;
    boolean restorewQCast;

    // Careful with the number of variables you keep in this class
    // if there are too many variables the compiler will start to mess up
    // and feel that there are less variables than there really are
    public Move(int sRow, int sCol, int dRow, int dCol, double value, Piece remPiece, Pair oldCastLoc, Pair newCastLoc, int restorePassIndex,
            boolean restoreFirstMove, boolean restoreSelfPassant, int evolutionSource) {
        this.sRow = sRow;
        this.sCol = sCol;
        this.dRow = dRow;
        this.dCol = dCol;
        this.value = value;
        this.remPiece = remPiece;
        this.oldCastLoc = oldCastLoc;
        this.newCastLoc = newCastLoc;
        this.restorePassIndex = restorePassIndex;
        this.restoreFirstMove = restoreFirstMove;
        this.restoreSelfPassant = restoreSelfPassant;
        this.evolutionSource = evolutionSource;

        restorebKCast = false;
        restorebQCast = false;
        restorewKCast = false;
        restorewQCast = false;
    }
    
    public void setRestoreCast(boolean bK, boolean bQ, boolean wK, boolean wQ){
        restorebKCast = bK;
        restorebQCast = bQ;
        restorewKCast = wK;
        restorewQCast = wQ;
    }

    public int compareTo(Move o) {
        return -Double.compare(value, o.value);
    }
}
