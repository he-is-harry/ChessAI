/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 *
 * @author harryhe
 */
public class Piece {

    public int type;
    private Handler handler;
    private Board board;
    private MoveGenerator mg = new MoveGenerator(handler, board);

    public boolean clicked;
    public ArrayList<Pair> validMoves;
    private boolean generatedMoves;

    public boolean firstMove;
    public boolean enPassant;

    private int posR;
    private int posC;
    private float x;
    private float y;
    private float destX;
    private float destY;
    private float velX;
    private float velY;
    //private boolean velCalc;

    private String img;
    private String invImg;
    private Font f1;
    private Font f2;
    private FontLoader fl;
    private Color dark;
    private Color light;
    private Color selected;

    private int handlerIndex;

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
    public Piece(int posR, int posC, int type, Board board,
            Handler handler, int handlerIndex) {
        this.posR = posR;
        this.posC = posC;
        this.board = board;
        if (board.wPers) {
            x = (int) (20 + posC * board.squareLength + (0.02 * board.squareLength));
            y = (int) (20 + posR * board.squareLength
                    + (0.9 * board.squareLength));
        } else {
            x = (int) (20 + (7 - posC) * board.squareLength + (0.02 * board.squareLength));
            y = (int) (20 + (7 - posR) * board.squareLength
                    + (0.9 * board.squareLength));
        }
        destX = x;
        destY = y;
        velX = 0;
        velY = 0;
        //velCalc = false;

        mg = new MoveGenerator(handler, board);
        this.handler = handler;
        this.type = type;

        clicked = false;
        generatedMoves = false;
        firstMove = true;
        enPassant = false;

        this.handlerIndex = handlerIndex;

        dark = new Color(68, 65, 64);
        light = new Color(236, 236, 236);
        selected = new Color(131, 207, 242);
        fl = new FontLoader();
        f1 = fl.loadFont("/FreeSerif.ttf", 100);
        f2 = fl.loadFont("/FreeSerif.ttf", 100);
        switch (type) {
            case 1:
                img = Character.toString('\u2659');
                invImg = Character.toString('\u265F');
                break;
            case 2:
                img = Character.toString('\u265F');
                invImg = Character.toString('\u2659');
                break;
            case 3:
                img = Character.toString('\u2657');
                invImg = Character.toString('\u265D');
                break;
            case 4:
                img = Character.toString('\u265D');
                invImg = Character.toString('\u2657');
                break;
            case 5:
                img = Character.toString('\u2658');
                invImg = Character.toString('\u265E');
                break;
            case 6:
                img = Character.toString('\u265E');
                invImg = Character.toString('\u2658');
                break;
            case 7:
                img = Character.toString('\u2656');
                invImg = Character.toString('\u265C');
                break;
            case 8:
                img = Character.toString('\u265C');
                invImg = Character.toString('\u2656');
                break;
            case 9:
                img = Character.toString('\u2655');
                invImg = Character.toString('\u265B');
                break;
            case 10:
                img = Character.toString('\u265B');
                invImg = Character.toString('\u2655');
                break;
            case 11:
                img = Character.toString('\u2654');
                invImg = Character.toString('\u265A');
                break;
            case 12:
                img = Character.toString('\u265A');
                invImg = Character.toString('\u2654');
                break;
            default:
                break;
        }
    }

    public void tick() {
        if (Math.round(destX) != Math.round(x)
                || Math.round(destY) != Math.round(y)) {
            //System.out.println(x + " " + y + " " + destX + " " + destY);
            x += velX;
            y += velY;
        }
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
    public void generateMoves() {
        boolean done = false;
        if (type == 1) {
            validMoves = mg.WhitePawn(posR, posC, firstMove);
            done = true;
        } else if (type == 2) {
            validMoves = mg.BlackPawn(posR, posC, firstMove);
            done = true;
        } else if (type == 5) {
            validMoves = mg.WhiteHorse(posR, posC);
            done = true;
        } else if (type == 6) {
            validMoves = mg.BlackHorse(posR, posC);
            done = true;
        } else if (type == 3) {
            validMoves = mg.WhiteBishop(posR, posC);
            done = true;
        } else if (type == 4) {
            validMoves = mg.BlackBishop(posR, posC);
            done = true;
        } else if (type == 7) {
            validMoves = mg.WhiteRook(posR, posC);
            done = true;
        } else if (type == 8) {
            validMoves = mg.BlackRook(posR, posC);
            done = true;
        } else if (type == 9) {
            validMoves = mg.WhiteQueen(posR, posC);
            done = true;
        } else if (type == 10) {
            validMoves = mg.BlackQueen(posR, posC);
            done = true;
        } else if (type == 11) {
            validMoves = mg.WhiteKing(posR, posC);
            done = true;
        } else if (type == 12) {
            validMoves = mg.BlackKing(posR, posC);
            done = true;
        }
        if (!done) {
            validMoves = new ArrayList<>();
        }
    }

    public void checkPassant() {
        if (enPassant) {
            enPassant = false;
        }
    }

    public void render(Graphics g) {
        if (clicked) {
            g.setColor(selected);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(makeTransparent((float) 0.5));
            for (int i = 0; i < validMoves.size(); i++) {
                Pair p = validMoves.get(i);
                if (board.wPers) {
//                    if (board.chessBoard[p.row][p.col] > 0) {
//                        g.drawOval((int) (20 + (p.col + 1) * board.squareLength
//                                - (board.squareLength / 2)),
//                                (int) (20 + (p.row + 1) * board.squareLength
//                                - (board.squareLength / 2)), (int) board.squareLength / 2,
//                                (int) board.squareLength / 2);
////                        g2d.draw(createRingShape((20 + (p.col + 1) * board.squareLength
////                                - board.squareLength / 2 - 12), 
////                                (20 + (7 - (p.row + 1)) * board.squareLength
////                                - board.squareLength / 2 - 12), 36, 8));
//                    } else {
//                        g.fillOval((int) (20 + (p.col + 1) * board.squareLength
//                                - board.squareLength / 2 - 12),
//                                (int) (20 + (p.row + 1) * board.squareLength
//                                - board.squareLength / 2 - 12), 24, 24);
//                    }
                    g.fillRect((int) (20 + (p.col) * board.squareLength),
                            (int) (20 + (p.row) * board.squareLength),
                            (int) board.squareLength, (int) board.squareLength);
                } else {
//                    if (board.chessBoard[p.row][p.col] > 0) {
//                        g.drawOval((int) (20 + (p.col + 1) * board.squareLength
//                                - (board.squareLength / 2)),
//                                (int) (20 + (7 - (p.row + 1)) * board.squareLength
//                                - (board.squareLength / 2)), (int) board.squareLength / 2,
//                                (int) board.squareLength / 2);
////                        g2d.draw(createRingShape((20 + (p.col + 1) * board.squareLength
////                                - board.squareLength / 2 - 12), 
////                                (20 + (7 - (p.row + 1)) * board.squareLength
////                                - board.squareLength / 2 - 12), 36, 8));
//                    } else {
//                        g.fillOval((int) (20 + (p.col + 1) * board.squareLength
//                                - board.squareLength / 2 - 12),
//                                (int) (20 + (7 - (p.row + 1)) * board.squareLength
//                                - board.squareLength / 2 - 12), 24, 24);
//                    }
                    g.fillRect((int) (20 + (7 - p.col) * board.squareLength),
                            (int) (20 + (7 - (p.row)) * board.squareLength),
                            (int) board.squareLength, (int) board.squareLength);
                }
            }
            g2d.setComposite(makeTransparent(1));
        }

        if (type % 2 == 1) {
            g.setFont(f2);
            g.setColor(light);
            g.drawString(invImg, (int) x, (int) y);
        }
        g.setFont(f1);
        g.setColor(dark);
        g.drawString(img, (int) x, (int) y);
    }

    public synchronized void move(int destR, int destC, boolean instant, boolean visible) {
        //System.out.println("This piece has apparently moved");
        if (firstMove) {
            firstMove = false;
            if (Math.abs(destR - posR) == 2 && (type == 1 || type == 2)) {
                enPassant = true;
                board.enPassantFile = posC;
//                System.out.println(handler.pieces.get
//                (handlerIndex).getEnPassant() + " " + enPassant);
            }
        }
        board.chessBoard[posR][posC] = 0;
        board.chessBoard[destR][destC] = type;
        handler.removeMapValue(handler.generateKey(posR, posC));
        handler.addMapValue(handler.generateKey(destR, destC), handlerIndex);
        posR = destR;
        posC = destC;

        // Actual rendering processes
        if (visible) {
            if (board.wPers) {
                destX = (int) (20 + posC * board.squareLength + (0.02 * board.squareLength));
                destY = (int) (20 + posR * board.squareLength
                        + (0.9 * board.squareLength));
            } else {
                destX = (int) (20 + (7 - posC) * board.squareLength + (0.02 * board.squareLength));
                destY = (int) (20 + (7 - posR) * board.squareLength
                        + (0.9 * board.squareLength));
            }
            if (instant) {
                x = destX;
                y = destY;
            }

            velX = (destX - x) / 25;
            velY = (destY - y) / 25;
        }

        //System.out.println(x + " " + y + " " + destX + " " + destY);
    }

    public synchronized void setType(int type, boolean visible) {
        this.type = type;
        if (visible) {
            switch (type) {
                case 1:
                    img = Character.toString('\u2659');
                    invImg = Character.toString('\u265F');
                    break;
                case 2:
                    img = Character.toString('\u265F');
                    invImg = Character.toString('\u2659');
                    break;
                case 3:
                    img = Character.toString('\u2657');
                    invImg = Character.toString('\u265D');
                    break;
                case 4:
                    img = Character.toString('\u265D');
                    invImg = Character.toString('\u2657');
                    break;
                case 5:
                    img = Character.toString('\u2658');
                    invImg = Character.toString('\u265E');
                    break;
                case 6:
                    img = Character.toString('\u265E');
                    invImg = Character.toString('\u2658');
                    break;
                case 7:
                    img = Character.toString('\u2656');
                    invImg = Character.toString('\u265C');
                    break;
                case 8:
                    img = Character.toString('\u265C');
                    invImg = Character.toString('\u2656');
                    break;
                case 9:
                    img = Character.toString('\u2655');
                    invImg = Character.toString('\u265B');
                    break;
                case 10:
                    img = Character.toString('\u265B');
                    invImg = Character.toString('\u2655');
                    break;
                case 11:
                    img = Character.toString('\u2654');
                    invImg = Character.toString('\u265A');
                    break;
                case 12:
                    img = Character.toString('\u265A');
                    invImg = Character.toString('\u2654');
                    break;
                default:
                    break;
            }
        }
    }

    public int getType() {
        return type;
    }

    public int getPosR() {
        return posR;
    }

    public int getPosC() {
        return posC;
    }

    public void setPosR(int posR) {
        this.posR = posR;
    }

    public void setPosC(int posC) {
        this.posC = posC;
    }

    public boolean getEnPassant() {
        return enPassant;
    }

    public int getHandlerIndex() {
        return handlerIndex;
    }

    public void setHandlerIndex(int handlerIndex) {
        this.handlerIndex = handlerIndex;
    }

    public void setClicked(boolean isClicked) {
        this.clicked = isClicked;
    }

    private AlphaComposite makeTransparent(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return (AlphaComposite.getInstance(type, alpha));
    }

    private static Shape createRingShape(
            double centerX, double centerY, double outerRadius, double thickness) {
        Ellipse2D outer = new Ellipse2D.Double(
                centerX - outerRadius,
                centerY - outerRadius,
                outerRadius + outerRadius,
                outerRadius + outerRadius);
        Ellipse2D inner = new Ellipse2D.Double(
                centerX - outerRadius + thickness,
                centerY - outerRadius + thickness,
                outerRadius + outerRadius - thickness - thickness,
                outerRadius + outerRadius - thickness - thickness);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }

}
