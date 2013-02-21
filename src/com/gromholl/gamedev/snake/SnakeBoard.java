package com.gromholl.gamedev.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SnakeBoard extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    public static final int TOP_SPACE  = 10;
    public static final int LEFT_SPACE = 10;
    public static final String FIELD_IMAGE_PATH = "/com/gromholl/gamedev/snake/images/Border.png";
    public static final String FEED_IMAGE_PATH = "/com/gromholl/gamedev/snake/images/Feed.png";
    public static Image FIELD_IMAGE;
    public static Image FEED_IMAGE;

    private final int DELAY = 50;
    
    private Thread animator;    
    private Snake snake;
    private Feed feed;
    
    private boolean gameOver = false;

    public SnakeBoard() throws IOException {
        
        FIELD_IMAGE = ImageIO.read(SnakeBoard.class.getResource(FIELD_IMAGE_PATH));
        FEED_IMAGE = ImageIO.read(SnakeBoard.class.getResource(FEED_IMAGE_PATH));
        
        snake = new Snake(140, 140);
        snake.moveHead();
        snake.moveHead();

        addKeyListener(new SnakeKeyAdapter());
        setFocusable(true);
        
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }
    
    public void addNotify() {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    private void calc() {
        if(feed == null) {
            addFeed();
        }
        
        snake.moveHead();
        
        if(snake.getHead().getX() < 0 ||
           snake.getHead().getY() < 0 ||
           snake.getHead().getX() >= 300 ||
           snake.getHead().getY() >= 300) {
            
            gameOver = true;
            return;
        }
        
        for(ISprite s : snake.getSegments()) {
            if(s != snake.getHead()) {
                if(s.getX() == snake.getHead().getX() && s.getY() == snake.getHead().getY()) {
                    gameOver = true;
                    return;
                }
            }
        }        
        
        if(feed.getX() == snake.getHead().getX() && feed.getY() == snake.getHead().getY()) {
            addFeed();
        } else {
            snake.moveTail();
        }

    }
    
    private void addFeed() {
        Random r = new Random();
        
        int x = r.nextInt(30)*10;
        int y = r.nextInt(30)*10;
        
        for(ISprite s : snake.getSegments()) {
            if(s.getX() == x && s.getY() == y) {
                addFeed();
                return;
            }
        }
                
        feed = new Feed(x, y);        
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2D = (Graphics2D) g;
        
        if(!gameOver) {
            g2D.drawImage(FIELD_IMAGE, 0, 0, this);        
            g2D.drawImage(feed.getImage(), LEFT_SPACE + feed.getX(), TOP_SPACE + feed.getY(), this);        
            for(ISprite s : snake.getSegments()) {
                g2D.drawImage(s.getImage(), LEFT_SPACE + s.getX(), TOP_SPACE + s.getY(), this);
            }
        } else {
            String msg = "Game Over";
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metr = this.getFontMetrics(small);

            g2D.setColor(Color.white);
            g2D.setFont(small);
            g2D.drawString(msg, (320 - metr.stringWidth(msg)) / 2, 160);
        }
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
   
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {
            
            calc();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0)
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    private class Feed implements ISprite {

        private int x;
        private int y;
        
        public Feed(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public int getX() {
            return x;
        }
        @Override
        public int getY() {
            return y;
        }

        @Override
        public Image getImage() {
            return FEED_IMAGE;
        }
        
    }

    private class SnakeKeyAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            snake.keyPressed(e);
        }
    }

}
