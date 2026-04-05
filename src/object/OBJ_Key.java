package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class OBJ_Key extends Entity {

    GamePanel gp;
    public static final String objName = "Key";

    public OBJ_Key(GamePanel gp)
    {
        super(gp);
        this.gp = gp;
        type = type_consumable;
        name = objName;
        down1 = setup("/objects/key",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nChia khoa van nang.";
        price = 350;
        stackable = true;

        setDialogue();
    }
    public void setDialogue()
    {
        dialogues[0][0] = "Ban dung " + name + " de mo cua";

        dialogues[1][0] = "Ban dang lam gi vay?";
    }
    public boolean use(Entity entity)
    {
        int objIndex = getDetected(entity, gp.obj, "Door"); //user, target, name
        if(objIndex != 999)
        {
            startDialogue(this,0);
            gp.playSE(3);
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        }
        else
        {
            startDialogue(this,1);
            return false;
        }
    }
}
