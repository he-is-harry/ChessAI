/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author harryhe
 */
public class TranspositionTable {
    
    Board board;
    
    public final double lookupFailed = Double.MIN_VALUE;
    public final int Exact = 0;
    public final int lowerBound = 1;
    public final int upperBound = 2;
    
    public Map<Long, Entry> entries;
    
    public boolean enabled = true;
    
    private double wMateScore;
    private double bMateScore;
    
    public TranspositionTable(Board board, double wMate, double bMate){
        this.board = board;
        
        entries = new HashMap<>();
        
        this.wMateScore = wMate;
        this.bMateScore = bMate;
    }
    
    public void clear(){
        entries.clear();
    }
    
    public double LookupEvaluation(int depth, int plyFromRoot, double alpha, 
            double beta){
        long Index = board.ZobristKey;
        if(!enabled || !entries.containsKey(Index)){
            return lookupFailed;
        }
        Entry entry = entries.get(Index);
        
        if(entry.key == board.ZobristKey){
            if(entry.depth >= depth){
                double correctedScore = CorrectRetrievedMateScore(entry.value, plyFromRoot);
                if(entry.nodeType == Exact){
                    return correctedScore;
                }
                if(entry.nodeType == upperBound && correctedScore <= alpha){
                    return correctedScore;
                }
                if(entry.nodeType == lowerBound && correctedScore >= beta){
                    return correctedScore;
                }
            }
        }
        return lookupFailed;
    }
    
    public void StoreEvaluation(double eval, int depth, int plyFromRoot, 
            int evalType){
        if(!enabled){
            return;
        }
        Entry entry = new Entry(board.ZobristKey, 
                CorrectMateScoreForStorage(eval, plyFromRoot), 
                depth, evalType);
        entries.put(board.ZobristKey, entry);
    }
    
    private double CorrectMateScoreForStorage(double score, int plyFromRoot){
        if(isMateScore(score)){
            int sign = 1;
            if(score < 0){
                sign = -1;
            }
            return (score * sign + plyFromRoot) * sign;
        }
        return score;
    }
    
    private double CorrectRetrievedMateScore(double score, int plyFromRoot){
        if(isMateScore(score)){
            int sign = 1;
            if(score < 0){
                sign = -1;
            }
            return (score * sign - plyFromRoot) * sign;
        }
        return score;
    }
    
    private boolean isMateScore(double score){
        if(score == wMateScore || score == bMateScore){
            return true;
        }
        return false;
    }
}
