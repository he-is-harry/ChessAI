/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author harryhe
 */
public class Handler {

    LinkedList<Piece> pieces;
    private LinkedList<Piece> renderPieces;
    Map<String, Integer> indexFinder;

    public Handler() {
        pieces = new LinkedList<>();
        renderPieces = new LinkedList<>();
        indexFinder = new HashMap<>();
    }

    public void tick() {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) != null) {
                Piece tempObject = pieces.get(i);
                tempObject.tick();
//                if (tempObject != null) {
//                    tempObject.tick();
//                }
            }

        }
        for (int i = 0; i < renderPieces.size(); i++) {
            if (renderPieces.get(i) != null) {
                Piece tempObject = renderPieces.get(i);
                tempObject.tick();
//                if (tempObject != null) {
//                    tempObject.tick();
//                }
            }
        }
    }

    public synchronized void render(Graphics g) {
        int clickedIndex = -1;
        for (int i = 0; i < renderPieces.size(); i++) {
            Piece tempObject = renderPieces.get(i);
            if (tempObject.clicked) {
                clickedIndex = i;
                tempObject.render(g);
            }
        }
        for (int i = 0; i < renderPieces.size(); i++) {
            Piece tempObject = renderPieces.get(i);
            if (i != clickedIndex) {
                tempObject.render(g);
            }
        }
    }

    public synchronized void clearPieces() {
        pieces.clear();
    }

    public void generateUnfilteredMoves() {
        for (int i = 0; i < pieces.size(); i++) {
            Piece tempObject = pieces.get(i);
            tempObject.generateMoves();
        }
    }

    public synchronized void addObject(Piece object) {
        this.pieces.add(object);
    }

    public synchronized void addMapValue(String key, int index) {
        this.indexFinder.put(key, index);
    }

    public synchronized void removeObject(Piece object) {
        this.pieces.remove(object);
        indexFinder.clear();
        for (int i = 0; i < pieces.size(); i++) {
            Piece tempObject = pieces.get(i);
            addMapValue(generateKey(tempObject.getPosR(),
                    tempObject.getPosC()), i);
            tempObject.setHandlerIndex(i);
        }
    }

    public synchronized void removeMapValue(String key) {
        this.indexFinder.remove(key);
    }

    public synchronized void updateRenderPieces() {
        renderPieces.clear();
        for (int i = 0; i < pieces.size(); i++) {
            renderPieces.add(pieces.get(i));
        }
    }

    public int getSize() {
        return pieces.size();
    }

    public int getPieceIndex(String key) {
        return this.indexFinder.get(key);
    }

    public String generateKey(int r, int c) {
        return Integer.toString(r) + " " + Integer.toString(c);
    }
}
