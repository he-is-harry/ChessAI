/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author harryhe
 */
public class ImageButton extends Button{
    
    private ScaledImageLoader sil;
    
    private Image img;
    private float selRoundD;
    private Color selColor;
    
    public ImageButton(float x, float y, float width, 
            float height, Color color, String path, Color selColor){
        super(x, y, width, height, color);
        sil = new ScaledImageLoader();
        img = sil.loadImage(path, (int)width, (int)height);
        selRoundD = Math.min((float)1.05 * width, 
                (float)1.05 * height) / 10;
        this.selColor = selColor;
    }
    public void tick() {
        
    }
    @Override
    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if(selected){
            g2d.setColor(selColor);
            g2d.fillRoundRect((int)x - (int)(width * 0.07), 
                    (int)y - (int)(height * 0.07), (int)(width * 1.14), 
            (int)(height * 1.14), (int)selRoundD, (int)selRoundD);
        }
        g2d.setColor(color);
        g2d.fillRoundRect((int)x, (int)y, (int)width, (int)height, 
                (int)roundD, (int)roundD);
        g.drawImage(img, (int)x, (int)y, null);
    }
}
