package netGame;

import java.awt.image.BufferedImage;

public abstract class Entity {
    public int x;
    public int y;
    public abstract BufferedImage getSprite();
}
