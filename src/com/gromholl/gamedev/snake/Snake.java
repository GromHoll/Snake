package com.gromholl.gamedev.snake;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Snake {

    private String SEGMENT_IMAGE_PATH = "/com/gromholl/gamedev/snake/images/Segment.png";
    private Image SEGMENT_IMAGE;
    
    private LinkedList<SnakeSegment> snake;
    
    public static final int UP    = 1;
    public static final int LEFT  = 2;
    public static final int RIGHT = 3;
    public static final int DOWN  = 4;
    
    private int direction = UP;
    
    public Snake(int x, int y) throws IOException {
        if(SEGMENT_IMAGE == null) {
            SEGMENT_IMAGE = ImageIO.read(Snake.class.getResource(SEGMENT_IMAGE_PATH));
        }
        
        snake = new LinkedList<SnakeSegment>();
        snake.add(new SnakeSegment(x, y));
    }
    
    public void moveHead() {
        
        switch(direction) {
            case UP:
                snake.addFirst(new SnakeSegment(getHead().x, getHead().y-10));
                break;
            case LEFT:
                snake.addFirst(new SnakeSegment(getHead().x-10, getHead().y));
                break;
            case RIGHT:
                snake.addFirst(new SnakeSegment(getHead().x+10, getHead().y));
                break;
            case DOWN:
                snake.addFirst(new SnakeSegment(getHead().x, getHead().y+10));
                break;
        }
    }
    
    public void moveTail() {
        snake.removeLast();
    }
    
    public SnakeSegment getHead() {
        return snake.get(0);
    }
    
    public LinkedList<SnakeSegment> getSegments() {
        return snake;
    }
    
    public class SnakeSegment implements ISprite {
        
        private int x;
        private int y;
        
        public SnakeSegment(int x, int y) {
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
            return SEGMENT_IMAGE;
        }
    }
    
    public void keyPressed(KeyEvent e) {
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if(direction != DOWN) {
                    direction = UP;
                }
                break;
            case KeyEvent.VK_LEFT:
                if(direction != RIGHT) {
                    direction = LEFT;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(direction != LEFT) {
                    direction = RIGHT;
                }
                break;
            case KeyEvent.VK_DOWN:
                if(direction != UP) {
                    direction = DOWN;
                }
                break;
        }
    }

}
