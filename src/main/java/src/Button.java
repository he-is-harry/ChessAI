/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author harryhe
 */
public abstract class Button {
    protected float x, y;
    protected float width, height;
    protected float roundD;
    protected boolean hover, selected;
    protected Color color;
    
    public Button(float x, float y, float width, float height, Color color){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.roundD = Math.min(width, height) / 10;
        this.hover = false;
        this.selected = false;
        this.color = color;
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
    public void setHover(boolean hover){
        this.hover = hover;
    }
    public boolean getHover(){
        return hover;
    }
    public void setSelected(boolean selected){
        this.selected = selected;
    }
    public boolean getSelected(){
        return selected;
    }
}
