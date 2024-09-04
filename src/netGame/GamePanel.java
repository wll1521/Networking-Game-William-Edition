package netGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel{
    WorldContext ctx = new WorldContext();
    Set<Integer> downKeys = new HashSet<Integer>();
    Entity player;
    float[] playerMovementVec;
    long lastTick; // Helper var for physics engine
    long lastBullet = 0;
    long bulletCooldown = 300;

    public GamePanel(){
        super();
        this.setFocusable(true);
        this.requestFocus();
        this.setPreferredSize(new Dimension(800, 600));
        // Spawn player
        this.ctx.worldEntities.add(new PlayerEntity());
        this.player = this.ctx.worldEntities.get(0);
        this.player.physVecs.add(new float[] {0,0});
        this.playerMovementVec = this.player.physVecs.get(0);
        player.x = 100;
        player.y = 100;
        // Listen for inputs
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                downKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                downKeys.remove(e.getKeyCode());
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
        //Process player input
        for(var key : downKeys){
            var angle = Math.toRadians(player.angle);
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            double playerSpeed = 1.2;

            if(key == KeyEvent.VK_RIGHT) {
                player.rotationVec = 1.0f;
            }
            if(key == KeyEvent.VK_LEFT) {
                player.rotationVec = -1.0f;
            }
            if(key == KeyEvent.VK_UP){
                playerMovementVec[0] = (float) (playerSpeed * sin);
                playerMovementVec[1] = (float) (playerSpeed * cos * -1);
            }
            if(key == KeyEvent.VK_SPACE){
                long bulletDiff = System.currentTimeMillis() - lastBullet;
                if(bulletDiff > bulletCooldown) {
                    lastBullet = System.currentTimeMillis();
                    float bulletSpeed = 10;
                    ctx.worldEntities.add(new BulletEntity());
                    var bullet = ctx.worldEntities.get(ctx.worldEntities.size() - 1);
                    bullet.x = player.x;
                    bullet.y = player.y;
                    bullet.angle = player.angle;
                    bullet.physVecs.add(new float[]{(float) (bulletSpeed * sin), (float) (bulletSpeed * cos * -1)});
                }
            }
        }
        if(this.lastTick == 0){
            this.lastTick = System.currentTimeMillis();
        }
        // Physics engine...
        double FRICTION_COEFFICIENT = 0.95;
        double STOP_POINT = 0.6;
        // Push objects...
        long curTime = System.currentTimeMillis();
        long delta = curTime - lastTick;
        lastTick = curTime;
        float moveAmount = (float) delta/6;

        for (int i = this.ctx.worldEntities.size()-1; i >= 0; i--){
            var entity = ctx.worldEntities.get(i);
            if(entity.rotationVec != 0){
                entity.angle += moveAmount * entity.rotationVec;
                entity.rotationVec *= (float) FRICTION_COEFFICIENT;
                if(Math.abs(entity.rotationVec) < STOP_POINT )
                    entity.rotationVec = 0;
                entity.angle %= 360;
            }
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
                    if(entity instanceof BulletEntity){
                        ctx.worldEntities.remove(entity);
                        continue;
                    }
                    if(vec != playerMovementVec)
                        entity.physVecs.remove(j);
                    else{
                        playerMovementVec[0] = 0;
                        playerMovementVec[1] = 0;
                    }
                }
            }
        }
        super.repaint();
    }
}
