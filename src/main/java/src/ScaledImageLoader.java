/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author harryhe
 */
public class ScaledImageLoader {
    BufferedImage image;
    
    String truePath = "";
    
    public ScaledImageLoader(){
        try{
            truePath = new File(".").getCanonicalPath();
        } catch (IOException e){
            System.out.println("Canonical Path has failed");
        }
    }
    // "/Users/harryhe/NetBeansProjects/ChessAI/res/"

    public Image loadImage(String path, int scaleW, int scaleH){
        try {
            image = ImageIO.read(new File(truePath + "/res/" + (path)));
        } catch (IOException ex) {
            Logger.getLogger(ScaledImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image.getScaledInstance(scaleW, scaleH, 
                java.awt.Image.SCALE_SMOOTH);
    }
}
