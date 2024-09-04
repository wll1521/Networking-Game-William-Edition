package netGame;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Entity {
    public float x;
    public float y;
    public int height = 25;
    public int width = 25;
    public float angle = 0;
    public float weight = 1.0F;
    public float rotationVec = 0.0f;
    public ArrayList<float[]> physVecs = new ArrayList<>();
    public abstract BufferedImage getSprite();

    public boolean checkCollision(Entity e){
        if(Math.abs(this.x - e.x) <= width)
            if(Math.abs(this.y - e.y) <= height)
                return true;
        return false;
    }
}
