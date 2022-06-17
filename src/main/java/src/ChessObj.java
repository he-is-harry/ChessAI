/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author harryhe
 */
public abstract class ChessObj {
    // This class is not useful, it is obsolete
    // Can be removed
    protected float x, y;
    protected float velX, velY;
    
    public ChessObj(float x, float y){
        this.x = x;
        this.y = y;
    }
    
    public abstract void tick();
    public abstract void render(Graphics g);
    
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
}
