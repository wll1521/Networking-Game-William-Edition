package netGame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import javax.imageio.ImageIO;
public class PlayerEntity extends Entity{
    private BufferedImage sprite;
    public boolean isDestroyed = false;
    public PlayerEntity(){
    }
    @Override
    public BufferedImage getSprite() {
        try{
            if(!isDestroyed)
                this.sprite = ImageIO.read(new File("./data/greentank.png"));
            else
                this.sprite = ImageIO.read(new File("./data/Bullet.png"));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return this.sprite;
    }
}
