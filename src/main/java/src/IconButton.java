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
public class IconButton extends Button {

    private Font font;
    private Font fontBorder;
    private char icon;
    private FontLoader fl;
    private float selRoundD;
    private int type;
    private Color light;
    private Color dark;
    private Color selColor;

    public IconButton(float x, float y, float width,
            float height, int type, int fontSize, char icon, Color selColor) {
        super(x, y, width, height, new Color(234, 234, 200));
        fl = new FontLoader();
        this.font = fl.loadFont("/FreeSerif.ttf", fontSize);
        this.fontBorder = fl.loadFont("/FreeSerif.ttf", fontSize + 6);
        this.icon = icon;
        this.type = type;
        // 0 is white
        // 1 is random
        // 2 is black
        this.selRoundD = Math.min((float) 1.05 * width,
                (float) 1.05 * height) / 10;
        this.selColor = selColor;
        dark = new Color(68, 65, 64);
        light = new Color(236, 236, 236);
    }

    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (selected) {
            g2d.setColor(selColor);
            g2d.fillRoundRect((int)x - (int)(width * 0.05), 
                    (int)y - (int)(height * 0.05), (int)(width * 1.1), 
            (int)(height * 1.1), (int)selRoundD, (int)selRoundD);
        }
        if (type == 0) {
            g2d.setColor(color);
            g2d.fillRoundRect((int) x, (int) y, (int) width, (int) height,
                (int) roundD, (int) roundD);
            g2d.setColor(light);
            g2d.setFont(font);
            g2d.drawString(Character.toString('\u265A'), (int) x + (float)0.08 * width,
                    (int) y + (float)0.8 * height);
            g2d.setColor(dark);
            g2d.drawString(Character.toString(icon), (int) x + (float)0.08 * width,
                    (int) y + (float)0.8 * height);
            
        } else if (type == 1) {
            g2d.setColor(color);
            g2d.fillRoundRect((int) x, (int) y, (int) width, (int) height,
                (int) roundD, (int) roundD);
            g2d.setColor(Color.black);
            g2d.fillRect((int)x + (int)width / 2, (int) y, (int) width / 2, 
                    (int) height);
            g2d.setColor(dark);
            g2d.setFont(fontBorder);
            g2d.drawString(Character.toString(icon), (int) x + (float)0.21 * width,
                    (int) y + (float)0.9 * height);
            g2d.setColor(light);
            g2d.setFont(font);
            g2d.drawString(Character.toString(icon), (int) x + (float)0.25 * width,
                    (int) y + (float)0.85 * height);
        } else if (type == 2) {
            g2d.setColor(Color.black);
            g2d.fillRoundRect((int) x, (int) y, (int) width, (int) height,
                (int) roundD, (int) roundD);
            g2d.setColor(dark);
            g2d.setFont(font);
            g2d.drawString(Character.toString(icon), (int)x + (float)0.08 * width,
                    (int) y + (float)0.8 * height);
//            g2d.setColor(light);
//            g2d.drawString(Character.toString(icon), (int) x + width / 5,
//                    (int) y + height);
        }
        
        
    }
}
