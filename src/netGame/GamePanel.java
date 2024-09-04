package netGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel{
    WorldContext ctx = new WorldContext();
    Entity player;
    long lastTick; // Helper var for physics engine

    public GamePanel(){
        super();
        this.setFocusable(true);
        this.requestFocus();
        this.setPreferredSize(new Dimension(800, 600));
        // Spawn player
        this.ctx.worldEntities.add(new PlayerEntity());
        this.player = this.ctx.worldEntities.get(0);
        player.x = 100;
        player.y = 100;
        // Listen for inputs
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                    player.angle = (player.angle + 30) % 360;
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    player.angle -= 30;
                    if(player.angle < 0)
                        player.angle += 360;
                }
                if(e.getKeyCode() == KeyEvent.VK_UP) {
                    var angle = Math.toRadians(player.angle);
                    float speed = 0.5f;
                    float sin = (float) Math.sin(angle);
                    float cos = (float) Math.cos(angle);
                    player.physVecs.add(new float[]{sin * speed, speed * -1 * cos});
                }
                revalidate();
                repaint();
            }
        });
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                physUpdate();
            }
        };
        Timer physTimer = new Timer(60/1000, action);
        physTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2d = (Graphics2D) g;
        for (var entity : this.ctx.worldEntities) {
            BufferedImage sprite = entity.getSprite();
            double angle = Math.toRadians(entity.angle);

            // Scale the sprite
            BufferedImage scaledSprite = new BufferedImage(entity.width, entity.height, sprite.getType());
            Graphics2D g2dScaled = scaledSprite.createGraphics();
            g2dScaled.drawImage(sprite, 0, 0, entity.width, entity.height, null);
            g2dScaled.dispose();

            // New values after rotation occurs
            double sin = Math.abs(Math.sin(angle));
            double cos = Math.abs(Math.cos(angle));
            int newWidth = (int) Math.floor(entity.width * cos + entity.height * sin);
            int newHeight = (int) Math.floor(entity.height * cos + entity.width * sin);

            // Create rotated version of sprite
            BufferedImage rotated = new BufferedImage(newWidth, newHeight, sprite.getType());
            Graphics2D g2dRotated = rotated.createGraphics();
            AffineTransform rotation = new AffineTransform();
            rotation.rotate(angle, entity.width / 2.0, entity.height / 2.0);
            g2dRotated.drawRenderedImage(scaledSprite, rotation);

            // Draw the transformed image
            g2d.drawImage(rotated, (int) Math.floor(entity.x), (int) Math.floor(entity.y), null);
        }
    }

    private void physUpdate(){
        if(this.lastTick == 0){
            this.lastTick = System.currentTimeMillis();
        }
        // Physics engine...
        double FRICTION_COEFFICIENT = 0.95;
        double STOP_POINT = 0.3;
        // Push objects...
        long curTime = System.currentTimeMillis();
        long delta = curTime - lastTick;
        float moveAmount = (float) delta/1000f;

        for (int i = 0; i < this.ctx.worldEntities.size(); i++){
            var entity = ctx.worldEntities.get(i);
            // Process all phys vectors on each entity
            for (int j = entity.physVecs.size() - 1; j >= 0; j--) {
                float[] vec = entity.physVecs.get(j);
                entity.x += moveAmount * vec[0];
                entity.y += moveAmount * vec[1];
                float magnitude = Math.abs(vec[0]) + Math.abs(vec[1]);

                vec[0] *= (float) FRICTION_COEFFICIENT;
                vec[1] *= (float) FRICTION_COEFFICIENT;

                // Remove vector if below stop point
                if (magnitude < STOP_POINT) {
                    entity.physVecs.remove(j);
                }
            }
        }
        super.repaint();
    }
}
