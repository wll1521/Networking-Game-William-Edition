package netGame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BulletEntity extends Entity{
    private BufferedImage sprite;
    public BulletEntity(){
    }
    @Override
    public BufferedImage getSprite() {
        try{
            this.sprite = ImageIO.read(new File("./data/Bullet.png"));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return this.sprite;
    }
}
