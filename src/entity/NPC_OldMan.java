package entity;

import main.GamePanel;

import java.awt.*;
import java.util.Random;


public class NPC_OldMan extends Entity{

    public NPC_OldMan(GamePanel gp)
    {
        super(gp);
        direction = "down";
        speed = 1;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        solidAreaDefaultX = 8;
        solidAreaDefaultY = 16;

        dialogueSet = -1; //For first dialogueSet(= 0)

        getImage();
        setDialogue();
    }
    public void getImage()
    {
        up1 = setup("/npc/oldman_up_1",gp.tileSize,gp.tileSize);
        up2 = setup("/npc/oldman_up_2",gp.tileSize,gp.tileSize);
        down1 = setup("/npc/oldman_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("/npc/oldman_down_2",gp.tileSize,gp.tileSize);
        left1 = setup("/npc/oldman_left_1",gp.tileSize,gp.tileSize);
        left2 = setup("/npc/oldman_left_2",gp.tileSize,gp.tileSize);
        right1 = setup("/npc/oldman_right_1",gp.tileSize,gp.tileSize);
        right2 = setup("/npc/oldman_right_2",gp.tileSize,gp.tileSize);
    }
    public void setDialogue()
    {
        dialogues[0][0] = "Xin chào.";
        dialogues[0][1] = "Vậy là bạn đến hòn đảo này để tìm kho báu.?";
        dialogues[0][2] = "Tôi từng là một pháp sư vĩ đại nhưng giờ... \ntôi đã quá già để tham gia một cuộc phiêu lưu rồi.";
        dialogues[0][4] = "Chúc bạn may mắn.";
        dialogues[0][3] = "Bạn có thể liên lạc lại với tôi khi bạn gặp khó khăn.";

        dialogues[1][0] = "Nếu bạn cảm thấy mệt mỏi, hãy nghỉ ngơi bên bờ nước.";
        dialogues[1][1] = "Tuy nhiên, quái vật sẽ xuất hiện\ntrở lại nếu bạn nghỉ ngơi.\nTôi không biết tại sao\nnhưng nó hoạt động như vậy đấy.";
        dialogues[1][2] = "Dù sao thì cũng đừng ép bản thân quá sức.";

        dialogues[2][0] = "Tôi tự hỏi làm thế nào để mở cánh cửa đó...";
    }
    public void setAction()
    {
        if(onPath == true)
        {
//            int goalCol = 12;
//            int goalRow = 9;

            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
            searchPath(goalCol, goalRow);

        }
        else
        {
            actionLockCounter++;

            if(actionLockCounter == 120)
            {
                Random random = new Random();
                int i = random.nextInt(100) + 1;  // pick up  a number from 1 to 100
                if(i <= 25)
                {
                    direction = "up";
                }
                if(i>25 && i <= 50)
                {
                    direction = "down";
                }
                if(i>50 && i <= 75)
                {
                    direction = "left";
                }
                if(i>75 && i <= 100)
                {
                    direction = "right";
                }
                actionLockCounter = 0; // reset
            }
        }
    }
    public void speak()
    {
        //ENTITY CLASS SPEAK()
        //Do this character specific stuff

        facePlayer();
        startDialogue(this,dialogueSet);

        dialogueSet++;
        if(dialogues[dialogueSet][0] == null)
        {
            //dialogueSet = 0;
            dialogueSet--; //displays last set
        }

        /*if(gp.player.life < gp.player.maxLife/3)
        {
            dialogueSet = 1;
        }*/
        //follow me, come with me  or something like that
        //onPath = true;
    }
}
