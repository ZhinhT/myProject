package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {

    GamePanel gp;
    public static final String objName = "Chest";
    public OBJ_Chest(GamePanel gp)
    {
        super(gp);
        this.gp = gp;

        type = type_obstacle;
        name = objName;
        image = setup("/objects/chest",gp.tileSize,gp.tileSize);
        image2 = setup("/objects/chest_opened",gp.tileSize,gp.tileSize);
        down1 = image;
        collision = true;

        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }
    public void setLoot(Entity loot)
    {
        this.loot = loot;

        setDialogue();
    }
    public void setDialogue()
    {
        dialogues[0][0] = "Bạn đã mở rương và tìm thấy " + loot.name + "!\n...Nhưng có vẻ balo của bạn đã đầy!";
        dialogues[1][0] = "Bạn đã mở rương và tìm thấy " + loot.name + "!\nBạn đã nhận được " + loot.name + "!";
        dialogues[2][0] = "Đây là chiếc rương rỗng.";
    }
    public void interact()
    {
        if(opened == false)
        {
            gp.playSE(3);

            if(gp.player.canObtainItem(loot) == false)
            {
                startDialogue(this,0);
            }
            else
            {
                startDialogue(this,1);
                //gp.player.inventory.add(loot); //canObtainItem() already adds item
                down1 = image2;
                opened = true;
            }
        }
        else
        {
            startDialogue(this,2);
        }
    }
}
