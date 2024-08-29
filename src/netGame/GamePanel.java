package netGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel{
    PlayerEntity player = new PlayerEntity();
    public GamePanel(){
        super();
        this.setFocusable(true);
        this.requestFocus();
        this.setPreferredSize(new Dimension(800, 600));
        player.x = 100;
        player.y = 100;
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                    player.x += 50;
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    player.x -= 50;
                }
                revalidate();
                repaint();
            }
        });
    }
    // Rendering method
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2d = (Graphics2D) g;
        g2d.drawImage(player.getSprite(),player.x-50,player.y-50,  50, 50, this);
    }
}
