/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author harryhe
 */
public class TextButton extends Button{
    
    private Font font;
    private String message;
    private Color hovColor, fontColor;
    
    public TextButton(float x, float y, float width, 
            float height, Color color, int fontSize, Color hovColor, 
            Color fontColor, String message){
        super(x, y, width, height, color);
        this.hovColor = hovColor;
        this.fontColor = fontColor;
        this.font = new Font("Helvetica", Font.BOLD, fontSize);
        this.message = message;
    }
    public void tick() {
        
    }
    @Override
    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if(hover){
            g2d.setColor(hovColor);
        } else {
            g2d.setColor(color);
        }
        g2d.fillRoundRect((int)x, (int)y, (int)width, (int)height, 
                (int)roundD, (int)roundD);
        g2d.setColor(fontColor);
        g2d.setFont(font);
        g2d.drawString(message, (int)x + + (float)0.31 * width, 
                (int)y + (float)0.62 * height);
    }
}
