package netGame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import javax.imageio.ImageIO;
public class PlayerEntity extends Entity{
    private BufferedImage sprite;
    public PlayerEntity(){
        try{
            this.sprite = ImageIO.read(new File("./data/greentank.png"));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    @Override
    public BufferedImage getSprite() {
        return this.sprite;
    }
}
