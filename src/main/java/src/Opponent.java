/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author harryhe
 */
public class Opponent implements Runnable {

    private Thread thread;

    private Game game;
    private Handler handler;
    private Board board;

    public int searchDepth;

    private boolean whiteSide;
    private int personality;
    private boolean firstOpponent;
    // Makes sure second will not play unless it is CvC

    // 0 is agressive
    // 1 is defensive
    // 2 is positional
    // 3 is balanced
    // Evaluation Variables
    double pawnValue;
    double horseValue;
    double bishopValue;
    double rookValue;
    double queenValue;
    double kingValue;

    double cornerEndgameValue;
    double safetyEarlyMidValue;
    double castleAvaliabilityValue;

    double supportPawnValue;
    double supportHorseValue;
    double supportBishopValue;
    double supportRookValue;
    double supportQueenValue;
    double supportKingValue;

    double threatValue;
    double kingThreatValue;

    double[][] controlValue;

    double[][] pawnSquareTable = {{0, 0, 0, 0, 0, 0, 0, 0}, {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5}, {0.1, 0.1, 0.2, 0.3, 0.3, 0.2, 0.1, 0.1}, {0.05, 0.05, 0.1, 0.25, 0.25, 0.1, 0.05, 0.05}, {0, 0, 0, 0.2, 0.2, 0, 0, 0}, {0.05, -0.05, -0.1, 0, 0, -0.1, -0.05, 0.05}, {0.05, 0.1, 0.1, -0.2, -0.2, 0.1, 0.1, 0.05}, {0, 0, 0, 0, 0, 0, 0, 0}};
    double[][] knightSquareTable = {{-0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5}, {-0.4, -0.2, 0, 0, 0, 0, -0.2, -0.4}, {-0.3, 0, 0.1, 0.15, 0.15, 0.1, 0, -0.3}, {-0.3, 0.05, 0.15, 0.2, 0.2, 0.15, 0.05, -0.3}, {-0.3, 0, 0.15, 0.2, 0.2, 0.15, 0, -0.3}, {-0.3, 0.05, 0.1, 0.15, 0.15, 0.1, 0.05, -0.3}, {-0.4, -0.2, 0, 0.05, 0.05, 0, -0.2, -0.4}, {-0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5}};
    double[][] bishopSquareTable = {{-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2}, {-0.1, 0, 0, 0, 0, 0, 0, -0.1}, {-0.1, 0, 0.05, 0.1, 0.1, 0.05, 0, -0.1}, {-0.1, 0.05, 0.05, 0.1, 0.1, 0.05, 0.05, -0.1}, {-0.1, 0, 0.1, 0.1, 0.1, 0.1, 0, -0.1}, {-0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, -0.1}, {-0.1, 0.05, 0, 0, 0, 0, 0.05, -0.1}, {-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2}};
    double[][] rookSquareTable = {{0, 0, 0, 0, 0, 0, 0, 0}, {0.05, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.05}, {-0.05, 0, 0, 0, 0, 0, 0, -0.05}, {-0.05, 0, 0, 0, 0, 0, 0, -0.05}, {-0.05, 0, 0, 0, 0, 0, 0, -0.05}, {-0.05, 0, 0, 0, 0, 0, 0, -0.05}, {-0.05, 0, 0, 0, 0, 0, 0, -0.05}, {0, 0, 0, 0.05, 0.05, 0, 0, 0}};
    double[][] queenSquareTable = {{-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2}, {-0.1, 0, 0, 0, 0, 0, 0, -0.1}, {-0.1, 0, 0.05, 0.05, 0.05, 0.05, 0, -0.1}, {-0.05, 0, 0.05, 0.05, 0.05, 0.05, 0, -0.05}, {0, 0, 0.05, 0.05, 0.05, 0.05, 0, -0.05}, {-0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0, -0.1}, {-0.1, 0, 0.05, 0, 0, 0, 0, -0.1}, {-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2}};
    double[][] kingSquareTable = {{-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3}, {-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3}, {-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3}, {-0.3, -0.4, -0.4, -0.5, -0.5, -0.4, -0.4, -0.3}, {-0.2, -0.3, -0.3, -0.4, -0.4, -0.3, -0.3, -0.2}, {-0.1, -0.2, -0.2, -0.2, -0.2, -0.2, -0.2, -0.1}, {0.2, 0.2, 0, 0, 0, 0, 0.2, 0.2}, {0.2, 0.3, 0.1, 0, 0, 0.1, 0.3, 0.2}};

    private int search;

    private final double defaultMax = 1e12;
    private final double defaultMin = -1e12;

    private MoveOrderer moveSorter;
    private TranspositionTable tt;

    public Opponent(Game game, Handler handler, Board board,
            boolean whiteSide, int personality, boolean firstOpponent) {
        this.game = game;
        this.handler = handler;
        this.board = board;

        this.searchDepth = 2;

        this.whiteSide = whiteSide;
        this.personality = personality;
        this.firstOpponent = firstOpponent;

        this.pawnValue = 10;
        this.horseValue = 30;
        this.bishopValue = 30;
        this.rookValue = 50;
        this.queenValue = 90;
        this.kingValue = 900;
        this.cornerEndgameValue = 5;
        this.safetyEarlyMidValue = 5;
        this.castleAvaliabilityValue = 15;

        // Maybe multiply everything by 1.5 later to 
        // make it more balanced
        this.supportPawnValue = 1.5;
        this.supportHorseValue = 0.5;
        this.supportBishopValue = 0.75;
        this.supportRookValue = 0.5;
        this.supportQueenValue = 0.4;
        this.supportKingValue = 0;

        this.threatValue = 0.1; // Multiplicative
        this.kingThreatValue = 1; // Additive
        // Threating the king will not be worth as much,
        // probably only worth 1

        double[][] temp = {{0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 0},
        {0, 1.5, 3, 3, 3, 3, 1.5, 0},
        {0, 1.5, 3, 5, 5, 3, 1.5, 0},
        {0, 1.5, 3, 5, 5, 3, 1.5, 0},
        {0, 1.5, 3, 3, 3, 3, 1.5, 0},
        {0, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 0},
        {0, 0, 0, 0, 0, 0, 0, 0}};
        this.controlValue = temp;

        double[] values = new double[5];
        values[0] = pawnValue;
        values[1] = horseValue;
        values[2] = bishopValue;
        values[3] = rookValue;
        values[4] = queenValue;
        this.moveSorter = new MoveOrderer(board, values);
        this.tt = new TranspositionTable(board, defaultMax, defaultMin);
    }

    @Override
    public void run() {
        // If you wish you can place a game loop here
        // however, a little bit of delay is find
        while (game.running) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tick();
        }
        stop();
    }

    public void tick() {
        // Put all of your move making here
//        System.out.println(board.playerType + " " + board.whiteTurn + " " + 
//                whiteSide);
        if (firstOpponent && board.playerType > 0 && board.whiteTurn == whiteSide
                && !board.selectEvo && !board.pendingAction) {
            // Now check if the moves have been rendered yet
            if (whiteSide && board.whiteValidMoves.size() > 0) {
                // It is the reverse of maximizing player
                // because here we already did the first depth min max
                // This is false now because we are looking for the best
                // for each perspective
                this.search = 0;
                Move bestMove = SearchHelper(this.searchDepth, whiteSide, whiteSide);
                System.out.println(this.search);

                int bestIndex = 0;
                Piece p = handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(bestMove.sRow, bestMove.sCol)));
                board.properMove(p, bestMove.dRow, bestMove.dCol,
                        bestMove.evolutionSource, false);

            } else if (!whiteSide && board.blackValidMoves.size() > 0) {
                this.search = 0;
                Move bestMove = SearchHelper(this.searchDepth, whiteSide, whiteSide);
                System.out.println(this.search);

                int bestIndex = 0;
                Piece p = handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(bestMove.sRow, bestMove.sCol)));
                board.properMove(p, bestMove.dRow, bestMove.dCol,
                        bestMove.evolutionSource, false);
            }
//            for(int i = 0; i < handler.pieces.size(); i++){
//                Piece p = handler.pieces.get(i);
//                System.out.println(p.type + " (" + p.destX + " " + 
//                        p.destY + ")");
//            }
        }

        // Same as above but for second opponent
        if (!firstOpponent && board.playerType == 2
                && board.whiteTurn == whiteSide && !board.selectEvo
                && !board.pendingAction) {
            // Now check if the moves have been rendered yet
            if (whiteSide && board.whiteValidMoves.size() > 0) {
                this.search = 0;
                Move bestMove = SearchHelper(this.searchDepth, whiteSide, whiteSide);
                System.out.println(this.search);

                int bestIndex = 0;
                Piece p = handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(bestMove.sRow, bestMove.sCol)));
                board.properMove(p, bestMove.dRow, bestMove.dCol,
                        bestMove.evolutionSource, false);

            } else if (!whiteSide && board.blackValidMoves.size() > 0) {
                this.search = 0;
                Move bestMove = SearchHelper(this.searchDepth, whiteSide, whiteSide);
                System.out.println(this.search);

                int bestIndex = 0;
                Piece p = handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(bestMove.sRow, bestMove.sCol)));
                board.properMove(p, bestMove.dRow, bestMove.dCol,
                        bestMove.evolutionSource, false);
            }
        }
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWhiteSide(boolean whiteSide) {
        this.whiteSide = whiteSide;
    }

    public void setPersonality(int personality) {
        this.personality = personality;
    }

    private Move SearchHelper(int depth, boolean whitePersp,
            boolean isMaximizingPlayer) {
        tt.clear();

        ArrayList<Quad> possibleMoves = new ArrayList<>();
        if (whitePersp) {
            possibleMoves = board.whiteValidMoves;
        } else {
            possibleMoves = board.blackValidMoves;
        }
        Collections.sort(possibleMoves, moveSorter);

        double bestMoveValue = 0;
        if (isMaximizingPlayer) {
            bestMoveValue = defaultMin - 1;
        } else {
            bestMoveValue = defaultMax + 1;
        }
        Move bestMove = new Move(-1, -1, -1, -1,
                bestMoveValue, new Piece(-1, -1, 1, board, handler, -1),
                new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                -1);

        boolean exit = false;
        for (int i = 0; i < possibleMoves.size() && !exit; i++) {
            Quad move = possibleMoves.get(i);
            if (!handler.indexFinder.containsKey(
                    handler.generateKey(move.sRow, move.sCol))) {
                System.out.print("Error occured here: ");
                System.out.println(handler.generateKey(move.sRow, move.sCol));
                for (String key : handler.indexFinder.keySet()) {
                    System.out.println(key + " : " + handler.indexFinder.get(key));
                }
                continue;
            }
            Piece p = handler.pieces.get(handler.getPieceIndex(
                    handler.generateKey(move.sRow, move.sCol)));
            int[] evoPossible = {-1};
            if ((p.type == 1 && move.dRow == 0)
                    || (p.type == 2 && move.dRow == 7)) {
                if (p.type == 1) {
                    // White
                    evoPossible = new int[4];
                    evoPossible[0] = 3;
                    evoPossible[1] = 5;
                    evoPossible[2] = 7;
                    evoPossible[3] = 9;
                } else {
                    // Black
                    evoPossible = new int[4];
                    evoPossible[0] = 4;
                    evoPossible[1] = 6;
                    evoPossible[2] = 8;
                    evoPossible[3] = 10;
                }
            }
            for (int j = 0; j < evoPossible.length; j++) {
                this.search++;
                board.properMove(p, move.dRow, move.dCol, evoPossible[j], true);// Recursive case
                double moveScore = SearchScore(depth - 1, 1, !whitePersp,
                        !isMaximizingPlayer, defaultMin, defaultMax);

                if (isMaximizingPlayer) {
                    if (moveScore > bestMoveValue) {
                        bestMoveValue = moveScore;
                        bestMove = new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                                bestMoveValue, new Piece(-1, -1, 1, board, handler, -1),
                                new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                                evoPossible[j]);
                    }
                } else {
                    if (moveScore < bestMoveValue) {
                        bestMoveValue = moveScore;
                        bestMove = new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                                bestMoveValue, new Piece(-1, -1, 1, board, handler, -1),
                                new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                                evoPossible[j]);
                    }
                }
                board.undoProper(true);
            }
        }
        return bestMove;
    }

    private double SearchScore(int depth, int plyFromRoot, boolean whitePersp,
            boolean isMaximizingPlayer, double alpha, double beta) {
        if (depth <= 0) {
            //double moveScore = evaluateBoard(whitePersp);
            double moveScore = QuiesenceSearch(plyFromRoot + 1, whitePersp,
                    isMaximizingPlayer, alpha, beta);
            return moveScore;
        }

        double ttVal = tt.LookupEvaluation(depth, plyFromRoot, alpha, beta);
        if (ttVal != tt.lookupFailed) {
            return ttVal;
        }

        double bestMoveValue = 0;
        if (isMaximizingPlayer) {
            bestMoveValue = this.defaultMin;
        } else {
            bestMoveValue = this.defaultMax;
        }

        ArrayList<Quad> possibleMoves = new ArrayList<>();
        if (whitePersp) {
            possibleMoves = board.whiteValidMoves;
        } else {
            possibleMoves = board.blackValidMoves;
        }
        Collections.sort(possibleMoves, moveSorter);

        boolean exit = false;
        int evalType = tt.upperBound;
        for (int i = 0; i < possibleMoves.size() && !exit; i++) {
            Quad move = possibleMoves.get(i);
            if (!handler.indexFinder.containsKey(
                    handler.generateKey(move.sRow, move.sCol))) {
                System.out.print("Error occured here: ");
                System.out.println(handler.generateKey(move.sRow, move.sCol));
                for (String key : handler.indexFinder.keySet()) {
                    System.out.println(key + " : " + handler.indexFinder.get(key));
                }
                continue;
            }
            Piece p = handler.pieces.get(handler.getPieceIndex(
                    handler.generateKey(move.sRow, move.sCol)));
            int[] evoPossible = {-1};
            if ((p.type == 1 && move.dRow == 0)
                    || (p.type == 2 && move.dRow == 7)) {
                if (p.type == 1) {
                    // White
                    evoPossible = new int[4];
                    evoPossible[0] = 3;
                    evoPossible[1] = 5;
                    evoPossible[2] = 7;
                    evoPossible[3] = 9;
                } else {
                    // Black
                    evoPossible = new int[4];
                    evoPossible[0] = 4;
                    evoPossible[1] = 6;
                    evoPossible[2] = 8;
                    evoPossible[3] = 10;
                }
            }

            for (int j = 0; j < evoPossible.length; j++) {
                this.search++;
                board.properMove(p, move.dRow, move.dCol, evoPossible[j], true);// Recursive case
                double moveScore = SearchScore(depth - 1, plyFromRoot + 1, !whitePersp,
                        !isMaximizingPlayer, alpha, beta);

                if (board.whiteValidMoves.size() == 0
                        && board.blackValidMoves.size() == 0) {
                    moveScore = 0;
                } else if (board.whiteValidMoves.size() == 0) {
                    moveScore = defaultMin + plyFromRoot;
                } else if (board.blackValidMoves.size() == 0) {
                    moveScore = defaultMax - plyFromRoot;
                }
                board.undoProper(true);

                if (isMaximizingPlayer) {
                    if (moveScore > bestMoveValue) {
                        evalType = tt.Exact;
                        bestMoveValue = moveScore;
                    }
                    alpha = Math.max(alpha, moveScore);
                } else {
                    if (moveScore < bestMoveValue) {
                        evalType = tt.Exact;
                        bestMoveValue = moveScore;
                    }
                    beta = Math.min(beta, moveScore);
                }

                // Perhaps try another where you return beta
                // or alpha
                if (beta <= alpha) {
                    tt.StoreEvaluation(moveScore, depth, plyFromRoot, tt.lowerBound);
                    exit = true;
                    break;
                }
            }
        }
        tt.StoreEvaluation(bestMoveValue, depth, plyFromRoot, evalType);
        return bestMoveValue;
    }

    private double QuiesenceSearch(int plyFromRoot, boolean whitePersp,
            boolean isMaximizingPlayer, double alpha, double beta) {
        double moveScore = evaluateBoard(whitePersp, plyFromRoot);
        this.search++;

        double bestMoveValue = 0;
        if (isMaximizingPlayer) {
            bestMoveValue = this.defaultMin;
        } else {
            bestMoveValue = this.defaultMax;
        }

        if (isMaximizingPlayer) {
            if (moveScore > bestMoveValue) {
                bestMoveValue = moveScore;
            }
            alpha = Math.max(alpha, moveScore);
        } else {
            if (moveScore < bestMoveValue) {
                bestMoveValue = moveScore;
            }
            beta = Math.min(beta, moveScore);
        }

        // Perhaps try another where you return beta
        // or alpha
        if (beta <= alpha) {
            return bestMoveValue;
        }

        ArrayList<Quad> unfilteredMoves = new ArrayList<>();
        if (whitePersp) {
            unfilteredMoves = board.whiteValidMoves;
        } else {
            unfilteredMoves = board.blackValidMoves;
        }
        // Capture moves are will be recognized
        // until there are no more remaining
        ArrayList<Quad> possibleMoves = new ArrayList<>();
        for (int i = 0; i < unfilteredMoves.size(); i++) {
            Quad move = unfilteredMoves.get(i);
            if (board.chessBoard[move.dRow][move.dCol] > 0) {
                possibleMoves.add(move);
            }
        }
        Collections.sort(possibleMoves, moveSorter);

        boolean exit = false;
        for (int i = 0; i < possibleMoves.size() && !exit; i++) {
            Quad move = possibleMoves.get(i);
            if (!handler.indexFinder.containsKey(
                    handler.generateKey(move.sRow, move.sCol))) {
                System.out.print("Error occured here: ");
                System.out.println(handler.generateKey(move.sRow, move.sCol));
                for (String key : handler.indexFinder.keySet()) {
                    System.out.println(key + " : " + handler.indexFinder.get(key));
                }
                continue;
            }
            Piece p = handler.pieces.get(handler.getPieceIndex(
                    handler.generateKey(move.sRow, move.sCol)));
            int[] evoPossible = {-1};
            if ((p.type == 1 && move.dRow == 0)
                    || (p.type == 2 && move.dRow == 7)) {
                if (p.type == 1) {
                    // White
                    evoPossible = new int[4];
                    evoPossible[0] = 3;
                    evoPossible[1] = 5;
                    evoPossible[2] = 7;
                    evoPossible[3] = 9;
                } else {
                    // Black
                    evoPossible = new int[4];
                    evoPossible[0] = 4;
                    evoPossible[1] = 6;
                    evoPossible[2] = 8;
                    evoPossible[3] = 10;
                }
            }
            for (int j = 0; j < evoPossible.length; j++) {
                this.search++;
                board.properMove(p, move.dRow, move.dCol, evoPossible[j], true);// Recursive case
                moveScore = QuiesenceSearch(plyFromRoot + 1, !whitePersp,
                        !isMaximizingPlayer, alpha, beta);

                if (board.whiteValidMoves.size() == 0
                        && board.blackValidMoves.size() == 0) {
                    moveScore = 0;
                } else if (board.whiteValidMoves.size() == 0) {
                    moveScore = defaultMin + plyFromRoot;
                } else if (board.blackValidMoves.size() == 0) {
                    moveScore = defaultMax - plyFromRoot;
                }
                board.undoProper(true);

                if (isMaximizingPlayer) {
                    if (moveScore > bestMoveValue) {
                        bestMoveValue = moveScore;
                    }
                    alpha = Math.max(alpha, moveScore);
                } else {
                    if (moveScore < bestMoveValue) {
                        bestMoveValue = moveScore;
                    }
                    beta = Math.min(beta, moveScore);
                }

                // Perhaps try another where you return beta
                // or alpha
                if (beta <= alpha) {
                    exit = true;
                    break;
                }
            }
        }
        return bestMoveValue;
    }

    private ArrayList<Move> getBestMovesAB(int depth, boolean whitePersp,
            boolean isMaximizingPlayer, double alpha, double beta) {

        ArrayList<Move> bestMoves = new ArrayList<>();
        ArrayList<Quad> possibleMoves = new ArrayList<>();
        if (whitePersp) {
            possibleMoves = board.whiteValidMoves;
        } else {
            possibleMoves = board.blackValidMoves;
        }
        Collections.shuffle(possibleMoves);

        boolean exit = false;
        for (int i = 0; i < possibleMoves.size() && !exit; i++) {
            Quad move = possibleMoves.get(i);
            if (!handler.indexFinder.containsKey(
                    handler.generateKey(move.sRow, move.sCol))) {
                System.out.print("Error occured here: ");
                System.out.println(handler.generateKey(move.sRow, move.sCol));
                for (String key : handler.indexFinder.keySet()) {
                    System.out.println(key + " : " + handler.indexFinder.get(key));
                }
                continue;
            }
            Piece p = handler.pieces.get(handler.getPieceIndex(
                    handler.generateKey(move.sRow, move.sCol)));
            int[] evoPossible = {-1};
            if ((p.type == 1 && move.dRow == 0)
                    || (p.type == 2 && move.dRow == 7)) {
                if (p.type == 1) {
                    // White
                    evoPossible = new int[4];
                    evoPossible[0] = 3;
                    evoPossible[1] = 5;
                    evoPossible[2] = 7;
                    evoPossible[3] = 9;
                } else {
                    // Black
                    evoPossible = new int[4];
                    evoPossible[0] = 4;
                    evoPossible[1] = 6;
                    evoPossible[2] = 8;
                    evoPossible[3] = 10;
                }
            }
            for (int j = 0; j < evoPossible.length; j++) {
                this.search++;
                board.properMove(p, move.dRow, move.dCol, evoPossible[j], true);

                if (depth <= 1) {
                    // Base case
                    double moveScore = 0;
                    moveScore = evaluateBoardOldTrue(whitePersp);
                    if (isMaximizingPlayer) {
                        alpha = Math.max(alpha, moveScore);
                    } else {
                        beta = Math.min(beta, moveScore);
                    }

                    bestMoves.add(new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                            moveScore, new Piece(-1, -1, 1, board, handler, -1),
                            new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                            evoPossible[j]));
                    board.undoProper(true);

                    //System.out.println(alpha + " " + beta);
//                    if (this.whiteSide) {
//                        if (isMaximizingPlayer) {
//                            if (beta <= alpha) {
//                                // The move is too good so we should not
//                                // enter this position
//                                exit = true;
//                                break;
//                            }
//                        } else {
//                            if (beta >= alpha) {
//                                // The move is too good so we should not
//                                // enter this position
//                                exit = true;
//                                break;
//                            }
//                        }
//                    } else {
//                        if (isMaximizingPlayer) {
//                            if (beta >= alpha) {
//                                // The move is too good so we should not
//                                // enter this position
//                                exit = true;
//                                break;
//                            }
//                        } else {
//                            if (beta <= alpha) {
//                                // The move is too good so we should not
//                                // enter this position
//                                exit = true;
//                                break;
//                            }
//                        }
//                    }
//                    if (isMaximizingPlayer) {
//                        if (beta >= alpha) {
//                            // The move is too good so we should not
//                            // enter this position
//                            exit = true;
//                            break;
//                        }
//                    } else {
//                        if (beta <= alpha) {
//                            // The move is too good so we should not
//                            // enter this position
//                            exit = true;
//                            break;
//                        }
//                    }
                } else {
                    // Recursive case
                    ArrayList<Move> results = getBestMovesAB(depth - 1, !whitePersp,
                            !isMaximizingPlayer, alpha, beta);

                    Collections.sort(results);
                    double moveScore = 0;
                    if (isMaximizingPlayer) {
                        // First value is max
                        if (board.whiteValidMoves.size() == 0
                                && board.blackValidMoves.size() == 0) {
                            moveScore = 0;
                        } else if (board.whiteValidMoves.size() == 0) {
                            moveScore = -9999999;
                        } else if (board.blackValidMoves.size() == 0) {
                            moveScore = 9999999;
                        } else {
                            moveScore = results.get(0).value;
                        }
                        alpha = Math.max(alpha, moveScore);
                    } else {
                        // Last value is min
                        if (board.whiteValidMoves.size() == 0
                                && board.blackValidMoves.size() == 0) {
                            moveScore = 0;
                        } else if (board.whiteValidMoves.size() == 0) {
                            moveScore = -9999999;
                        } else if (board.blackValidMoves.size() == 0) {
                            moveScore = 9999999;
                        } else {
                            moveScore = results.get(results.size() - 1).value;
                        }
                        beta = Math.min(beta, moveScore);
                    }

                    bestMoves.add(new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                            moveScore, new Piece(-1, -1, 1, board, handler, -1),
                            new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                            evoPossible[j]));
                    board.undoProper(true);

//                    if (this.whiteSide) {
//                        if (isMaximizingPlayer) {
//                            if (beta >= alpha) {
//                                // The move is too good so we should not
//                                // enter this position
//                                exit = true;
//                                break;
//                            }
//                        } else {
//                            if (beta <= alpha) {
//                                // The move is too good so we should not
//                                // enter this position
//                                exit = true;
//                                break;
//                            }
//                        }
//                    } else {
//                        if (isMaximizingPlayer) {
//                            if (beta <= alpha) {
//                                // The move is too good so we should not
//                                // enter this position
//                                exit = true;
//                                break;
//                            }
//                        } else {
//                            if (beta >= alpha) {
//                                // The move is too good so we should not
//                                // enter this position
//                                exit = true;
//                                break;
//                            }
//                        }
//                    }
//                    if (isMaximizingPlayer) {
//                        if (beta <= alpha) {
//                            // The move is too good so we should not
//                            // enter this position
//                            exit = true;
//                            break;
//                        }
//                    } else {
//                        if (beta >= alpha) {
//                            // The move is too good so we should not
//                            // enter this position
//                            exit = true;
//                            break;
//                        }
//                    }
                }
                //System.out.println(alpha + " " + beta);
//                    if (isMaximizingPlayer) {
//                        if (beta <= alpha) {
//                            // The move is too good so we should not
//                            // enter this position
//                            exit = true;
//                            break;
//                        }
//                    } else {
//                        if (alpha <= beta) {
//                            // The move is too good so we should not
//                            // enter this position
//                            exit = true;
//                            break;
//                        }
//                    }
            }
        }

        return bestMoves;
    }

    private double evaluateBoard(boolean whitePersp, int plyFromRoot) {
        double totalEvaluation = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                totalEvaluation += getPieceValue(board.chessBoard[i][j]);
                totalEvaluation += getSquareValue(board.chessBoard[i][j], i, j);
            }
        }
//        totalEvaluation += KingEval(whitePersp);
//
//        ArrayList<Pair> whiteCover = new ArrayList<>();
//        MoveGenerator mg = new MoveGenerator(handler, board);
//        for (int i = 0; i < handler.pieces.size(); i++) {
//            ArrayList<Pair> pieceCap = new ArrayList<>();
//            switch (handler.pieces.get(i).type) {
//                case 1:
//                    pieceCap = mg.WhitePawnCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 3:
//                    pieceCap = mg.WhiteBishopCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 5:
//                    pieceCap = mg.WhiteHorseCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 7:
//                    pieceCap = mg.WhiteRookCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 9:
//                    pieceCap = mg.WhiteQueenCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 11:
//                    pieceCap = mg.WhiteKingCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                default:
//                    break;
//            }
//            for (int j = 0; j < pieceCap.size(); j++) {
//                whiteCover.add(pieceCap.get(j));
//            }
//        }
//        totalEvaluation += getPosValue(whiteCover, whitePersp);
//        
//        ArrayList<Pair> blackCover = new ArrayList<>();
//        for (int i = 0; i < handler.pieces.size(); i++) {
//            ArrayList<Pair> pieceCap = new ArrayList<>();
//            switch (handler.pieces.get(i).type) {
//                case 2:
//                    pieceCap = mg.WhitePawnCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 4:
//                    pieceCap = mg.WhiteBishopCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 6:
//                    pieceCap = mg.WhiteHorseCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 8:
//                    pieceCap = mg.WhiteRookCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 10:
//                    pieceCap = mg.WhiteQueenCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 12:
//                    pieceCap = mg.WhiteKingCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                default:
//                    break;
//            }
//            for (int j = 0; j < pieceCap.size(); j++) {
//                blackCover.add(pieceCap.get(j));
//            }
//        }
//        totalEvaluation += getPosValue(blackCover, whitePersp);

        // May need to change whiteSide to the cur move evaluation perspective
        // so that bad moves are not counted as good from the current
        // perspective
        // The above statement has been done
        if (board.whiteValidMoves.isEmpty() && board.blackValidMoves.isEmpty()) {
            totalEvaluation = 0;
        } else if (board.whiteValidMoves.isEmpty() && board.checked && !whitePersp) {
            // Black gets points for checkmating
            totalEvaluation = this.defaultMin + plyFromRoot;
        } else if (board.blackValidMoves.isEmpty() && board.checked && whitePersp) {
            // White gets points for checkmating
            totalEvaluation = this.defaultMax - plyFromRoot;
        }
        return totalEvaluation;
    }

    // 1 White Pawn
    // 2 Black Pawn
    // 3 White Bishop
    // 4 Black Bishop
    // 5 White Horse
    // 6 Black Horse
    // 7 White Rook
    // 8 Black Rook
    // 9 White Queen
    // 10 Black Queen
    // 11 White King
    // 12 Black King
    private double getPieceValue(int pieceType) {
        int multi = 0;
        if (pieceType % 2 == 1) {
            multi = 1;
        } else if (pieceType % 2 == 0) {
            multi = -1;
        }
        switch (pieceType) {
            case 1:
            case 2:
                return multi * pawnValue;
            case 3:
            case 4:
                return multi * bishopValue;
            case 5:
            case 6:
                return multi * horseValue;
            case 7:
            case 8:
                return multi * rookValue;
            case 9:
            case 10:
                return multi * queenValue;
            case 11:
            case 12:
                return multi * kingValue;
            default:
                break;
        }

        return 0;
    }

    private double getSquareValue(int pieceType, int row, int col) {
        int multi = 0;
        if (pieceType % 2 == 1) {
            multi = 1;
        } else if (pieceType % 2 == 0) {
            multi = -1;
        }
        switch (pieceType) {
            case 1:
            case 2:
                return multi * pawnSquareTable[row][col];
            case 3:
            case 4:
                return multi * bishopSquareTable[row][col];
            case 5:
            case 6:
                return multi * knightSquareTable[row][col];
            case 7:
            case 8:
                return multi * rookSquareTable[row][col];
            case 9:
            case 10:
                return multi * queenSquareTable[row][col];
            case 11:
            case 12:
                return multi * kingSquareTable[row][col];
            default:
                break;
        }

        return 0;
    }

    private double KingEval(boolean whitePersp) {
        int oppKingRow = -1;
        int oppKingCol = -1;
        int selfKingRow = -1;
        int selfKingCol = -1;
        int friendlyKingIndex = -1;
        int modType = 1;
        if (whitePersp) {
            modType = 0;
        }
        int oppPieces = 0;
        for (int i = 0; i < handler.getSize(); i++) {
            Piece temp = handler.pieces.get(i);
            if (temp.type % 2 == modType) {
                oppPieces++;
            }
            if (temp.type == 11) {
                if (whitePersp) {
                    friendlyKingIndex = i;
                    selfKingRow = temp.getPosR();
                    selfKingCol = temp.getPosC();
                } else {
                    oppKingRow = temp.getPosR();
                    oppKingCol = temp.getPosC();
                }
            } else if (temp.type == 12) {
                if (!whitePersp) {
                    friendlyKingIndex = i;
                    selfKingRow = temp.getPosR();
                    selfKingCol = temp.getPosC();
                } else {
                    oppKingRow = temp.getPosR();
                    oppKingCol = temp.getPosC();
                }
            }
        }
        double endgameEvaluation = 0;
        int oppKingDistCenter = Math.max(3 - oppKingRow, oppKingRow - 4)
                + Math.max(3 - oppKingCol, oppKingCol - 4);
        int distBetweenKings = Math.abs(selfKingRow - oppKingRow)
                + Math.abs(selfKingCol - oppKingCol);
        endgameEvaluation += oppKingDistCenter;
        endgameEvaluation += 14 - distBetweenKings;

        //double adjustingConstant = 0.05;
        // Endgame weight maximum is 8
        double endgameWeight = Math.pow((Math.sqrt(2) / 4) * (0.5 * oppPieces)
                - Math.sqrt(8), 2);

        double earlyMidEvaluation = 0;
        if (handler.pieces.get(friendlyKingIndex).firstMove == true) {
            earlyMidEvaluation += castleAvaliabilityValue;
        }
        int selfKingDistCenter = Math.max(3 - selfKingRow, selfKingRow - 4)
                + Math.max(3 - selfKingCol, selfKingCol - 4);
        earlyMidEvaluation += selfKingDistCenter;

        double earlyMidWeight = 8 - endgameWeight;

        if (oppPieces > 8) {
            endgameWeight = 0;
        }

        if (oppPieces < 7) {
            earlyMidWeight = 0;
        }

        if (whitePersp) {
            return endgameEvaluation * (cornerEndgameValue) * endgameWeight
                    + earlyMidEvaluation * (safetyEarlyMidValue) * earlyMidWeight;
        } else {
            return -endgameEvaluation * (cornerEndgameValue) * endgameWeight
                    + -earlyMidEvaluation * (safetyEarlyMidValue) * earlyMidWeight;
        }
    }

    private double getPosValue(ArrayList<Pair> cover, boolean whitePersp) {
        double evaluation = 0;
        double multi = -1;
        if (whiteSide) {
            multi = 1;
        }
        for (int i = 0; i < cover.size(); i++) {
            Pair p = cover.get(i);
            evaluation += multi * controlValue[p.row][p.col];
            if (board.chessBoard[p.row][p.col] > 0) {
                if (board.chessBoard[p.row][p.col] % 2 == 0) {
                    // Black Piece
                    if (whitePersp) {
                        switch (board.chessBoard[p.row][p.col]) {
                            case 2:
                                evaluation += multi * threatValue * pawnValue;
                                break;
                            case 4:
                                evaluation += multi * threatValue * bishopValue;
                                break;
                            case 6:
                                evaluation += multi * threatValue * horseValue;
                                break;
                            case 8:
                                evaluation += multi * threatValue * rookValue;
                                break;
                            case 10:
                                evaluation += multi * threatValue * queenValue;
                                break;
                            case 12:
                                evaluation += multi * kingThreatValue;
                                break;
                            default:
                                break;
                        }
                    } else {
                        switch (board.chessBoard[p.row][p.col]) {
                            case 2:
                                evaluation += multi * supportPawnValue;
                                break;
                            case 4:
                                evaluation += multi * supportBishopValue;
                                break;
                            case 6:
                                evaluation += multi * supportHorseValue;
                                break;
                            case 8:
                                evaluation += multi * supportRookValue;
                                break;
                            case 10:
                                evaluation += multi * supportQueenValue;
                                break;
                            case 12:
                                evaluation += multi * supportKingValue;
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    // White Piece
                    if (whitePersp) {
                        switch (board.chessBoard[p.row][p.col]) {
                            case 1:
                                evaluation += multi * supportPawnValue;
                                break;
                            case 3:
                                evaluation += multi * supportBishopValue;
                                break;
                            case 5:
                                evaluation += multi * supportHorseValue;
                                break;
                            case 7:
                                evaluation += multi * supportRookValue;
                                break;
                            case 9:
                                evaluation += multi * supportQueenValue;
                                break;
                            case 11:
                                evaluation += multi * supportKingValue;
                                break;
                            default:
                                break;
                        }
                    } else {
                        switch (board.chessBoard[p.row][p.col]) {
                            case 1:
                                evaluation += multi * threatValue * pawnValue;
                                break;
                            case 3:
                                evaluation += multi * threatValue * bishopValue;
                                break;
                            case 5:
                                evaluation += multi * threatValue * horseValue;
                                break;
                            case 7:
                                evaluation += multi * threatValue * rookValue;
                                break;
                            case 9:
                                evaluation += multi * threatValue * queenValue;
                                break;
                            case 11:
                                evaluation += multi * kingThreatValue;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        return evaluation;
    }

    // Some of the previous decision making
    public void randomDecisionTick() {
        if (firstOpponent && board.playerType > 0 && board.whiteTurn == whiteSide) {
            // Now check if the moves have been rendered yet
            if (whiteSide && board.whiteValidMoves.size() > 0) {
                Random rand = new Random();
                Quad move = board.whiteValidMoves.get(rand.nextInt(board.whiteValidMoves.size()));
                Piece p = handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(move.sRow, move.sCol)));
                // Set the -1 to something else, if you wish to make it updated
                // set it to a random number {3, 5, 7, 9}
                board.properMove(p, move.dRow, move.dCol, -1, false);

            } else if (board.blackValidMoves.size() > 0) {
                Random rand = new Random();
                Quad move = board.blackValidMoves.get(rand.nextInt(board.blackValidMoves.size()));
                Piece p = handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(move.sRow, move.sCol)));
                board.properMove(p, move.dRow, move.dCol, -1, false);
            }
        }

        // Same as above but for second opponent
        if (!firstOpponent && board.playerType == 2
                && board.whiteTurn == whiteSide) {
            // Now check if the moves have been rendered yet
            if (whiteSide && board.whiteValidMoves.size() > 0) {
                Random rand = new Random();
                Quad move = board.whiteValidMoves.get(rand.nextInt(board.whiteValidMoves.size()));
                Piece p = handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(move.sRow, move.sCol)));
                board.properMove(p, move.dRow, move.dCol, -1, false);

            } else if (board.blackValidMoves.size() > 0) {
                Random rand = new Random();
                Quad move = board.blackValidMoves.get(rand.nextInt(board.blackValidMoves.size()));
                Piece p = handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(move.sRow, move.sCol)));
                board.properMove(p, move.dRow, move.dCol, -1, false);
            }
        }
    }

    public ArrayList<Move> Depth1Search(ArrayList<Quad> validMoves) {
        // This used the old piece evaluation method
        // where you always looked for max
        // this will no longer work
        ArrayList<Move> bestMoves = new ArrayList<>();
        for (int i = 0; i < validMoves.size(); i++) {
            Quad move = validMoves.get(i);
            Piece p = handler.pieces.get(handler.getPieceIndex(
                    handler.generateKey(move.sRow, move.sCol)));
//            System.out.print("Takes: " + board.chessBoard[move.dRow][move.dCol] 
//                    + " ");

            if ((p.type == 1 && move.dRow == 0)
                    || (p.type == 2 && move.dRow == 7)) {
                // 1 White Pawn
                // 2 Black Pawn
                // 3 White Bishop
                // 4 Black Bishop
                // 5 White Horse
                // 6 Black Horse
                // 7 White Rook
                // 8 Black Rook
                // 9 White Queen
                // 10 Black Queen
                // 11 White King
                // 12 Black King
                if (p.type == 1) {
                    // White
                    int[] evoPossible = {3, 5, 7, 9};
                    for (int j = 0; j < evoPossible.length; j++) {
                        board.properMove(p, move.dRow, move.dCol, evoPossible[j], true);
                        double moveScore = evaluateBoardOldTrue(whiteSide);
                        bestMoves.add(new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                                moveScore, new Piece(-1, -1, 1, board, handler, -1),
                                new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                                evoPossible[j]));
                        // Use the evolution source as a carried variable
                        // this will not affect the undo, as best moves
                        // is only used to determine what move to make
                        // just make sure that there are no other uses
                        // for evolution source in the opponent class
                        board.undoProper(true);
                    }
                } else {
                    // Black
                    int[] evoPossible = {4, 6, 8, 10};
                    for (int j = 0; j < evoPossible.length; j++) {
                        board.properMove(p, move.dRow, move.dCol, evoPossible[j], true);
                        double moveScore = evaluateBoardOldTrue(whiteSide);
                        bestMoves.add(new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                                moveScore, new Piece(-1, -1, 1, board, handler, -1),
                                new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                                evoPossible[j]));
                        board.undoProper(true);
                    }
                }

            } else {
                board.properMove(p, move.dRow, move.dCol, -1, true);
//            for(int j = 0; j < 8; j++){
//                for(int k = 0; k < 8; k++){
//                    System.out.print(board.chessBoard[j][k] + "\t");
//                }
//                System.out.println();
//            }
                double moveScore = evaluateBoardOldTrue(whiteSide);
                // The remPiece, castLoc's, passIndex are all unnecessary
//            System.out.println(moveScore);
                bestMoves.add(new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                        moveScore, new Piece(-1, -1, 1, board, handler, -1),
                        new Pair(-1, -1), new Pair(-1, -1), -1, false, false, -1));
                board.undoProper(true);
//            System.out.println();
//            for(int j = 0; j < 8; j++){
//                for(int k = 0; k < 8; k++){
//                    System.out.print(board.chessBoard[j][k] + "\t");
//                }
//                System.out.println();
//            }
            }

        }
//        Collections.sort(bestMoves);
//        for (int i = 0; i < bestMoves.size(); i++) {
//            System.out.println("Move Score: " + bestMoves.get(i).value);
//        }
        return bestMoves;
    }

    private ArrayList<Move> getBestMovesNoAB(int depth, boolean whitePersp,
            boolean isMaximizingPlayer) {

        ArrayList<Move> bestMoves = new ArrayList<>();
        ArrayList<Quad> possibleMoves = new ArrayList<>();
        if (whitePersp) {
            possibleMoves = board.whiteValidMoves;
        } else {
            possibleMoves = board.blackValidMoves;
        }
        Collections.shuffle(possibleMoves);

        for (int i = 0; i < possibleMoves.size(); i++) {
            Quad move = possibleMoves.get(i);
            Piece p = handler.pieces.get(handler.getPieceIndex(
                    handler.generateKey(move.sRow, move.sCol)));
            int[] evoPossible = {-1};
            if ((p.type == 1 && move.dRow == 0)
                    || (p.type == 2 && move.dRow == 7)) {
                if (p.type == 1) {
                    // White
                    evoPossible = new int[4];
                    evoPossible[0] = 3;
                    evoPossible[1] = 5;
                    evoPossible[2] = 7;
                    evoPossible[3] = 9;
                } else {
                    // Black
                    evoPossible = new int[4];
                    evoPossible[0] = 4;
                    evoPossible[1] = 6;
                    evoPossible[2] = 8;
                    evoPossible[3] = 10;
                }
            }
            for (int j = 0; j < evoPossible.length; j++) {
                board.properMove(p, move.dRow, move.dCol, evoPossible[j], true);

                if (depth <= 1) {
                    // Base case
                    double moveScore = 0;
                    moveScore = evaluateBoardOldTrue(whitePersp);

                    bestMoves.add(new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                            moveScore, new Piece(-1, -1, 1, board, handler, -1),
                            new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                            evoPossible[j]));
                    board.undoProper(true);
                } else {
                    // Recursive case
                    ArrayList<Move> results = getBestMovesNoAB(depth - 1, !whitePersp, !isMaximizingPlayer);

                    Collections.sort(results);
                    double moveScore = 0;
                    if (isMaximizingPlayer) {
                        // First value is max
                        moveScore = results.get(0).value;
                    } else {
                        // Last value is min
                        moveScore = results.get(results.size() - 1).value;
                    }

                    bestMoves.add(new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                            moveScore, new Piece(-1, -1, 1, board, handler, -1),
                            new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                            evoPossible[j]));
                    board.undoProper(true);
                }
                this.search++;
            }
        }

        return bestMoves;
    }

    private double evaluateBoardOldTrue(boolean whitePersp) {
        double totalEvaluation = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                totalEvaluation += getPieceValue(board.chessBoard[i][j]);
            }
        }
//        totalEvaluation += KingEval(whitePersp);
//
//        ArrayList<Pair> whiteCover = new ArrayList<>();
//        MoveGenerator mg = new MoveGenerator(handler, board);
//        for (int i = 0; i < handler.pieces.size(); i++) {
//            ArrayList<Pair> pieceCap = new ArrayList<>();
//            switch (handler.pieces.get(i).type) {
//                case 1:
//                    pieceCap = mg.WhitePawnCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 3:
//                    pieceCap = mg.WhiteBishopCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 5:
//                    pieceCap = mg.WhiteHorseCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 7:
//                    pieceCap = mg.WhiteRookCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 9:
//                    pieceCap = mg.WhiteQueenCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 11:
//                    pieceCap = mg.WhiteKingCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                default:
//                    break;
//            }
//            for (int j = 0; j < pieceCap.size(); j++) {
//                whiteCover.add(pieceCap.get(j));
//            }
//        }
//        totalEvaluation += getPosValue(whiteCover, whitePersp);
//        
//        ArrayList<Pair> blackCover = new ArrayList<>();
//        for (int i = 0; i < handler.pieces.size(); i++) {
//            ArrayList<Pair> pieceCap = new ArrayList<>();
//            switch (handler.pieces.get(i).type) {
//                case 2:
//                    pieceCap = mg.WhitePawnCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 4:
//                    pieceCap = mg.WhiteBishopCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 6:
//                    pieceCap = mg.WhiteHorseCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 8:
//                    pieceCap = mg.WhiteRookCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 10:
//                    pieceCap = mg.WhiteQueenCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                case 12:
//                    pieceCap = mg.WhiteKingCover(
//                            handler.pieces.get(i).getPosR(),
//                            handler.pieces.get(i).getPosC());
//                    break;
//                default:
//                    break;
//            }
//            for (int j = 0; j < pieceCap.size(); j++) {
//                blackCover.add(pieceCap.get(j));
//            }
//        }
//        totalEvaluation += getPosValue(blackCover, whitePersp);

        // May need to change whiteSide to the cur move evaluation perspective
        // so that bad moves are not counted as good from the current
        // perspective
        // The above statement has been done
        if (board.whiteValidMoves.isEmpty() && board.checked && !whitePersp) {
            // Black gets points for checkmating
            totalEvaluation = -9999999;
        } else if (board.blackValidMoves.isEmpty() && board.checked && whitePersp) {
            // White gets points for checkmating
            totalEvaluation = 9999999;
        }

        return totalEvaluation;
    }

    private ArrayList<Move> LagueNegamax(int depth, boolean whitePersp,
            double alpha, double beta, Move preAlphaMove) {
        ArrayList<Move> bestMoves = new ArrayList<>();
        if (depth == 0) {
            // Requires bias to be implemented into the evl=aluate
            double moveScore = evaluateBoard(whitePersp, 0);
            Move m = board.madeMoves.get(board.madeMoves.size() - 1);
            bestMoves.add(new Move(m.sRow, m.sCol, m.dRow, m.dCol,
                    moveScore, new Piece(-1, -1, 1, board, handler, -1),
                    new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                    m.evolutionSource));
            return bestMoves;
        }

        ArrayList<Quad> possibleMoves = new ArrayList<>();
        if (whitePersp) {
            possibleMoves = board.whiteValidMoves;
        } else {
            possibleMoves = board.blackValidMoves;
        }
        Collections.shuffle(possibleMoves);

        boolean exit = false;
        for (int i = 0; i < possibleMoves.size() && !exit; i++) {
            Quad move = possibleMoves.get(i);
            if (!handler.indexFinder.containsKey(
                    handler.generateKey(move.sRow, move.sCol))) {
                System.out.print("Error occured here: ");
                System.out.println(handler.generateKey(move.sRow, move.sCol));
                for (String key : handler.indexFinder.keySet()) {
                    System.out.println(key + " : " + handler.indexFinder.get(key));
                }
                continue;
            }
            Piece p = handler.pieces.get(handler.getPieceIndex(
                    handler.generateKey(move.sRow, move.sCol)));
            int[] evoPossible = {-1};
            if ((p.type == 1 && move.dRow == 0)
                    || (p.type == 2 && move.dRow == 7)) {
                if (p.type == 1) {
                    // White
                    evoPossible = new int[4];
                    evoPossible[0] = 3;
                    evoPossible[1] = 5;
                    evoPossible[2] = 7;
                    evoPossible[3] = 9;
                } else {
                    // Black
                    evoPossible = new int[4];
                    evoPossible[0] = 4;
                    evoPossible[1] = 6;
                    evoPossible[2] = 8;
                    evoPossible[3] = 10;
                }
            }
            for (int j = 0; j < evoPossible.length; j++) {
                this.search++;
                board.properMove(p, move.dRow, move.dCol, evoPossible[j], true);
                ArrayList<Move> results = LagueNegamax(depth - 1,
                        !whitePersp, -beta, -alpha, preAlphaMove);
                for (int k = 0; k < results.size(); k++) {
                    results.get(k).value *= -1;
                }

                Collections.sort(results);
                double moveScore = 0;
                // First value is max
                if (board.whiteValidMoves.size() == 0
                        && board.blackValidMoves.size() == 0) {
                    moveScore = 0;
                } else if (board.whiteValidMoves.size() == 0) {
                    moveScore = -9999999;
                } else if (board.blackValidMoves.size() == 0) {
                    moveScore = 9999999;
                } else {
                    moveScore = results.get(0).value;
                }

                bestMoves.add(new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                        moveScore, new Piece(-1, -1, 1, board, handler, -1),
                        new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                        evoPossible[j]));

                board.undoProper(true);

                if (moveScore >= beta) {
                    // Move was too good, opponent will avoid
                    // this position
                    exit = true;

                    // Returning the beta position
                    ArrayList<Move> ret = new ArrayList<>();
                    preAlphaMove.value = beta;
                    ret.add(preAlphaMove);
                    return ret;
                }

                if (moveScore > alpha) {
                    preAlphaMove = new Move(move.sRow, move.sCol, move.dRow, move.dCol,
                            moveScore, new Piece(-1, -1, 1, board, handler, -1),
                            new Pair(-1, -1), new Pair(-1, -1), -1, false, false,
                            evoPossible[j]);

                    alpha = moveScore;
                }

            }
        }
        return bestMoves;
    }
}
