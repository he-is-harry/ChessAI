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
public class SudoStorage {
//    public synchronized void sudoMove(Piece tempObject, int dR, int dC) {
//        checked = false;
//        Piece removePiece = new Piece(-1, -1, 1, this, handler, -1);
//        for (int i = 0; i < handler.getSize(); i++) {
//            Piece checkPiece = handler.pieces.get(i);
//            if (checkPiece.getPosR() == dR && checkPiece.getPosC() == dC) {
//                removePiece = checkPiece;
//                break;
//            } else if (checkPiece.type == 1 && tempObject.type == 2
//                    && checkPiece.getEnPassant()
//                    && checkPiece.getPosR() + 1 == dR
//                    && checkPiece.getPosC() == dC) {
//                removePiece = checkPiece;
//                break;
//            } else if (checkPiece.type == 2 && tempObject.type == 1
//                    && checkPiece.getEnPassant()
//                    && checkPiece.getPosR() - 1 == dR
//                    && checkPiece.getPosC() == dC) {
//                removePiece = checkPiece;
//                break;
//            }
//        }
//        // When castling both the king and rook will have to be moved
//        // You should check the move if it is a castle and then find the 
//        // rook that the king is targeting
//        Pair oldCastLoc = new Pair(-1, -1);
//        Pair newCastLoc = new Pair(-1, -1);
//        if (tempObject.type == 11) {
//            if (dC - tempObject.getPosC() == 2) {
//                //Right
//                Piece rook = handler.pieces.get(
//                        handler.getPieceIndex(handler.generateKey(7, 7)));
//
//                chessBoard[rook.getPosR()][rook.getPosC()] = 0;
//                chessBoard[dR][dC - 1] = rook.type;
//                handler.removeMapValue(handler.generateKey(rook.getPosR(),
//                        rook.getPosC()));
//                handler.addMapValue(handler.generateKey(dR, dC - 1),
//                        rook.getHandlerIndex());
//                rook.setPosR(dR);
//                rook.setPosC(dC - 1);
//
//                oldCastLoc = new Pair(7, 7);
//                newCastLoc = new Pair(dR, dC - 1);
//            } else if (dC - tempObject.getPosC() == -2) {
//                Piece rook = handler.pieces.get(
//                        handler.getPieceIndex(handler.generateKey(7, 0)));
//
//                chessBoard[rook.getPosR()][rook.getPosC()] = 0;
//                chessBoard[dR][dC + 1] = rook.type;
//                handler.removeMapValue(handler.generateKey(rook.getPosR(),
//                        rook.getPosC()));
//                handler.addMapValue(handler.generateKey(dR, dC + 1),
//                        rook.getHandlerIndex());
//                rook.setPosR(dR);
//                rook.setPosC(dC + 1);
//
//                oldCastLoc = new Pair(7, 0);
//                newCastLoc = new Pair(dR, dC + 1);
//            }
//        } else if (tempObject.type == 12) {
//            if (dC - tempObject.getPosC() == 2) {
//                //Right
//                Piece rook = handler.pieces.get(
//                        handler.getPieceIndex(handler.generateKey(0, 7)));
//
//                chessBoard[rook.getPosR()][rook.getPosC()] = 0;
//                chessBoard[dR][dC - 1] = rook.type;
//                handler.removeMapValue(handler.generateKey(rook.getPosR(),
//                        rook.getPosC()));
//                handler.addMapValue(handler.generateKey(dR, dC - 1),
//                        rook.getHandlerIndex());
//                rook.setPosR(dR);
//                rook.setPosC(dC - 1);
//
//                oldCastLoc = new Pair(0, 7);
//                newCastLoc = new Pair(dR, dC - 1);
//            } else if (dC - tempObject.getPosC() == -2) {
//                Piece rook = handler.pieces.get(
//                        handler.getPieceIndex(handler.generateKey(0, 0)));
//
//                chessBoard[rook.getPosR()][rook.getPosC()] = 0;
//                chessBoard[dR][dC + 1] = rook.type;
//                handler.removeMapValue(handler.generateKey(rook.getPosR(),
//                        rook.getPosC()));
//                handler.addMapValue(handler.generateKey(dR, dC + 1),
//                        rook.getHandlerIndex());
//                rook.setPosR(dR);
//                rook.setPosC(dC + 1);
//
//                oldCastLoc = new Pair(0, 0);
//                newCastLoc = new Pair(dR, dC + 1);
//            }
//        }
//        int sourceR = tempObject.getPosR();
//        int sourceC = tempObject.getPosC();
//        boolean isFirstMove = tempObject.firstMove;
//        boolean restorePass = false;
//        if (isFirstMove && Math.abs(dR - sourceR) == 2 && 
//                (tempObject.type == 1 || tempObject.type == 2)) {
//                 restorePass = true;
//        }
//        
//
////        for(int i = 0; i < madeMoves.size(); i++){
////            System.out.println("Source: " + madeMoves.get(i).sRow +
////            " " + madeMoves.get(i).sCol + " Dest: " + madeMoves.get(i).dRow +
////            " " + madeMoves.get(i).dCol);
//////             + " Rem: " + madeMoves.get(i).remPiece.type
//////            + " CastLoc: " + madeMoves.get(i).castleLoc.row + " " + 
//////                    madeMoves.get(i).castleLoc.col
////        }
////        System.out.println();
//        if (tempObject.firstMove) {
//            tempObject.firstMove = false;
//            if (Math.abs(dR - sourceR) == 2 && (tempObject.type == 1 || 
//                    tempObject.type == 2)) {
//                tempObject.enPassant = true;
//            }
//        }
//        chessBoard[tempObject.getPosR()][tempObject.getPosC()] = 0;
//        chessBoard[dR][dC] = tempObject.type;
//        handler.removeMapValue(handler.generateKey(tempObject.getPosR(),
//                tempObject.getPosC()));
//        handler.addMapValue(handler.generateKey(dR, dC),
//                tempObject.getHandlerIndex());
//        tempObject.setPosR(dR);
//        tempObject.setPosC(dC);
//        if (removePiece != null) {
//            handler.removeObject(removePiece);
//        }
//
//        int restorePassIndex = -1;
//        // Remove en passant for the current turns team
//        for (int i = 0; i < handler.pieces.size(); i++) {
//            if (whiteTurn && handler.pieces.get(i).type == 1) {
//                if (handler.pieces.get(i).enPassant) {
//                    restorePassIndex = i;
//                }
//                handler.pieces.get(i).checkPassant();
//            } else if (!whiteTurn && handler.pieces.get(i).type == 2) {
//                if (handler.pieces.get(i).enPassant) {
//                    restorePassIndex = i;
//                }
//                handler.pieces.get(i).checkPassant();
//            }
//        }
//        madeMoves.add(new Move(sourceR, sourceC,
//                dR, dC, 0.0, removePiece, oldCastLoc, newCastLoc,
//                restorePassIndex, isFirstMove, restorePass));
//
//        // Important creation of new legal moves after
//        makeNewLegalMoves();
//    }
    
    
//    public synchronized void undoSudo() {
//        Move move = madeMoves.get(madeMoves.size() - 1);
//
//        Piece p = handler.pieces.get(handler.getPieceIndex(
//                handler.generateKey(move.dRow, move.dCol)));
//
//        chessBoard[move.dRow][move.dCol] = 0;
//        chessBoard[move.sRow][move.sCol] = p.type;
//        handler.removeMapValue(handler.generateKey(move.dRow,
//                move.dCol));
//        handler.addMapValue(handler.generateKey(move.sRow, move.sCol),
//                p.getHandlerIndex());
//        p.setPosR(move.sRow);
//        p.setPosC(move.sCol);
//        //System.out.println("Returned: " + chessBoard[move.sRow][move.sCol]);
//        if (move.remPiece.getPosR() != -1) {
//            handler.addObject(move.remPiece);
//            move.remPiece.setHandlerIndex(handler.getSize() - 1);
//            handler.addMapValue(handler.generateKey(move.remPiece.getPosR(),
//                    move.remPiece.getPosC()), move.remPiece.getHandlerIndex());
//        }
//        if (move.oldCastLoc.row != -1) {
//            Piece rook = handler.pieces.get(handler.getPieceIndex(
//                    handler.generateKey(move.newCastLoc.row,
//                            move.newCastLoc.col)));
//
//            chessBoard[move.newCastLoc.row][move.newCastLoc.col] = 0;
//            chessBoard[move.oldCastLoc.row][move.oldCastLoc.col] = rook.type;
//            handler.removeMapValue(handler.generateKey(move.newCastLoc.row,
//                    move.newCastLoc.col));
//            handler.addMapValue(handler.generateKey(move.oldCastLoc.row,
//                    move.oldCastLoc.col), rook.getHandlerIndex());
//            rook.setPosR(move.oldCastLoc.row);
//            rook.setPosC(move.oldCastLoc.col);
//        }
//        if (move.restorePassIndex != -1) {
//            handler.pieces.get(move.restorePassIndex).enPassant = true;
//        }
//        madeMoves.remove(madeMoves.size() - 1);
//    }
}
