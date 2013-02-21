package com.gromholl.gamedev.snake;

import javax.swing.JFrame;

public class SnakeFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public SnakeFrame() {
        try {
            add(new SnakeBoard());
        } catch (Exception e) {
            System.out.println("Game Resource Not Found");
            return;
        }
        setTitle("GromHoll's Snake");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(326, 345);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
    
    
    public static void main(String[] args) {
        new SnakeFrame();
    }
    
}
