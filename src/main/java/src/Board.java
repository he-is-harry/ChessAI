/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author harryhe
 */
public class Board extends MouseAdapter {

    private Game game;
    private Handler handler;
    public int[][] chessBoard;
    public boolean wPers;
    public int playerType;
    public boolean selectEvo;
    public int evoType;
    public Piece evoPiece;
    public Move evoMove;

    private Color boardWhite;
    private Color boardGreen;
    public float squareLength;
    private Font LetterFont;
    private FontLoader fl;

    public ArrayList<Quad> whiteValidMoves;
    public ArrayList<Quad> blackValidMoves;
    public ArrayList<Move> madeMoves;
    public ArrayList<Move> properMoves;
    public boolean checked;
    public boolean whiteTurn;
    public int endResult;

    public boolean pendingAction;

    public int enPassantFile;
    public boolean bKCast;
    public boolean bQCast;
    public boolean wKCast;
    public boolean wQCast;
    public long ZobristKey;
    private Zobrist zob;

    private int clickedIndex;

    public Board(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;
        boardGreen = new Color(114, 148, 83);
        boardWhite = new Color(234, 234, 200);
        squareLength = (float) 86; // This must go before resets
        wPers = true;
        whiteTurn = true;
        playerType = 0;
        // 0 - PvP
        // 1 - PvC
        // 2 - CvC
        // Set this in menu rather than board
        // However checks will be still in board
        selectEvo = false;
        evoType = -1;
        evoPiece = new Piece(-1, -1, -1, this, handler, -1);
        evoMove = new Move(-1, -1, -1, -1, 0.0, new Piece(-1, -1, 1, this, handler, -1),
                new Pair(-1, -1), new Pair(-1, -1), -1, false, false, -1);

        resetBoard();
        
        this.zob = new Zobrist(this);
        ZobristKey = zob.CalculateZobristKey();

        fl = new FontLoader();
        LetterFont = new Font("arial", 1, 20);

        whiteValidMoves = new ArrayList<>();
        blackValidMoves = new ArrayList<>();
        madeMoves = new ArrayList<>();
        properMoves = new ArrayList<>();
        checked = false;
        endResult = -1;
        // -1 is no end result
        // 0 is white wins
        // 1 is black wins
        // 2 is stalemate

        pendingAction = false;

        enPassantFile = -1;

        clickedIndex = -1;
    }

    public void render(Graphics g) {
        g.setColor(boardWhite);
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                if ((col + row) % 2 == 0) {
                    g.fillRect((int) (20 + col * squareLength),
                            (int) (20 + row * squareLength),
                            (int) squareLength, (int) squareLength);
                }
            }
        }

        g.setColor(boardGreen);
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                if ((col + row) % 2 == 1) {
                    g.fillRect((int) (20 + col * squareLength),
                            (int) (20 + row * squareLength),
                            (int) squareLength, (int) squareLength);
                }
            }
        }

        // Letter Rendering
        g.setFont(LetterFont);
        g.setColor(boardWhite);

        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                if (wPers) {
                    g.drawString(Character.toString((char) ('a' + i)),
                            (int) (20 + i * squareLength + (0.8 * squareLength)),
                            (int) (20 + 8 * squareLength - 0.075 * squareLength));
                } else {
                    g.drawString(Character.toString((char) ('h' - i)),
                            (int) (20 + i * squareLength + (0.8 * squareLength)),
                            (int) (20 + 8 * squareLength - 0.075 * squareLength));
                }
            }
        }

        g.setColor(boardGreen);
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 1) {
                if (wPers) {
                    g.drawString(Character.toString((char) ('a' + i)),
                            (int) (20 + i * squareLength + (0.8 * squareLength)),
                            (int) (20 + 8 * squareLength - 0.075 * squareLength));
                } else {
                    g.drawString(Character.toString((char) ('h' - i)),
                            (int) (20 + i * squareLength + (0.8 * squareLength)),
                            (int) (20 + 8 * squareLength - 0.075 * squareLength));
                }
            }
        }
        g.setColor(boardGreen);
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                if (wPers) {
                    g.drawString(Character.toString((char) ('8' - i)),
                            (int) (20 + (0.075 * squareLength)),
                            (int) (20 + i * squareLength + 0.25 * squareLength));
                } else {
                    g.drawString(Character.toString((char) ('1' + i)),
                            (int) (20 + (0.075 * squareLength)),
                            (int) (20 + i * squareLength + 0.25 * squareLength));
                }
            }
        }
        g.setColor(boardWhite);
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 1) {
                if (wPers) {
                    g.drawString(Character.toString((char) ('8' - i)),
                            (int) (20 + (0.075 * squareLength)),
                            (int) (20 + i * squareLength + 0.25 * squareLength));
                } else {
                    g.drawString(Character.toString((char) ('1' + i)),
                            (int) (20 + (0.075 * squareLength)),
                            (int) (20 + i * squareLength + 0.25 * squareLength));
                }
            }
        }
    }

    public synchronized void resetBoard() {
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
        //resetting = true;
        pendingAction = true;
        handler.clearPieces();
        int[][] board = {{8, 6, 4, 10, 12, 4, 6, 8},
        {2, 2, 2, 2, 2, 2, 2, 2},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 1, 1, 1, 1},
        {7, 5, 3, 9, 11, 3, 5, 7}};
        chessBoard = board;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != 0) {
                    Piece temp = new Piece(i, j, board[i][j], this,
                            handler, handler.getSize());
//                    System.out.println(temp.getPosR() + " " + temp.getPosC()
//                            + " = " + temp.type);
                    handler.addObject(temp);
                }
            }
        }
        for (int i = 0; i < handler.pieces.size(); i++) {
            Piece temp = handler.pieces.get(i);
            handler.addMapValue(handler.generateKey(temp.getPosR(), temp.getPosC()), i);
        }
        handler.updateRenderPieces();
        whiteTurn = true;
        madeMoves = new ArrayList<>();
        properMoves = new ArrayList<>();
        bKCast = true;
        bQCast = true;
        wKCast = true;
        wQCast = true;
        handler.generateUnfilteredMoves();
        sortSideMoves();
        // Then you partition
        checkPartition();
        // Then you sort side moves again
        sortSideMoves();
        //resetting = false;
        pendingAction = false;
    }

    public synchronized void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        int cR = -1;
        int cC = -1;
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                if (mouseOver(mx, my, (int) (20 + col * squareLength),
                        (int) (20 + row * squareLength), (int) (squareLength),
                        (int) (squareLength))) {
                    if (wPers) {
                        cR = row;
                        cC = col;
                    } else {
                        cR = 7 - row;
                        cC = 7 - col;
                    }
                }
            }
        }
        //System.out.println(cR + " " + cC);
        if (clickedIndex == -1 && cR != -1) {
            for (int i = 0; i < handler.getSize(); i++) {
                if (handler.pieces.get(i).getPosR() == cR
                        && handler.pieces.get(i).getPosC() == cC
                        && (whiteTurn && handler.pieces.get(i).type % 2 == 1
                        || !whiteTurn && handler.pieces.get(i).type % 2 == 0)) {
                    handler.pieces.get(i).setClicked(true);
                    clickedIndex = i;
                } else {
                    handler.pieces.get(i).setClicked(false);
                }
            }
            handler.updateRenderPieces();
        } else if (cR != -1) {
            Piece tempObject = handler.pieces.get(clickedIndex);
            // Chech if valid move
            // Later on check if the player can move that piece
            // if it controls those pieces
            boolean valid = false;
            for (int i = 0; i < tempObject.validMoves.size(); i++) {
                if (tempObject.validMoves.get(i).row == cR
                        && tempObject.validMoves.get(i).col == cC) {
                    valid = true;
                }
            }
            if (tempObject.type % 2 == 0 && whiteTurn) {
                valid = false;
            } else if (tempObject.type % 2 == 1 && !whiteTurn) {
                valid = false;
            }
            if (playerType == 1) {
                if (wPers && !whiteTurn) {
                    valid = false;
                } else if (!wPers && whiteTurn) {
                    valid = false;
                }
            }
            if (selectEvo == true) {
                valid = false;
            }
            // Player can only move its pieces
            if (valid) {
                this.properMove(tempObject, cR, cC, -1, false);
                // Change -1 to autoswitch pawn evo type
            }

//            for(int i = 0; i < 8; i++){
//                for(int j = 0; j < 8; j++){
//                    if(handler.indexFinder.containsKey(handler.generateKey(i, j))){
//                        Piece temp = handler.pieces.get(
//                            handler.getPieceIndex(handler.generateKey(i, j)));
//                        System.out.print(temp.enPassant + "\t");
//                    } else {
//                        System.out.print("0\t");
//                    }
//                    
//                }
//                System.out.println();
//            }
            //System.out.println("Apparent move");
            tempObject.setClicked(false);
            clickedIndex = -1;
            handler.updateRenderPieces();
        } else {
            clickedIndex = -1;
            for (int i = 0; i < handler.getSize(); i++) {
                handler.pieces.get(i).setClicked(false);
            }
            handler.updateRenderPieces();
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        if (mx > x && mx < x + width) {
            if (my > y && my < y + height) {
                return true;
            }
        }
        return false;
    }

    public void tick() {

    }

    private void sortSideMoves() {
        whiteValidMoves = new ArrayList<>();
        blackValidMoves = new ArrayList<>();
        for (int i = 0; i < handler.pieces.size(); i++) {
            Piece temp = handler.pieces.get(i);
            if (temp.type % 2 == 1) {
                for (int j = 0; j < temp.validMoves.size(); j++) {
                    Pair dest = temp.validMoves.get(j);
                    whiteValidMoves.add(new Quad(temp.getPosR(),
                            temp.getPosC(), dest.row, dest.col));
                }
            } else {
                for (int j = 0; j < temp.validMoves.size(); j++) {
                    Pair dest = temp.validMoves.get(j);
                    blackValidMoves.add(new Quad(temp.getPosR(),
                            temp.getPosC(), dest.row, dest.col));
                }
            }
        }
    }

    public synchronized void makeNewLegalMoves() {
        handler.generateUnfilteredMoves();
        sortSideMoves();
        // Then you partition
        checkPartition();
        // Then you sort side moves again
        sortSideMoves();
    }

    // This is unfinished, currently works now
    public synchronized void undoProper(boolean instant) {
        Move move = properMoves.get(properMoves.size() - 1);

        Piece p = handler.pieces.get(handler.getPieceIndex(
                handler.generateKey(move.dRow, move.dCol)));

        p.move(move.sRow, move.sCol, instant, !instant);
        //System.out.println("Returned: " + chessBoard[move.sRow][move.sCol]);
        if (move.remPiece.getPosR() != -1) {
            chessBoard[move.dRow][move.dCol] = move.remPiece.type;
            handler.addObject(move.remPiece);
            move.remPiece.setHandlerIndex(handler.getSize() - 1);
            handler.addMapValue(handler.generateKey(move.remPiece.getPosR(),
                    move.remPiece.getPosC()), move.remPiece.getHandlerIndex());
        }
        if (move.oldCastLoc.row != -1) {
            Piece rook = handler.pieces.get(handler.getPieceIndex(
                    handler.generateKey(move.newCastLoc.row,
                            move.newCastLoc.col)));
            rook.firstMove = true;
            chessBoard[move.newCastLoc.row][move.newCastLoc.col] = 0;
            chessBoard[move.oldCastLoc.row][move.oldCastLoc.col] = rook.type;
            handler.removeMapValue(handler.generateKey(move.newCastLoc.row,
                    move.newCastLoc.col));
            handler.addMapValue(handler.generateKey(move.oldCastLoc.row,
                    move.oldCastLoc.col), rook.getHandlerIndex());
            rook.setPosR(move.oldCastLoc.row);
            rook.setPosC(move.oldCastLoc.col);
        }
        if (move.restorePassIndex != -1) {
            handler.pieces.get(move.restorePassIndex).enPassant = true;
            enPassantFile = handler.pieces.get(move.restorePassIndex).getPosC();
        }
        if (move.restoreFirstMove) {
            p.firstMove = true;
        }
        if (move.restoreSelfPassant) {
            p.enPassant = false;
        }
        if (move.evolutionSource > 0) {
            p.setType(move.evolutionSource, !instant);
        }
        if (move.restorebKCast) {
            bKCast = true;
        }
        if (move.restorebQCast) {
            bQCast = true;
        }
        if (move.restorewKCast) {
            wKCast = true;
        }
        if (move.restorewQCast) {
            wQCast = true;
        }
        whiteTurn = !whiteTurn;

        madeMoves.remove(madeMoves.size() - 1);
        properMoves.remove(properMoves.size() - 1);

        makeNewLegalMoves();
        if (!instant) {
            handler.updateRenderPieces();
        }
    }

    public synchronized void properMove(Piece tempObject, int dR, int dC, int autoEvoType, boolean instant) {
        pendingAction = true;
        whiteTurn = !whiteTurn;
        checked = false;
        Piece removePiece = new Piece(-1, -1, 1, this, handler, -1);
        for (int i = 0; i < handler.getSize(); i++) {
            Piece checkPiece = handler.pieces.get(i);
            if (checkPiece.getPosR() == dR && checkPiece.getPosC() == dC) {
                removePiece = checkPiece;
                break;
            } else if (checkPiece.type == 1 && tempObject.type == 2
                    && checkPiece.getEnPassant()
                    && checkPiece.getPosR() + 1 == dR
                    && checkPiece.getPosC() == dC) {
                removePiece = checkPiece;
                chessBoard[checkPiece.getPosR()][checkPiece.getPosC()] = 0;
                chessBoard[checkPiece.getPosR() + 1][checkPiece.getPosC()] = 0;
                break;
            } else if (checkPiece.type == 2 && tempObject.type == 1
                    && checkPiece.getEnPassant()
                    && checkPiece.getPosR() - 1 == dR
                    && checkPiece.getPosC() == dC) {
                removePiece = checkPiece;
                chessBoard[checkPiece.getPosR()][checkPiece.getPosC()] = 0;
                chessBoard[checkPiece.getPosR() - 1][checkPiece.getPosC()] = 0;
                break;
            }
        }
        // When castling both the king and rook will have to be moved
        // You should check the move if it is a castle and then find the 
        // rook that the king is targeting
        Pair oldCastLoc = new Pair(-1, -1);
        Pair newCastLoc = new Pair(-1, -1);
        if (tempObject.type == 11) {
            if (dC - tempObject.getPosC() == 2) {
                //Right
                Piece rook = handler.pieces.get(
                        handler.getPieceIndex(handler.generateKey(7, 7)));
                rook.move(dR, dC - 1, instant, !instant);
                oldCastLoc = new Pair(7, 7);
                newCastLoc = new Pair(dR, dC - 1);
            } else if (dC - tempObject.getPosC() == -2) {
                Piece rook = handler.pieces.get(
                        handler.getPieceIndex(handler.generateKey(7, 0)));
                rook.move(dR, dC + 1, instant, !instant);
                oldCastLoc = new Pair(7, 0);
                newCastLoc = new Pair(dR, dC + 1);
            }
        } else if (tempObject.type == 12) {
            if (dC - tempObject.getPosC() == 2) {
                //Right
                Piece rook = handler.pieces.get(
                        handler.getPieceIndex(handler.generateKey(0, 7)));
                rook.move(dR, dC - 1, instant, !instant);
                oldCastLoc = new Pair(0, 7);
                newCastLoc = new Pair(dR, dC - 1);
            } else if (dC - tempObject.getPosC() == -2) {
                Piece rook = handler.pieces.get(
                        handler.getPieceIndex(handler.generateKey(0, 0)));
                rook.move(dR, dC + 1, instant, !instant);
                oldCastLoc = new Pair(0, 0);
                newCastLoc = new Pair(dR, dC + 1);
            }
        }
        int sourceR = tempObject.getPosR();
        int sourceC = tempObject.getPosC();
        boolean isFirstMove = tempObject.firstMove;
        boolean restorePass = false;
        if (isFirstMove && Math.abs(dR - sourceR) == 2
                && (tempObject.type == 1 || tempObject.type == 2)) {
            restorePass = true;
        }

//        for(int i = 0; i < madeMoves.size(); i++){
//            System.out.println("Source: " + madeMoves.get(i).sRow +
//            " " + madeMoves.get(i).sCol + " Dest: " + madeMoves.get(i).dRow +
//            " " + madeMoves.get(i).dCol);
////             + " Rem: " + madeMoves.get(i).remPiece.type
////            + " CastLoc: " + madeMoves.get(i).castleLoc.row + " " + 
////                    madeMoves.get(i).castleLoc.col
//        }
//        System.out.println();
        tempObject.move(dR, dC, instant, !instant);
        if (removePiece != null) {
            handler.removeObject(removePiece);
        }

        int restorePassIndex = -1;
        // Remove en passant for the current turns team
        for (int i = 0; i < handler.pieces.size(); i++) {
            if (whiteTurn && handler.pieces.get(i).type == 1) {
                if (handler.pieces.get(i).enPassant) {
                    restorePassIndex = i;
                }
                handler.pieces.get(i).checkPassant();
            } else if (!whiteTurn && handler.pieces.get(i).type == 2) {
                if (handler.pieces.get(i).enPassant) {
                    restorePassIndex = i;
                }
                handler.pieces.get(i).checkPassant();
            }
        }
        if (tempObject.type == 1 && dR == 0) {
            // Set the turn back to whoever made the move
            if (!instant) {
                selectEvo = true;
            }
            evoPiece = tempObject;
            evoMove = new Move(sourceR, sourceC,
                    dR, dC, 0.0, removePiece, oldCastLoc, newCastLoc,
                    restorePassIndex, isFirstMove, restorePass, 1);
            madeMoves.add(new Move(sourceR, sourceC,
                    dR, dC, 0.0, removePiece, oldCastLoc, newCastLoc,
                    restorePassIndex, isFirstMove, restorePass, 1));
            properMoves.add(new Move(sourceR, sourceC,
                    dR, dC, 0.0, removePiece, oldCastLoc, newCastLoc,
                    restorePassIndex, isFirstMove, restorePass, 1));
            if (autoEvoType > 0) {
                setEvoType(autoEvoType, true, instant);
            }
        } else if (tempObject.type == 2 && dR == 7) {
            // Set the turn back to whoever made the move
            if (!instant) {
                selectEvo = true;
            }
            evoPiece = tempObject;
            evoMove = new Move(sourceR, sourceC,
                    dR, dC, 0.0, removePiece, oldCastLoc, newCastLoc,
                    restorePassIndex, isFirstMove, restorePass, 2);
            madeMoves.add(new Move(sourceR, sourceC,
                    dR, dC, 0.0, removePiece, oldCastLoc, newCastLoc,
                    restorePassIndex, isFirstMove, restorePass, 2));
            properMoves.add(new Move(sourceR, sourceC,
                    dR, dC, 0.0, removePiece, oldCastLoc, newCastLoc,
                    restorePassIndex, isFirstMove, restorePass, 2));
            if (autoEvoType > 0) {
                setEvoType(autoEvoType, true, instant);
            }
        } else {
            madeMoves.add(new Move(sourceR, sourceC,
                    dR, dC, 0.0, removePiece, oldCastLoc, newCastLoc,
                    restorePassIndex, isFirstMove, restorePass, -1));
            properMoves.add(new Move(sourceR, sourceC,
                    dR, dC, 0.0, removePiece, oldCastLoc, newCastLoc,
                    restorePassIndex, isFirstMove, restorePass, -1));

            // Important creation of new legal moves after
            makeNewLegalMoves();

            if (!instant) {
                if (whiteValidMoves.isEmpty() && checked && whiteTurn) {
                    endResult = 0;
                    System.out.println("Checkmate: Black Wins");
                } else if (blackValidMoves.isEmpty() && checked && !whiteTurn) {
                    endResult = 1;
                    System.out.println("Checkmate: White Wins");
                } else if (blackValidMoves.isEmpty()
                        || whiteValidMoves.isEmpty()) {
                    System.out.println("Stalemate");
                    endResult = 2;
                }
            }

            // Start of castle checking
            bKCast =  false;
            bQCast =  false;
            wKCast =  false;
            wQCast =  false;
            for(int i = 0; i < whiteValidMoves.size(); i++){
                Quad move = whiteValidMoves.get(i);
                if(chessBoard[move.sRow][move.sCol] == 11){
                    if(move.dCol - move.sCol == 2){
                        wKCast = true;
                    } else if(move.dCol - move.sCol == -2){
                        wQCast = true;
                    }
                }
            }
            for(int i = 0; i < blackValidMoves.size(); i++){
                Quad move = blackValidMoves.get(i);
                if(chessBoard[move.sRow][move.sCol] == 12){
                    if(move.dCol - move.sCol == 2){
                        bKCast = true;
                    } else if(move.dCol - move.sCol == -2){
                        bQCast = true;
                    }
                }
            }
            // End of castle checking

        }
        if (!instant) {
            handler.updateRenderPieces();
        }
        ZobristKey = zob.CalculateZobristKey();
        pendingAction = false;
    }

    public void setEvoType(int evoType, boolean isProper, boolean instant) {
        if (evoType > 0) {
            pendingAction = true;
            int originalType = evoPiece.type;

            evoPiece.setType(evoType, !instant);
            selectEvo = false;
            evoPiece = new Piece(-1, -1, -1, this, handler, -1);
            this.evoType = -1;
            madeMoves.remove(madeMoves.size() - 1);
            properMoves.remove(properMoves.size() - 1);
            madeMoves.add(new Move(evoMove.sRow, evoMove.sCol,
                    evoMove.dRow, evoMove.dCol, 0.0, evoMove.remPiece,
                    evoMove.oldCastLoc, evoMove.newCastLoc,
                    evoMove.restorePassIndex,
                    evoMove.restoreFirstMove, evoMove.restoreSelfPassant,
                    originalType));
            if (isProper) {
                properMoves.add(new Move(evoMove.sRow, evoMove.sCol,
                        evoMove.dRow, evoMove.dCol, 0.0, evoMove.remPiece,
                        evoMove.oldCastLoc, evoMove.newCastLoc,
                        evoMove.restorePassIndex, evoMove.restoreFirstMove,
                        evoMove.restoreSelfPassant, originalType));
            }
            evoMove = new Move(-1, -1, -1, -1,
                    0.0, new Piece(-1, -1, 1, this, handler, -1),
                    new Pair(-1, -1), new Pair(-1, -1), -1, false, false, -1);

            makeNewLegalMoves();
            if (!instant) {
                handler.updateRenderPieces();
            }
            if (!instant) {
                if (whiteValidMoves.isEmpty() && checked) {
                    endResult = 0;
                    System.out.println("Checkmate: Black Wins");
                } else if (blackValidMoves.isEmpty() && checked) {
                    endResult = 1;
                    System.out.println("Checkmate: White Wins");
                } else if (blackValidMoves.isEmpty()
                        || whiteValidMoves.isEmpty()) {
                    System.out.println("Stalemate");
                    endResult = 2;
                }
            }
            pendingAction = false;
        }
    }

    public synchronized void checkPartition() {
        // White
        // Command F White Key
        // Piece cannot move to then check king
        // Only care about bishop, rook, queen

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
        for (int i = 0; i < handler.pieces.size(); i++) {
            Piece tempObject = handler.pieces.get(i);
            boolean blocksCheck = false;
            int blockerIndex = -1;
            Pair passantExpose = new Pair(-1, -1);
            ArrayList<Pair> stillBlockMoves = new ArrayList<>();
            stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
            if (tempObject.type == 4) {
                int curR = tempObject.getPosR();
                int curC = tempObject.getPosC();
                while (curR > 0 && curC > 0) {
                    curR--;
                    curC--;
                    stillBlockMoves.add(new Pair(curR, curC));
                    if (chessBoard[curR][curC] == 11) {
                        blocksCheck = true;
                        break;
                    } else if (chessBoard[curR][curC] % 2 == 1
                            && blockerIndex == -1) {
                        if (handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            blockerIndex = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                        } else {
                            System.out.print("Null Pointer Error: ");
                            System.out.println(handler.generateKey(curR, curC));
                            for (int m = 0; m < chessBoard.length; m++) {
                                for (int n = 0; n < chessBoard.length; n++) {
                                    System.out.print(chessBoard[m][n] + "\t");
                                }
                                System.out.println();
                            }
                            for (String s : handler.indexFinder.keySet()) {
                                System.out.println(s + " : " + handler.indexFinder.get(s));
                            }
                        }

                    } else if (chessBoard[curR][curC] % 2 == 1
                            && blockerIndex > 0) {
                        blockerIndex = -2;
                    } else if (chessBoard[curR][curC] % 2 == 0
                            && chessBoard[curR][curC] != 0) {
                        // Blacks bishop is blocked by its
                        // own piece
                        if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            int possiblePass = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                            if (handler.pieces.get(possiblePass).enPassant) {
                                passantExpose = new Pair(curR - 1, curC);
                            }
                        } else {
                            break;
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR > 0 && curC < chessBoard.length - 1) {
                        curR--;
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }

                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1 && curC > 0) {
                        curR++;
                        curC--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }

                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1
                            && curC < chessBoard.length - 1) {
                        curR++;
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

            } else if (tempObject.type == 8) {
                // Rooks
                int curR = tempObject.getPosR();
                int curC = tempObject.getPosC();
                while (curR > 0) {
                    curR--;
                    stillBlockMoves.add(new Pair(curR, curC));
                    if (chessBoard[curR][curC] == 11) {
                        blocksCheck = true;
                        break;
                    } else if (chessBoard[curR][curC] % 2 == 1
                            && blockerIndex == -1) {
                        if (handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            blockerIndex = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                        } else {
                            System.out.print("Null Pointer Error: ");
                            System.out.println(handler.generateKey(curR, curC));
                            for (int m = 0; m < chessBoard.length; m++) {
                                for (int n = 0; n < chessBoard.length; n++) {
                                    System.out.print(chessBoard[m][n] + "\t");
                                }
                                System.out.println();
                            }
                            for (String s : handler.indexFinder.keySet()) {
                                System.out.println(s + " : " + handler.indexFinder.get(s));
                            }
                        }
                    } else if (chessBoard[curR][curC] % 2 == 1
                            && blockerIndex > 0) {
                        blockerIndex = -2;
                    } else if (chessBoard[curR][curC] % 2 == 0
                            && chessBoard[curR][curC] != 0) {
                        // Blacks rook is blocked by its own piece
                        if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            int possiblePass = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                            if (handler.pieces.get(possiblePass).enPassant) {
                                passantExpose = new Pair(curR - 1, curC);
                            }
                        } else {
                            break;
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curC < chessBoard.length - 1) {
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1) {
                        curR++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curC > 0) {
                        curC--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            } else if (tempObject.type == 10) {
                // Queem
                int curR = tempObject.getPosR();
                int curC = tempObject.getPosC();
                while (curR > 0 && curC > 0) {
                    curR--;
                    curC--;
                    stillBlockMoves.add(new Pair(curR, curC));
                    if (chessBoard[curR][curC] == 11) {
                        blocksCheck = true;
                        break;
                    } else if (chessBoard[curR][curC] % 2 == 1
                            && blockerIndex == -1) {
                        if (handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            blockerIndex = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                        } else {
                            System.out.print("Null Pointer Error: ");
                            System.out.println(handler.generateKey(curR, curC));
                            for (int m = 0; m < chessBoard.length; m++) {
                                for (int n = 0; n < chessBoard.length; n++) {
                                    System.out.print(chessBoard[m][n] + "\t");
                                }
                                System.out.println();
                            }
                            for (String s : handler.indexFinder.keySet()) {
                                System.out.println(s + " : " + handler.indexFinder.get(s));
                            }
                        }
                    } else if (chessBoard[curR][curC] % 2 == 1
                            && blockerIndex > 0) {
                        blockerIndex = -2;
                    } else if (chessBoard[curR][curC] % 2 == 0
                            && chessBoard[curR][curC] != 0) {
                        // Blacks queen is blocked by its own piece
                        if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            int possiblePass = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                            if (handler.pieces.get(possiblePass).enPassant) {
                                passantExpose = new Pair(curR - 1, curC);
                            }
                        } else {
                            break;
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR > 0 && curC < chessBoard.length - 1) {
                        curR--;
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1 && curC > 0) {
                        curR++;
                        curC--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1
                            && curC < chessBoard.length - 1) {
                        curR++;
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    while (curR > 0) {
                        curR--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curC < chessBoard.length - 1) {
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1) {
                        curR++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curC > 0) {
                        curC--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 11) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 1
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0) {
                            if (chessBoard[curR][curC] == 2 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR - 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            // Blocker's moves become limited
            if (blocksCheck && blockerIndex >= 0 && passantExpose.row == -1) {
                Piece blocker = handler.pieces.get(blockerIndex);
                ArrayList<Pair> legalMoves = new ArrayList<>();
                for (int j = 0; j < blocker.validMoves.size(); j++) {
                    boolean legal = false;
                    for (int k = 0; k < stillBlockMoves.size(); k++) {
                        if (blocker.validMoves.get(j).row == stillBlockMoves.get(k).row
                                && blocker.validMoves.get(j).col == stillBlockMoves.get(k).col) {
                            legal = true;
                        }
                    }
                    if (legal) {
                        legalMoves.add(blocker.validMoves.get(j));
                    }
                }
                blocker.validMoves = legalMoves;
            }
            // Blocks en passant moves that put the king into check
            if (blocksCheck && (blockerIndex >= -1) && passantExpose.row != -1) {
                if (blockerIndex == -1) {
                    for (int j = 0; j < handler.getSize(); j++) {
                        Piece temp = handler.pieces.get(j);
                        if (temp.type == 1) {
                            for (int k = 0; k < temp.validMoves.size(); k++) {
                                if (temp.validMoves.get(k).row == passantExpose.row
                                        && temp.validMoves.get(k).col == passantExpose.col) {
                                    temp.validMoves.remove(k);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    Piece blocker = handler.pieces.get(blockerIndex);
                    if (blocker.type == 1) {
                        for (int k = 0; k < blocker.validMoves.size(); k++) {
                            if (blocker.validMoves.get(k).row == passantExpose.row
                                    && blocker.validMoves.get(k).col == passantExpose.col) {
                                blocker.validMoves.remove(k);
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Check if the king is in check and remove moves that
        // don't put the king out of check, however another check will
        // be required later to make sure that the king doesn't move into
        // check again
        Piece king = new Piece(-1, -1, -1, this, handler, -1);
        for (int i = 0; i < handler.pieces.size(); i++) {
            if (handler.pieces.get(i).type == 11) {
                king = handler.pieces.get(i);
            }
        }
        int checkerPieceIndex = -1;
        boolean doubleCheck = false;
        for (int i = 0; i < blackValidMoves.size(); i++) {
            Quad move = blackValidMoves.get(i);
            if (move.dRow == king.getPosR() && move.dCol == king.getPosC()) {
                if (checkerPieceIndex >= 0) {
                    doubleCheck = true;
                }
                checkerPieceIndex = handler.getPieceIndex(
                        handler.generateKey(move.sRow, move.sCol));
            }
        }

        // Means that the king is checked
        if (checkerPieceIndex >= 0) {
            checked = true;
            Piece checker = handler.pieces.get(checkerPieceIndex);
            ArrayList<Pair> notKingMoves = new ArrayList<>();
            notKingMoves.add(new Pair(checker.getPosR(),
                    checker.getPosC()));
            if (checker.type == 4) {
                int distR = king.getPosR() - checker.getPosR();
                int distC = king.getPosC() - checker.getPosC();
                int curR = checker.getPosR();
                int curC = checker.getPosC();
                if (distR > 0 && distC > 0) {
                    while (curR < king.getPosR() && curC < king.getPosC()) {
                        curR++;
                        curC++;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distR > 0 && distC < 0) {
                    while (curR < king.getPosR() && curC > king.getPosC()) {
                        curR++;
                        curC--;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distR < 0 && distC > 0) {
                    while (curR > king.getPosR() && curC < king.getPosC()) {
                        curR--;
                        curC++;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distR < 0 && distC < 0) {
                    while (curR > king.getPosR() && curC > king.getPosC()) {
                        curR--;
                        curC--;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                }
            } else if (checker.type == 8) {
                int distR = king.getPosR() - checker.getPosR();
                int distC = king.getPosC() - checker.getPosC();
                int curR = checker.getPosR();
                int curC = checker.getPosC();
                if (distR > 0) {
                    while (curR < king.getPosR()) {
                        curR++;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distR < 0) {
                    while (curR > king.getPosR()) {
                        curR--;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distC > 0) {
                    while (curC < king.getPosC()) {
                        curC++;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distC < 0) {
                    while (curC > king.getPosC()) {
                        curC--;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                }
            } else if (checker.type == 10) {
                int distR = king.getPosR() - checker.getPosR();
                int distC = king.getPosC() - checker.getPosC();
                int curR = checker.getPosR();
                int curC = checker.getPosC();
                if (distR != 0 && distC != 0) {
                    if (distR > 0 && distC > 0) {
                        while (curR < king.getPosR() && curC < king.getPosC()) {
                            curR++;
                            curC++;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distR > 0 && distC < 0) {
                        while (curR < king.getPosR() && curC > king.getPosC()) {
                            curR++;
                            curC--;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distR < 0 && distC > 0) {
                        while (curR > king.getPosR() && curC < king.getPosC()) {
                            curR--;
                            curC++;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distR < 0 && distC < 0) {
                        while (curR > king.getPosR() && curC > king.getPosC()) {
                            curR--;
                            curC--;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    }
                } else {
                    if (distR > 0) {
                        while (curR < king.getPosR()) {
                            curR++;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distR < 0) {
                        while (curR > king.getPosR()) {
                            curR--;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distC > 0) {
                        while (curC < king.getPosC()) {
                            curC++;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distC < 0) {
                        while (curC > king.getPosC()) {
                            curC--;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    }
                }
            }
            // Removal of all moves that do not stop the check
            //ArrayList<Quad> newValid = new ArrayList<>();
            for (int i = 0; i < whiteValidMoves.size(); i++) {
                Quad move = whiteValidMoves.get(i);
                boolean valid = false;
                for (int j = 0; j < notKingMoves.size(); j++) {
                    if (move.dRow == notKingMoves.get(j).row
                            && move.dCol == notKingMoves.get(j).col
                            && !doubleCheck) {
                        valid = true;
                        break;
                    }
                }
                if (handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(move.sRow, move.sCol))).type != 11
                        && !valid) {
                    Piece temp = handler.pieces.get(handler.getPieceIndex(
                            handler.generateKey(move.sRow, move.sCol)));
                    for (int j = 0; j < temp.validMoves.size(); j++) {
                        if (temp.validMoves.get(j).row == move.dRow
                                && temp.validMoves.get(j).col == move.dCol) {
                            temp.validMoves.remove(j);
                            break;
                        }
                    }
                }
            }
            // Instead of entire you are supposed to do for each piece
            // Then we will resort them after generation and paritition
        }

        // Removal of moves that make the king go into check
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
        ArrayList<Pair> dangers = new ArrayList<>();
        MoveGenerator mg = new MoveGenerator(handler, this);
        for (int i = 0; i < handler.pieces.size(); i++) {
            ArrayList<Pair> pieceCap = new ArrayList<>();
            switch (handler.pieces.get(i).type) {
                case 2:
                    pieceCap = mg.BlackPawnCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 4:
                    pieceCap = mg.BlackBishopCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 6:
                    pieceCap = mg.BlackHorseCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 8:
                    pieceCap = mg.BlackRookCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 10:
                    pieceCap = mg.BlackQueenCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 12:
                    pieceCap = mg.BlackKingCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                default:
                    break;
            }
            for (int j = 0; j < pieceCap.size(); j++) {
                dangers.add(pieceCap.get(j));
            }
        }

        ArrayList<Pair> newKingValid = new ArrayList<>();
        if (king.validMoves != null) {
            for (int i = 0; i < king.validMoves.size(); i++) {
                Pair kingMove = king.validMoves.get(i);
                boolean valid = true;
                if (Math.abs(kingMove.col - king.getPosC()) == 2) {
                    // Castle
                    if (checkerPieceIndex >= 0) {
                        valid = false;
                    }
                    int increment = 1;
                    if (king.getPosC() - kingMove.col > 0) {
                        increment = -1;
                    }
                    for (int j = 0; j < dangers.size() && valid; j++) {
                        if ((dangers.get(j).row == kingMove.row
                                && dangers.get(j).col == kingMove.col)
                                || (dangers.get(j).row == kingMove.row
                                && dangers.get(j).col == kingMove.col - increment)) {
//                    System.out.println("Black move can take");
//                    System.out.println(blackValidMoves.get(j).dRow + " " +
//                            blackValidMoves.get(j).dCol);
                            valid = false;
                            break;
                        }
                    }
                } else {
                    for (int j = 0; j < dangers.size(); j++) {
                        if (dangers.get(j).row == kingMove.row
                                && dangers.get(j).col == kingMove.col) {
//                    System.out.println("Black move can take");
//                    System.out.println(blackValidMoves.get(j).dRow + " " +
//                            blackValidMoves.get(j).dCol);
                            valid = false;
                            break;
                        }
                    }
                }
                if (valid) {
                    newKingValid.add(kingMove);
                }
            }
        }
        king.validMoves = newKingValid;

        // Black
        // Command F Black Key
        for (int i = 0; i < handler.pieces.size(); i++) {
            Piece tempObject = handler.pieces.get(i);
            boolean blocksCheck = false;
            int blockerIndex = -1;
            Pair passantExpose = new Pair(-1, -1);
            ArrayList<Pair> stillBlockMoves = new ArrayList<>();
            stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
            if (tempObject.type == 3) {
                // Bishop
                int curR = tempObject.getPosR();
                int curC = tempObject.getPosC();
                while (curR > 0 && curC > 0) {
                    curR--;
                    curC--;
                    stillBlockMoves.add(new Pair(curR, curC));
                    if (chessBoard[curR][curC] == 12) {
                        blocksCheck = true;
                        break;
                    } else if (chessBoard[curR][curC] % 2 == 0 && chessBoard[curR][curC] != 0
                            && blockerIndex == -1) {
                        if (handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            blockerIndex = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                        } else {
                            System.out.print("Null Pointer Error: ");
                            System.out.println(handler.generateKey(curR, curC));
                            for (int m = 0; m < chessBoard.length; m++) {
                                for (int n = 0; n < chessBoard.length; n++) {
                                    System.out.print(chessBoard[m][n] + "\t");
                                }
                                System.out.println();
                            }
                            for (String s : handler.indexFinder.keySet()) {
                                System.out.println(s + " : " + handler.indexFinder.get(s));
                            }
                        }
                    } else if (chessBoard[curR][curC] % 2 == 0
                            && chessBoard[curR][curC] != 0
                            && blockerIndex > 0) {
                        blockerIndex = -2;
                    } else if (chessBoard[curR][curC] % 2 == 1) {
                        // White's bishop is blocked by its own piece
                        if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            int possiblePass = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                            if (handler.pieces.get(possiblePass).enPassant) {
                                passantExpose = new Pair(curR + 1, curC);
                            }
                        } else {
                            break;
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR > 0 && curC < chessBoard.length - 1) {
                        curR--;
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1 && curC > 0) {
                        curR++;
                        curC--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1
                            && curC < chessBoard.length - 1) {
                        curR++;
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

            } else if (tempObject.type == 7) {
                // Rooks
                int curR = tempObject.getPosR();
                int curC = tempObject.getPosC();
                while (curR > 0) {
                    curR--;
                    stillBlockMoves.add(new Pair(curR, curC));
                    if (chessBoard[curR][curC] == 12) {
                        blocksCheck = true;
                        break;
                    } else if (chessBoard[curR][curC] % 2 == 0
                            && chessBoard[curR][curC] != 0
                            && blockerIndex == -1) {
                        if (handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            blockerIndex = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                        } else {
                            System.out.print("Null Pointer Error: ");
                            System.out.println(handler.generateKey(curR, curC));
                            for (int m = 0; m < chessBoard.length; m++) {
                                for (int n = 0; n < chessBoard.length; n++) {
                                    System.out.print(chessBoard[m][n] + "\t");
                                }
                                System.out.println();
                            }
                            for (String s : handler.indexFinder.keySet()) {
                                System.out.println(s + " : " + handler.indexFinder.get(s));
                            }
                        }
                    } else if (chessBoard[curR][curC] % 2 == 0
                            && chessBoard[curR][curC] != 0
                            && blockerIndex > 0) {
                        blockerIndex = -2;
                    } else if (chessBoard[curR][curC] % 2 == 1) {
                        // White's rooks are blocked by its own piece
                        if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            int possiblePass = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                            if (handler.pieces.get(possiblePass).enPassant) {
                                passantExpose = new Pair(curR + 1, curC);
                            }
                        } else {
                            break;
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curC < chessBoard.length - 1) {
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1) {
                        curR++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curC > 0) {
                        curC--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            } else if (tempObject.type == 9) {
                // Queem
                int curR = tempObject.getPosR();
                int curC = tempObject.getPosC();
                while (curR > 0 && curC > 0) {
                    curR--;
                    curC--;
                    stillBlockMoves.add(new Pair(curR, curC));
                    if (chessBoard[curR][curC] == 12) {
                        blocksCheck = true;
                        break;
                    } else if (chessBoard[curR][curC] % 2 == 0
                            && chessBoard[curR][curC] != 0
                            && blockerIndex == -1) {
                        if (handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            blockerIndex = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                        } else {
                            System.out.print("Null Pointer Error: ");
                            System.out.println(handler.generateKey(curR, curC));
                            for (int m = 0; m < chessBoard.length; m++) {
                                for (int n = 0; n < chessBoard.length; n++) {
                                    System.out.print(chessBoard[m][n] + "\t");
                                }
                                System.out.println();
                            }
                            for (String s : handler.indexFinder.keySet()) {
                                System.out.println(s + " : " + handler.indexFinder.get(s));
                            }
                        }
                    } else if (chessBoard[curR][curC] % 2 == 0
                            && chessBoard[curR][curC] != 0
                            && blockerIndex > 0) {
                        blockerIndex = -2;
                    } else if (chessBoard[curR][curC] % 2 == 1) {
                        // White's queen is blocked by its own piece
                        if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                handler.generateKey(curR, curC))) {
                            int possiblePass = handler.getPieceIndex(
                                    handler.generateKey(curR, curC));
                            if (handler.pieces.get(possiblePass).enPassant) {
                                passantExpose = new Pair(curR + 1, curC);
                            }
                        } else {
                            break;
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR > 0 && curC < chessBoard.length - 1) {
                        curR--;
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1 && curC > 0) {
                        curR++;
                        curC--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1
                            && curC < chessBoard.length - 1) {
                        curR++;
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    while (curR > 0) {
                        curR--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curC < chessBoard.length - 1) {
                        curC++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curR < chessBoard.length - 1) {
                        curR++;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (!blocksCheck) {
                    blockerIndex = -1;
                    curR = tempObject.getPosR();
                    curC = tempObject.getPosC();
                    stillBlockMoves = new ArrayList<>();
                    stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
                    while (curC > 0) {
                        curC--;
                        stillBlockMoves.add(new Pair(curR, curC));
                        if (chessBoard[curR][curC] == 12) {
                            blocksCheck = true;
                            break;
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex == -1) {
                            if (handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                blockerIndex = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                            } else {
                                System.out.print("Null Pointer Error: ");
                                System.out.println(handler.generateKey(curR, curC));
                                for (int m = 0; m < chessBoard.length; m++) {
                                    for (int n = 0; n < chessBoard.length; n++) {
                                        System.out.print(chessBoard[m][n] + "\t");
                                    }
                                    System.out.println();
                                }
                                for (String s : handler.indexFinder.keySet()) {
                                    System.out.println(s + " : " + handler.indexFinder.get(s));
                                }
                            }
                        } else if (chessBoard[curR][curC] % 2 == 0
                                && chessBoard[curR][curC] != 0
                                && blockerIndex > 0) {
                            blockerIndex = -2;
                        } else if (chessBoard[curR][curC] % 2 == 1) {
                            if (chessBoard[curR][curC] == 1 && handler.indexFinder.containsKey(
                                    handler.generateKey(curR, curC))) {
                                int possiblePass = handler.getPieceIndex(
                                        handler.generateKey(curR, curC));
                                if (handler.pieces.get(possiblePass).enPassant) {
                                    passantExpose = new Pair(curR + 1, curC);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

            }
            // Blocker's moves become limited
            if (blocksCheck && blockerIndex >= 0 && passantExpose.row == -1) {
                Piece blocker = handler.pieces.get(blockerIndex);
                ArrayList<Pair> legalMoves = new ArrayList<>();
                for (int j = 0; j < blocker.validMoves.size(); j++) {
                    boolean legal = false;
                    for (int k = 0; k < stillBlockMoves.size(); k++) {
                        if (blocker.validMoves.get(j).row == stillBlockMoves.get(k).row
                                && blocker.validMoves.get(j).col == stillBlockMoves.get(k).col) {
                            legal = true;
                        }
                    }
                    if (legal) {
                        legalMoves.add(blocker.validMoves.get(j));
                    }
                }
                blocker.validMoves = legalMoves;
            }
            // Blocks en passant moves that put the king into check
            if (blocksCheck && (blockerIndex >= -1) && passantExpose.row != -1) {
                if (blockerIndex == -1) {
                    for (int j = 0; j < handler.getSize(); j++) {
                        Piece temp = handler.pieces.get(j);
                        if (temp.type == 2) {
                            for (int k = 0; k < temp.validMoves.size(); k++) {
                                if (temp.validMoves.get(k).row == passantExpose.row
                                        && temp.validMoves.get(k).col == passantExpose.col) {
                                    temp.validMoves.remove(k);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    Piece blocker = handler.pieces.get(blockerIndex);
                    if (blocker.type == 2) {
                        for (int k = 0; k < blocker.validMoves.size(); k++) {
                            if (blocker.validMoves.get(k).row == passantExpose.row
                                    && blocker.validMoves.get(k).col == passantExpose.col) {
                                blocker.validMoves.remove(k);
                                break;
                            }
                        }
                    }
                }
            }
        }
        king = new Piece(-1, -1, -1, this, handler, -1);
        for (int i = 0; i < handler.pieces.size(); i++) {
            if (handler.pieces.get(i).type == 12) {
                king = handler.pieces.get(i);
                break;
            }
        }
        //System.out.println(king.validMoves.size());
        checkerPieceIndex = -1;
        doubleCheck = false;
        for (int i = 0; i < whiteValidMoves.size(); i++) {
            Quad move = whiteValidMoves.get(i);
            if (move.dRow == king.getPosR() && move.dCol == king.getPosC()) {
                if (checkerPieceIndex >= 0) {
                    doubleCheck = true;
                }
                checkerPieceIndex = handler.getPieceIndex(
                        handler.generateKey(move.sRow, move.sCol));
            }
        }

        // Means that the king is checked
        if (checkerPieceIndex >= 0) {
            checked = true;
            //System.out.println("Registers");
            Piece checker = handler.pieces.get(checkerPieceIndex);
            ArrayList<Pair> notKingMoves = new ArrayList<>();
            notKingMoves.add(new Pair(checker.getPosR(),
                    checker.getPosC()));
            if (checker.type == 3) {
                int distR = king.getPosR() - checker.getPosR();
                int distC = king.getPosC() - checker.getPosC();
                int curR = checker.getPosR();
                int curC = checker.getPosC();
                if (distR > 0 && distC > 0) {
                    while (curR < king.getPosR() && curC < king.getPosC()) {
                        curR++;
                        curC++;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distR > 0 && distC < 0) {
                    while (curR < king.getPosR() && curC > king.getPosC()) {
                        curR++;
                        curC--;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distR < 0 && distC > 0) {
                    while (curR > king.getPosR() && curC < king.getPosC()) {
                        curR--;
                        curC++;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distR < 0 && distC < 0) {
                    while (curR > king.getPosR() && curC > king.getPosC()) {
                        curR--;
                        curC--;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                }
            } else if (checker.type == 7) {
                int distR = king.getPosR() - checker.getPosR();
                int distC = king.getPosC() - checker.getPosC();
                int curR = checker.getPosR();
                int curC = checker.getPosC();
                if (distR > 0) {
                    while (curR < king.getPosR()) {
                        curR++;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distR < 0) {
                    while (curR > king.getPosR()) {
                        curR--;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distC > 0) {
                    while (curC < king.getPosC()) {
                        curC++;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                } else if (distC < 0) {
                    while (curC > king.getPosC()) {
                        curC--;
                        notKingMoves.add(new Pair(curR, curC));
                    }
                }
            } else if (checker.type == 9) {
                int distR = king.getPosR() - checker.getPosR();
                int distC = king.getPosC() - checker.getPosC();
                int curR = checker.getPosR();
                int curC = checker.getPosC();
                if (distR != 0 && distC != 0) {
                    if (distR > 0 && distC > 0) {
                        while (curR < king.getPosR() && curC < king.getPosC()) {
                            curR++;
                            curC++;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distR > 0 && distC < 0) {
                        while (curR < king.getPosR() && curC > king.getPosC()) {
                            curR++;
                            curC--;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distR < 0 && distC > 0) {
                        while (curR > king.getPosR() && curC < king.getPosC()) {
                            curR--;
                            curC++;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distR < 0 && distC < 0) {
                        while (curR > king.getPosR() && curC > king.getPosC()) {
                            curR--;
                            curC--;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    }
                } else {
                    if (distR > 0) {
                        while (curR < king.getPosR()) {
                            curR++;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distR < 0) {
                        while (curR > king.getPosR()) {
                            curR--;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distC > 0) {
                        while (curC < king.getPosC()) {
                            curC++;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    } else if (distC < 0) {
                        while (curC > king.getPosC()) {
                            curC--;
                            notKingMoves.add(new Pair(curR, curC));
                        }
                    }
                }
            }
            // Removal of all moves that do not stop the check
            //ArrayList<Quad> newValid = new ArrayList<>();
            //System.out.println("Before: " + king.validMoves.size());
            for (int i = 0; i < blackValidMoves.size(); i++) {
                Quad move = blackValidMoves.get(i);
                boolean valid = false;
                for (int j = 0; j < notKingMoves.size(); j++) {
                    if (move.dRow == notKingMoves.get(j).row
                            && move.dCol == notKingMoves.get(j).col
                            && !doubleCheck) {
                        valid = true;
                        break;
                    }
                }
                if (handler.pieces.get(handler.getPieceIndex(
                        handler.generateKey(move.sRow, move.sCol))).type != 12
                        && !valid) {
                    Piece temp = handler.pieces.get(handler.getPieceIndex(
                            handler.generateKey(move.sRow, move.sCol)));
                    //System.out.println(temp.type);
                    for (int j = 0; j < temp.validMoves.size(); j++) {
                        if (temp.validMoves.get(j).row == move.dRow
                                && temp.validMoves.get(j).col == move.dCol) {
                            temp.validMoves.remove(j);
                            break;
                        }
                    }
                }
            }
            //System.out.println("After: " + king.validMoves.size());
            // Instead of entire you are supposed to do for each piece
            // Then we will resort them after generation and paritition
        }

        // Removal of moves that make the king go into check
        dangers = new ArrayList<>();
        for (int i = 0; i < handler.pieces.size(); i++) {
            ArrayList<Pair> pieceCap = new ArrayList<>();
            switch (handler.pieces.get(i).type) {
                case 1:
                    pieceCap = mg.WhitePawnCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 3:
                    pieceCap = mg.WhiteBishopCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 5:
                    pieceCap = mg.WhiteHorseCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 7:
                    pieceCap = mg.WhiteRookCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 9:
                    pieceCap = mg.WhiteQueenCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                case 11:
                    pieceCap = mg.WhiteKingCover(
                            handler.pieces.get(i).getPosR(),
                            handler.pieces.get(i).getPosC());
                    break;
                default:
                    break;
            }
            for (int j = 0; j < pieceCap.size(); j++) {
                dangers.add(pieceCap.get(j));
            }
        }

        newKingValid = new ArrayList<>();

        if (king.validMoves != null) {
            for (int i = 0; i < king.validMoves.size(); i++) {
                Pair kingMove = king.validMoves.get(i);
                boolean valid = true;
                if (Math.abs(kingMove.col - king.getPosC()) == 2) {
                    // Castle
                    if (checkerPieceIndex >= 0) {
                        valid = false;
                    }
                    int increment = 1;
                    if (king.getPosC() - kingMove.col > 0) {
                        increment = -1;
                    }
                    for (int j = 0; j < dangers.size() && valid; j++) {
                        if ((dangers.get(j).row == kingMove.row
                                && dangers.get(j).col == kingMove.col)
                                || (dangers.get(j).row == kingMove.row
                                && dangers.get(j).col == kingMove.col - increment)) {
//                    System.out.println("Black move can take");
//                    System.out.println(blackValidMoves.get(j).dRow + " " +
//                            blackValidMoves.get(j).dCol);
                            valid = false;
                            break;
                        }
                    }
                } else {
                    for (int j = 0; j < dangers.size(); j++) {
                        if (dangers.get(j).row == kingMove.row
                                && dangers.get(j).col == kingMove.col) {
//                    System.out.println("Black move can take");
//                    System.out.println(blackValidMoves.get(j).dRow + " " +
//                            blackValidMoves.get(j).dCol);
                            valid = false;
                            break;
                        }
                    }
                }
                if (valid) {
                    newKingValid.add(kingMove);
                }
            }
            king.validMoves = newKingValid;
        }
    }

//    private ArrayList<Pair> whiteCheckMoves(Piece tempObject) {
//        boolean blocksCheck = false;
//        int blockerIndex = -1;
//        ArrayList<Pair> stillBlockMoves = new ArrayList<>();
//        stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//        if (tempObject.type == 4) {
//            int curR = tempObject.getPosR();
//            int curC = tempObject.getPosC();
//            while (curR > 0 && curC > 0) {
//                curR--;
//                curC--;
//                stillBlockMoves.add(new Pair(curR, curC));
//                if (chessBoard[curR][curC] == 11) {
//                    blocksCheck = true;
//                    break;
//                } else if (chessBoard[curR][curC] % 2 == 1
//                        && blockerIndex == -1) {
//                    blockerIndex = handler.getPieceIndex(
//                            handler.generateKey(curR, curC));
//                } else if (chessBoard[curR][curC] % 2 == 1
//                        && blockerIndex > 0) {
//                    blockerIndex = -2;
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curR > 0 && curC < chessBoard.length - 1) {
//                    curR--;
//                    curC++;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curR < chessBoard.length - 1 && curC > 0) {
//                    curR++;
//                    curC--;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curR < chessBoard.length - 1
//                        && curC < chessBoard.length - 1) {
//                    curR++;
//                    curC++;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//        } else if (tempObject.type == 8) {
//            // Rooks
//            int curR = tempObject.getPosR();
//            int curC = tempObject.getPosC();
//            while (curR > 0) {
//                curR--;
//                stillBlockMoves.add(new Pair(curR, curC));
//                if (chessBoard[curR][curC] == 11) {
//                    blocksCheck = true;
//                    break;
//                } else if (chessBoard[curR][curC] % 2 == 1
//                        && blockerIndex == -1) {
//                    blockerIndex = handler.getPieceIndex(
//                            handler.generateKey(curR, curC));
//                } else if (chessBoard[curR][curC] % 2 == 1
//                        && blockerIndex > 0) {
//                    blockerIndex = -2;
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curC < chessBoard.length - 1) {
//                    curC++;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curR < chessBoard.length - 1) {
//                    curR++;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curC > 0) {
//                    curC--;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//        } else if (tempObject.type == 10) {
//            // Queem
//            int curR = tempObject.getPosR();
//            int curC = tempObject.getPosC();
//            while (curR > 0 && curC > 0) {
//                curR--;
//                curC--;
//                stillBlockMoves.add(new Pair(curR, curC));
//                if (chessBoard[curR][curC] == 11) {
//                    blocksCheck = true;
//                    break;
//                } else if (chessBoard[curR][curC] % 2 == 1
//                        && blockerIndex == -1) {
//                    blockerIndex = handler.getPieceIndex(
//                            handler.generateKey(curR, curC));
//                } else if (chessBoard[curR][curC] % 2 == 1
//                        && blockerIndex > 0) {
//                    blockerIndex = -2;
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curR > 0 && curC < chessBoard.length - 1) {
//                    curR--;
//                    curC++;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curR < chessBoard.length - 1 && curC > 0) {
//                    curR++;
//                    curC--;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curR < chessBoard.length - 1
//                        && curC < chessBoard.length - 1) {
//                    curR++;
//                    curC++;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//            curR = tempObject.getPosR();
//            curC = tempObject.getPosC();
//            while (curR > 0) {
//                curR--;
//                stillBlockMoves.add(new Pair(curR, curC));
//                if (chessBoard[curR][curC] == 11) {
//                    blocksCheck = true;
//                    break;
//                } else if (chessBoard[curR][curC] % 2 == 1
//                        && blockerIndex == -1) {
//                    blockerIndex = handler.getPieceIndex(
//                            handler.generateKey(curR, curC));
//                } else if (chessBoard[curR][curC] % 2 == 1
//                        && blockerIndex > 0) {
//                    blockerIndex = -2;
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curC < chessBoard.length - 1) {
//                    curC++;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curR < chessBoard.length - 1) {
//                    curR++;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//
//            if (!blocksCheck) {
//                curR = tempObject.getPosR();
//                curC = tempObject.getPosC();
//                stillBlockMoves = new ArrayList<>();
//                stillBlockMoves.add(new Pair(tempObject.getPosR(), tempObject.getPosC()));
//                while (curC > 0) {
//                    curC--;
//                    stillBlockMoves.add(new Pair(curR, curC));
//                    if (chessBoard[curR][curC] == 11) {
//                        blocksCheck = true;
//                        break;
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex == -1) {
//                        blockerIndex = handler.getPieceIndex(
//                                handler.generateKey(curR, curC));
//                    } else if (chessBoard[curR][curC] % 2 == 1
//                            && blockerIndex > 0) {
//                        blockerIndex = -2;
//                    }
//                }
//            }
//        }
//        return stillBlockMoves;
//    }
}
