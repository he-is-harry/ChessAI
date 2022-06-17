/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author harryhe
 */
public class Game extends Canvas implements Runnable{
    
    public static final int WIDTH = 1024, HEIGHT = WIDTH * 9 / 12;
    private Thread thread;
    public boolean running = false;
    
    Color backColour;
    
    private Handler handler;
    private Board board;
    private Menu menu;
    private Opponent opponent1;
    private Opponent opponent2;
    
    private String truePath;
    
    public Game(){
        try{
            truePath = new File(".").getCanonicalPath();
        } catch (IOException e){
            System.out.println("Canonical Path has failed");
        }
        
        handler = new Handler();
        board = new Board(this, handler);
        opponent1 = new Opponent(this, handler, board, !board.wPers, 
                0, true);
        opponent2 = new Opponent(this, handler, board, board.wPers, 
                0, false);
        menu = new Menu(board, handler, this, opponent1, opponent2);
        
        this.addMouseListener(board);
        this.addMouseListener(menu);
        
        new Window(WIDTH, HEIGHT, "Harry's Chess Game", this);
        
        opponent1.start();
        opponent2.start();
                
        backColour = new Color(48, 46, 43);
    }
    
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running) {
                render();
            }
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }
    
    public void tick(){
        handler.tick();
        menu.tick();
    }
    
    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        
        g.setColor(backColour);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        if(board != null){
            board.render(g);
        }
        handler.render(g);
        menu.render(g);
        
        g.dispose();
        bs.show();
    }
    
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String [] args){
        new Game();
    }
}
