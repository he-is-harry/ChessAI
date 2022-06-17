/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.Comparator;

/**
 *
 * @author harryhe
 */
public class MoveOrderer implements Comparator<Quad>{
    private Board board;
    private double pawnValue;
    private double horseValue;
    private double bishopValue;
    private double rookValue;
    private double queenValue;
    
    // Personal doesn't affect opponent decision
    // Used to make captures better than non-captures
    private double capturedPieceMultiplier;
    private double controlledPawnPenalty;
    
    public MoveOrderer(Board board, double [] values){
        this.board = board;
        pawnValue = values[0];
        horseValue = values[1];
        bishopValue = values[2];
        rookValue = values[3];
        queenValue = values[4];
        
        capturedPieceMultiplier = 10;
        controlledPawnPenalty = 35;
    }
    
    public int compare(Quad o1, Quad o2) {
        double a = getMoveScoreGuess(o1);
        double b = getMoveScoreGuess(o2);
        // Best to worst for white
        int multi = -1;
        // Worst to best for black
        if(!board.whiteTurn){
            multi = 1;
        }
        return multi * Double.compare(a, b);
    }
    
    public double getMoveScoreGuess(Quad q){
        double moveScoreGuess = 0;
        int pieceType = board.chessBoard[q.sRow][q.sCol];
        
        if(board.chessBoard[q.dRow][q.dCol] > 0){
            // We should take the negative of the piece value
            // as the perspective that is taking it things that it
            // is good if the piece is lost
            
            // We still subtract the piece taking's value because
            // that piece is better if it remains on the board
            moveScoreGuess += capturedPieceMultiplier * 
                    -getPieceValue(board.chessBoard[q.dRow][q.dCol]) 
                    - getPieceValue(pieceType);
        }
        
        if(pieceType == 1 && q.dRow == 0){
            // Most likely queen, so just make it as good as queen
            moveScoreGuess += queenValue;
        } else if(pieceType == 2 && q.dRow == 7){
            // Most likely queen, so just make it as good as queen
            // We add here because the queen value is negative for
            // black
            moveScoreGuess += queenValue;
        }
        // The moves are considered negative if black is winning
        // positive if white is winning
        if(pieceType % 2 == 1){
            if(q.dCol > 0 && board.chessBoard[q.dRow][q.dCol - 1] == 2){
                moveScoreGuess -= controlledPawnPenalty;
            } else if(q.dCol < board.chessBoard.length - 1 && 
                    board.chessBoard[q.dRow][q.dCol + 1] == 2){
                moveScoreGuess -= controlledPawnPenalty;
            }
        } else {
            if(q.dCol > 0 && board.chessBoard[q.dRow][q.dCol - 1] == 1){
                moveScoreGuess += controlledPawnPenalty;
            } else if(q.dCol < board.chessBoard.length - 1 && 
                    board.chessBoard[q.dRow][q.dCol + 1] == 1){
                moveScoreGuess += controlledPawnPenalty;
            }
        }
        
        return moveScoreGuess;
    }
    
    private double getPieceValue(int pieceType) {
        switch (pieceType) {
            case 1:
                return pawnValue;
            case 2:
                return -pawnValue;
            case 3:
                return bishopValue;
            case 4:
                return -bishopValue;
            case 5:
                return horseValue;
            case 6:
                return -horseValue;
            case 7:
                return rookValue;
            case 8:
                return -rookValue;
            case 9:
                return queenValue;
            case 10:
                return -queenValue;
            default:
                break;
        }

        return 0;
    }
}
