/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author harryhe
 */
public class FontLoader {

    String truePath = "";
    
    public FontLoader(){
        try{
            truePath = new File(".").getCanonicalPath();
        } catch (IOException e){
            System.out.println("Canonical Path has failed");
        }
    }
    // "/Users/harryhe/NetBeansProjects/ChessAI/res/"

    public Font loadFont(String path, int fontSize) {
        Font f = new Font("arial", 1, 30);
        try {
            f = Font.createFont(Font.PLAIN, new File(truePath + "/res/" + (path))).deriveFont(Font.PLAIN, fontSize);
        } catch (FontFormatException | NullPointerException | IOException ex) {
            ex.printStackTrace();
        }
        if (f != null) {
            return f;
        } else {
            System.out.println("Warning file is null");
            return f;
        }
    }
}
