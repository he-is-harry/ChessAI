/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;

/**
 *
 * @author harryhe
 */
public class MoveGenerator {

    Handler handler;
    Board board;

    public MoveGenerator(Handler handler, Board board) {
        this.handler = handler;
        this.board = board;
    }

    public ArrayList<Pair> WhitePawn(int posR, int posC, boolean firstMove) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR > 0 && board.chessBoard[posR - 1][posC] == 0) {
            moves.add(new Pair(posR - 1, posC));
            if (firstMove && board.chessBoard[posR - 2][posC] == 0) {
                moves.add(new Pair(posR - 2, posC));
            }
        }
//            if (posR > 0 && posC > 0) {
//                System.out.println(posR + " " + posC + " " + 
//                    board.chessBoard[posR - 1][posC - 1]);
//            }
        if (posR > 0 && posC > 0
                && board.chessBoard[posR - 1][posC - 1] > 0
                && board.chessBoard[posR - 1][posC - 1] % 2 == 0) {
            moves.add(new Pair(posR - 1, posC - 1));
        }

//            if (posR > 0 && posC < board.chessBoard.length - 1) {
//                System.out.println(posR + " " + posC + " " + 
//                    board.chessBoard[posR - 1][posC + 1]);
//            }
        if (posR > 0 && posC < board.chessBoard.length - 1
                && board.chessBoard[posR - 1][posC + 1] > 0
                && board.chessBoard[posR - 1][posC + 1] % 2 == 0) {
            moves.add(new Pair(posR - 1, posC + 1));
        }
        // En passant
        if (posC > 0
                && handler.indexFinder.containsKey(handler.generateKey(posR, posC - 1))) {
            int checkIndex = handler.getPieceIndex(
                    handler.generateKey(posR, posC - 1));
//                System.out.println(handler.pieces.get(checkIndex).type + " - " + posC + " " +
//                        (handler.pieces.get(checkIndex).enPassant));
            if (board.chessBoard[posR][posC - 1] == 2
                    && handler.pieces.get(checkIndex).getEnPassant()) {
                // If the en passant is implemented correctly there will 
                // be no pieces behind the pawn
                moves.add(new Pair(posR - 1, posC - 1));
            }
        }
        if (posC < board.chessBoard.length - 1
                && handler.indexFinder.containsKey(handler.generateKey(posR, posC + 1))) {
            int checkIndex = handler.getPieceIndex(
                    handler.generateKey(posR, posC + 1));
//                System.out.println(handler.pieces.get(checkIndex).type + " + " + posC + " " +
//                        (handler.pieces.get(checkIndex).enPassant));
            //System.out.println(handler.pieces.get(checkIndex).getEnPassant() + " " + posC);
            if (board.chessBoard[posR][posC + 1] == 2
                    && handler.pieces.get(checkIndex).getEnPassant()) {
                // If the en passant is implemented correctly there will 
                // be no pieces behind the pawn
                moves.add(new Pair(posR - 1, posC + 1));
            }
        }
        return moves;
    }

    public ArrayList<Pair> BlackPawn(int posR, int posC, boolean firstMove) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 1
                && board.chessBoard[posR + 1][posC] == 0) {
            moves.add(new Pair(posR + 1, posC));
            if (firstMove && board.chessBoard[posR + 2][posC] == 0) {
                moves.add(new Pair(posR + 2, posC));
            }
        }
        if (posR < board.chessBoard.length - 1 && posC > 0
                && board.chessBoard[posR + 1][posC - 1] > 0
                && board.chessBoard[posR + 1][posC - 1] % 2 == 1) {
            moves.add(new Pair(posR + 1, posC - 1));
        }

        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 1
                && board.chessBoard[posR + 1][posC + 1] > 0
                && board.chessBoard[posR + 1][posC + 1] % 2 == 1) {
            moves.add(new Pair(posR + 1, posC + 1));
        }
        // En passant
        if (posC > 0
                && handler.indexFinder.containsKey(handler.generateKey(posR, posC - 1))) {
            int checkIndex = handler.getPieceIndex(
                    handler.generateKey(posR, posC - 1));
            //System.out.println(handler.pieces.get(checkIndex).getEnPassant() + " " + posC);
            if (board.chessBoard[posR][posC - 1] == 1
                    && handler.pieces.get(checkIndex).getEnPassant()) {
                // If the en passant is implemented correctly there will 
                // be no pieces behind the pawn
                moves.add(new Pair(posR + 1, posC - 1));
            }
        }
        if (posC < board.chessBoard.length - 1
                && handler.indexFinder.containsKey(handler.generateKey(posR, posC + 1))) {
            int checkIndex = handler.getPieceIndex(
                    handler.generateKey(posR, posC + 1));
            if (board.chessBoard[posR][posC + 1] == 1
                    && handler.pieces.get(checkIndex).getEnPassant()) {
                // If the en passant is implemented correctly there will 
                // be no pieces behind the pawn
                moves.add(new Pair(posR + 1, posC + 1));
            }
        }
        return moves;
    }

    public ArrayList<Pair> WhiteHorse(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 2 && posC > 0
                && board.chessBoard[posR + 2][posC - 1] % 2 == 0) {
            moves.add(new Pair(posR + 2, posC - 1));
        }
        if (posR < board.chessBoard.length - 2
                && posC < board.chessBoard.length - 1
                && board.chessBoard[posR + 2][posC + 1] % 2 == 0) {
            moves.add(new Pair(posR + 2, posC + 1));
        }
        if (posR > 1 && posC > 0
                && board.chessBoard[posR - 2][posC - 1] % 2 == 0) {
            moves.add(new Pair(posR - 2, posC - 1));
        }
        if (posR > 1 && posC < board.chessBoard.length - 1
                && board.chessBoard[posR - 2][posC + 1] % 2 == 0) {
            moves.add(new Pair(posR - 2, posC + 1));
        }
        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 2
                && board.chessBoard[posR + 1][posC + 2] % 2 == 0) {
            moves.add(new Pair(posR + 1, posC + 2));
        }
        if (posR > 0
                && posC < board.chessBoard.length - 2
                && board.chessBoard[posR - 1][posC + 2] % 2 == 0) {
            moves.add(new Pair(posR - 1, posC + 2));
        }
        if (posR < board.chessBoard.length - 1
                && posC > 1
                && board.chessBoard[posR + 1][posC - 2] % 2 == 0) {
            moves.add(new Pair(posR + 1, posC - 2));
        }
        if (posR > 0
                && posC > 1
                && board.chessBoard[posR - 1][posC - 2] % 2 == 0) {
            moves.add(new Pair(posR - 1, posC - 2));
        }
        return moves;
    }

    public ArrayList<Pair> BlackHorse(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 2 && posC > 0
                && (board.chessBoard[posR + 2][posC - 1] % 2 == 1
                || board.chessBoard[posR + 2][posC - 1] == 0)) {
            moves.add(new Pair(posR + 2, posC - 1));
        }
        if (posR < board.chessBoard.length - 2
                && posC < board.chessBoard.length - 1
                && (board.chessBoard[posR + 2][posC + 1] % 2 == 1
                || board.chessBoard[posR + 2][posC + 1] == 0)) {
            moves.add(new Pair(posR + 2, posC + 1));
        }
        if (posR > 1 && posC > 0
                && (board.chessBoard[posR - 2][posC - 1] % 2 == 1
                || board.chessBoard[posR - 2][posC - 1] == 0)) {
            moves.add(new Pair(posR - 2, posC - 1));
        }
        if (posR > 1 && posC < board.chessBoard.length - 1
                && (board.chessBoard[posR - 2][posC + 1] % 2 == 1
                || board.chessBoard[posR - 2][posC + 1] == 0)) {
            moves.add(new Pair(posR - 2, posC + 1));
        }
        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 2
                && (board.chessBoard[posR + 1][posC + 2] % 2 == 1
                || board.chessBoard[posR + 1][posC + 2] == 0)) {
            moves.add(new Pair(posR + 1, posC + 2));
        }
        if (posR > 0
                && posC < board.chessBoard.length - 2
                && (board.chessBoard[posR - 1][posC + 2] % 2 == 1
                || board.chessBoard[posR - 1][posC + 2] == 0)) {
            moves.add(new Pair(posR - 1, posC + 2));
        }
        if (posR < board.chessBoard.length - 1
                && posC > 1
                && (board.chessBoard[posR + 1][posC - 2] % 2 == 1
                || board.chessBoard[posR + 1][posC - 2] == 0)) {
            moves.add(new Pair(posR + 1, posC - 2));
        }
        if (posR > 0
                && posC > 1
                && (board.chessBoard[posR - 1][posC - 2] % 2 == 1
                || board.chessBoard[posR - 1][posC - 2] == 0)) {
            moves.add(new Pair(posR - 1, posC - 2));
        }
        return moves;
    }

    public ArrayList<Pair> WhiteBishop(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0 && curC > 0) {
            curR--;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR > 0 && curC < board.chessBoard.length - 1) {
            curR--;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1 && curC > 0) {
            curR++;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1
                && curC < board.chessBoard.length - 1) {
            curR++;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> BlackBishop(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0 && curC > 0) {
            curR--;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR > 0 && curC < board.chessBoard.length - 1) {
            curR--;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1 && curC > 0) {
            curR++;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1
                && curC < board.chessBoard.length - 1) {
            curR++;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> WhiteRook(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0) {
            curR--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        while (curC < board.chessBoard.length - 1) {
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curC = posC;
        while (curR < board.chessBoard.length - 1) {
            curR++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        while (curC > 0) {
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> BlackRook(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0) {
            curR--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        while (curC < board.chessBoard.length - 1) {
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curC = posC;
        while (curR < board.chessBoard.length - 1) {
            curR++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        while (curC > 0) {
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> WhiteQueen(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0) {
            curR--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        while (curC < board.chessBoard.length - 1) {
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curC = posC;
        while (curR < board.chessBoard.length - 1) {
            curR++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        while (curC > 0) {
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curC = posC;
        while (curR > 0 && curC > 0) {
            curR--;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR > 0 && curC < board.chessBoard.length - 1) {
            curR--;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1 && curC > 0) {
            curR++;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1
                && curC < board.chessBoard.length - 1) {
            curR++;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 0) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> BlackQueen(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0) {
            curR--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        while (curC < board.chessBoard.length - 1) {
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curC = posC;
        while (curR < board.chessBoard.length - 1) {
            curR++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        while (curC > 0) {
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curC = posC;
        while (curR > 0 && curC > 0) {
            curR--;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR > 0 && curC < board.chessBoard.length - 1) {
            curR--;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1 && curC > 0) {
            curR++;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1
                && curC < board.chessBoard.length - 1) {
            curR++;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] % 2 == 1) {
                moves.add(new Pair(curR, curC));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> WhiteKing(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 1 && posC > 0
                && board.chessBoard[posR + 1][posC - 1] % 2 == 0) {
            moves.add(new Pair(posR + 1, posC - 1));
        }
        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 1
                && board.chessBoard[posR + 1][posC + 1] % 2 == 0) {
            moves.add(new Pair(posR + 1, posC + 1));
        }
        if (posR > 0 && posC > 0
                && board.chessBoard[posR - 1][posC - 1] % 2 == 0) {
            moves.add(new Pair(posR - 1, posC - 1));
        }
        if (posR > 0 && posC < board.chessBoard.length - 1
                && board.chessBoard[posR - 1][posC + 1] % 2 == 0) {
            moves.add(new Pair(posR - 1, posC + 1));
        }
        if(posR < board.chessBoard.length - 1 && 
                board.chessBoard[posR + 1][posC] % 2 == 0){
            moves.add(new Pair(posR + 1, posC));
        }
        if(posR > 0 && 
                board.chessBoard[posR - 1][posC] % 2 == 0){
            moves.add(new Pair(posR - 1, posC));
        }
        if(posC < board.chessBoard.length - 1 && 
                board.chessBoard[posR][posC + 1] % 2 == 0){
            moves.add(new Pair(posR, posC + 1));
        }
        if(posC > 0 && 
                board.chessBoard[posR][posC - 1] % 2 == 0){
            moves.add(new Pair(posR, posC - 1));
        }
        if (handler.indexFinder.containsKey(handler.generateKey(posR, posC))
                && handler.indexFinder.containsKey(handler.generateKey(7, 0))) {
            Piece king = handler.pieces.get(
                    handler.getPieceIndex(handler.generateKey(posR, posC)));
            Piece rook = handler.pieces.get(
                    handler.getPieceIndex(handler.generateKey(7, 0)));
            int curC = posC;
            boolean clear = true;
            while(curC > 1){
                curC--;
                if(board.chessBoard[posR][curC] != 0){
                    clear = false;
                    break;
                }
            }
            if (king.firstMove && rook.firstMove && clear) {
                moves.add(new Pair(posR, posC - 2));
            }
        }
        if (handler.indexFinder.containsKey(handler.generateKey(posR, posC))
                && handler.indexFinder.containsKey(handler.generateKey(7, 7))) {
            Piece king = handler.pieces.get(
                    handler.getPieceIndex(handler.generateKey(posR, posC)));
            Piece rook = handler.pieces.get(
                    handler.getPieceIndex(handler.generateKey(7, 7)));
            int curC = posC;
            boolean clear = true;
            while(curC < board.chessBoard.length - 2){
                curC++;
                if(board.chessBoard[posR][curC] != 0){
                    clear = false;
                    break;
                }
            }
            if (king.firstMove && rook.firstMove && clear) {
                moves.add(new Pair(posR, posC + 2));
            }
        }
        return moves;
    }
    
    public ArrayList<Pair> BlackKing(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 1 && posC > 0
                && (board.chessBoard[posR + 1][posC - 1] % 2 == 1 || 
                board.chessBoard[posR + 1][posC - 1] == 0)) {
            moves.add(new Pair(posR + 1, posC - 1));
        }
        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 1
                && (board.chessBoard[posR + 1][posC + 1] % 2 == 1 ||
                board.chessBoard[posR + 1][posC + 1] == 0)) {
            moves.add(new Pair(posR + 1, posC + 1));
        }
        if (posR > 0 && posC > 0
                && (board.chessBoard[posR - 1][posC - 1] % 2 == 1 ||
                board.chessBoard[posR - 1][posC - 1] == 0)) {
            moves.add(new Pair(posR - 1, posC - 1));
        }
        if (posR > 0 && posC < board.chessBoard.length - 1
                && (board.chessBoard[posR - 1][posC + 1] % 2 == 1 ||
                board.chessBoard[posR - 1][posC + 1] == 0)) {
            moves.add(new Pair(posR - 1, posC + 1));
        }
        if(posR < board.chessBoard.length - 1 && 
                (board.chessBoard[posR + 1][posC] % 2 == 1 ||
                board.chessBoard[posR + 1][posC] == 0)){
            moves.add(new Pair(posR + 1, posC));
        }
        if(posR > 0 && 
                (board.chessBoard[posR - 1][posC] % 2 == 1 ||
                board.chessBoard[posR - 1][posC] == 0)){
            moves.add(new Pair(posR - 1, posC));
        }
        if(posC < board.chessBoard.length - 1 && 
                (board.chessBoard[posR][posC + 1] % 2 == 1 ||
                board.chessBoard[posR][posC + 1] == 0)){
            moves.add(new Pair(posR, posC + 1));
        }
        if(posC > 0 && 
                (board.chessBoard[posR][posC - 1] % 2 == 1 ||
                board.chessBoard[posR][posC - 1] == 0)){
            moves.add(new Pair(posR, posC - 1));
        }
        if (handler.indexFinder.containsKey(handler.generateKey(posR, posC))
                && handler.indexFinder.containsKey(handler.generateKey(0, 0))) {
            Piece king = handler.pieces.get(
                    handler.getPieceIndex(handler.generateKey(posR, posC)));
            Piece rook = handler.pieces.get(
                    handler.getPieceIndex(handler.generateKey(0, 0)));
            int curC = posC;
            boolean clear = true;
            while(curC > 1){
                curC--;
                if(board.chessBoard[posR][curC] != 0){
                    clear = false;
                    break;
                }
            }
            if (king.firstMove && rook.firstMove && clear) {
                moves.add(new Pair(posR, posC - 2));
            }
        }
        if (handler.indexFinder.containsKey(handler.generateKey(posR, posC))
                && handler.indexFinder.containsKey(handler.generateKey(0, 7))) {
            Piece king = handler.pieces.get(
                    handler.getPieceIndex(handler.generateKey(posR, posC)));
            Piece rook = handler.pieces.get(
                    handler.getPieceIndex(handler.generateKey(0, 7)));
            int curC = posC;
            boolean clear = true;
            while(curC < board.chessBoard.length - 2){
                curC++;
                if(board.chessBoard[posR][curC] != 0){
                    clear = false;
                    break;
                }
            }
            if (king.firstMove && rook.firstMove && clear) {
                moves.add(new Pair(posR, posC + 2));
            }
        }
        //System.out.println(moves.size());
        return moves;
        
    }
    
    // Unfiltered versions underneath
    
    public ArrayList<Pair> WhitePawnCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR > 0 && posC > 0) {
            moves.add(new Pair(posR - 1, posC - 1));
        }
        if (posR > 0 && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR - 1, posC + 1));
        }
        return moves;
    }

    public ArrayList<Pair> BlackPawnCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 1 && posC > 0) {
            moves.add(new Pair(posR + 1, posC - 1));
        }

        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR + 1, posC + 1));
        }
        return moves;
    }

    public ArrayList<Pair> WhiteHorseCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 2 && posC > 0) {
            moves.add(new Pair(posR + 2, posC - 1));
        }
        if (posR < board.chessBoard.length - 2
                && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR + 2, posC + 1));
        }
        if (posR > 1 && posC > 0) {
            moves.add(new Pair(posR - 2, posC - 1));
        }
        if (posR > 1 && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR - 2, posC + 1));
        }
        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 2) {
            moves.add(new Pair(posR + 1, posC + 2));
        }
        if (posR > 0
                && posC < board.chessBoard.length - 2) {
            moves.add(new Pair(posR - 1, posC + 2));
        }
        if (posR < board.chessBoard.length - 1
                && posC > 1) {
            moves.add(new Pair(posR + 1, posC - 2));
        }
        if (posR > 0
                && posC > 1) {
            moves.add(new Pair(posR - 1, posC - 2));
        }
        return moves;
    }

    public ArrayList<Pair> BlackHorseCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 2 && posC > 0) {
            moves.add(new Pair(posR + 2, posC - 1));
        }
        if (posR < board.chessBoard.length - 2
                && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR + 2, posC + 1));
        }
        if (posR > 1 && posC > 0) {
            moves.add(new Pair(posR - 2, posC - 1));
        }
        if (posR > 1 && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR - 2, posC + 1));
        }
        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 2) {
            moves.add(new Pair(posR + 1, posC + 2));
        }
        if (posR > 0
                && posC < board.chessBoard.length - 2) {
            moves.add(new Pair(posR - 1, posC + 2));
        }
        if (posR < board.chessBoard.length - 1
                && posC > 1) {
            moves.add(new Pair(posR + 1, posC - 2));
        }
        if (posR > 0
                && posC > 1) {
            moves.add(new Pair(posR - 1, posC - 2));
        }
        return moves;
    }

    public ArrayList<Pair> WhiteBishopCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0 && curC > 0) {
            curR--;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR > 0 && curC < board.chessBoard.length - 1) {
            curR--;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1 && curC > 0) {
            curR++;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1
                && curC < board.chessBoard.length - 1) {
            curR++;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> BlackBishopCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0 && curC > 0) {
            curR--;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR > 0 && curC < board.chessBoard.length - 1) {
            curR--;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1 && curC > 0) {
            curR++;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1
                && curC < board.chessBoard.length - 1) {
            curR++;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> WhiteRookCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0) {
            curR--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        while (curC < board.chessBoard.length - 1) {
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curC = posC;
        while (curR < board.chessBoard.length - 1) {
            curR++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        while (curC > 0) {
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> BlackRookCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0) {
            curR--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        while (curC < board.chessBoard.length - 1) {
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curC = posC;
        while (curR < board.chessBoard.length - 1) {
            curR++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        while (curC > 0) {
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> WhiteQueenCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0) {
            curR--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        while (curC < board.chessBoard.length - 1) {
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curC = posC;
        while (curR < board.chessBoard.length - 1) {
            curR++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        while (curC > 0) {
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curC = posC;
        while (curR > 0 && curC > 0) {
            curR--;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR > 0 && curC < board.chessBoard.length - 1) {
            curR--;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1 && curC > 0) {
            curR++;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1
                && curC < board.chessBoard.length - 1) {
            curR++;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 12){
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> BlackQueenCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        int curR = posR;
        int curC = posC;
        while (curR > 0) {
            curR--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        while (curC < board.chessBoard.length - 1) {
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curC = posC;
        while (curR < board.chessBoard.length - 1) {
            curR++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        while (curC > 0) {
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curC = posC;
        while (curR > 0 && curC > 0) {
            curR--;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR > 0 && curC < board.chessBoard.length - 1) {
            curR--;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1 && curC > 0) {
            curR++;
            curC--;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }

        curR = posR;
        curC = posC;
        while (curR < board.chessBoard.length - 1
                && curC < board.chessBoard.length - 1) {
            curR++;
            curC++;
            if (board.chessBoard[curR][curC] == 0) {
                moves.add(new Pair(curR, curC));
            } else if (board.chessBoard[curR][curC] > 0) {
                moves.add(new Pair(curR, curC));
                if(board.chessBoard[curR][curC] != 11){
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<Pair> WhiteKingCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 1 && posC > 0) {
            moves.add(new Pair(posR + 1, posC - 1));
        }
        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR + 1, posC + 1));
        }
        if (posR > 0 && posC > 0) {
            moves.add(new Pair(posR - 1, posC - 1));
        }
        if (posR > 0 && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR - 1, posC + 1));
        }
        if(posR < board.chessBoard.length - 1){
            moves.add(new Pair(posR + 1, posC));
        }
        if(posR > 0){
            moves.add(new Pair(posR - 1, posC));
        }
        if(posC < board.chessBoard.length - 1){
            moves.add(new Pair(posR, posC + 1));
        }
        if(posC > 0){
            moves.add(new Pair(posR, posC - 1));
        }
        return moves;
    }
    
    public ArrayList<Pair> BlackKingCover(int posR, int posC) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (posR < board.chessBoard.length - 1 && posC > 0) {
            moves.add(new Pair(posR + 1, posC - 1));
        }
        if (posR < board.chessBoard.length - 1
                && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR + 1, posC + 1));
        }
        if (posR > 0 && posC > 0) {
            moves.add(new Pair(posR - 1, posC - 1));
        }
        if (posR > 0 && posC < board.chessBoard.length - 1) {
            moves.add(new Pair(posR - 1, posC + 1));
        }
        if(posR < board.chessBoard.length - 1){
            moves.add(new Pair(posR + 1, posC));
        }
        if(posR > 0){
            moves.add(new Pair(posR - 1, posC));
        }
        if(posC < board.chessBoard.length - 1){
            moves.add(new Pair(posR, posC + 1));
        }
        if(posC > 0){
            moves.add(new Pair(posR, posC - 1));
        }
        
        //System.out.println(moves.size());
        return moves;
        
    }
}
