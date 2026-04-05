package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Helmets extends Entity {
    public static final String objName = "Helmets";

    public OBJ_Helmets(GamePanel gp) {
        super(gp);

        type = type_shield;
        name = objName;
        down1 = setup("/objects/sprite_1",gp.tileSize,gp.tileSize);

        description = "[" + name + "]\nMũ chiến binh";
        price = 5;
        defenseValue = 5;
    }
}
