package netGame;

import java.util.ArrayList;

public class WorldContext {
    public ArrayList<PlayerEntity> tanks;
    public ArrayList<Entity> bullets;
    public WorldContext(){
        this.tanks = new ArrayList<>();
        this.bullets = new ArrayList<>();
    }
}
