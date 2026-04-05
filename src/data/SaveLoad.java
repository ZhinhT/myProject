package data;

import entity.Entity;
import main.GamePanel;
import object.*;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;

public class SaveLoad {

    GamePanel gp;

    public SaveLoad(GamePanel gp)
    {
        this.gp = gp;
    }

    public void save()
    {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
            DataStorage ds = new DataStorage();

            //PLAYER STATS
            ds.level = gp.player.level;
            ds.maxLife = gp.player.maxLife;
            ds.life = gp.player.life;
            ds.maxMana = gp.player.maxMana;
            ds.mana = gp.player.mana;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;

            //PLAYER INVENTORY
            for(int i = 0;i < gp.player.inventory.size(); i++)
            {
                ds.itemNames.add(gp.player.inventory.get(i).name);
                ds.itemAmounts.add(gp.player.inventory.get(i).amount);
            }

            //PLAYER EQUIPMENT
            ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
            ds.currentShieldSlot = gp.player.getCurrentShieldSlot();

            //OBJECTS ON MAP
            ds.mapObjectNames = new String[gp.maxMap][gp.obj[1].length]; //2nd dimension of obj array
            ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectOpened = new boolean[gp.maxMap][gp.obj[1].length];

            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++)
            {
                for(int i = 0; i < gp.obj[1].length; i++)
                {
                    if(gp.obj[mapNum][i] == null)
                    {
                        ds.mapObjectNames[mapNum][i] = "NA";
                    }
                    else
                    {
                        ds.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
                        ds.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
                        ds.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;
                        if(gp.obj[mapNum][i].loot != null)
                        {
                            ds.mapObjectLootNames[mapNum][i] = gp.obj[mapNum][i].loot.name;
                        }
                        ds.mapObjectOpened[mapNum][i] = gp.obj[mapNum][i].opened;
                    }
                }
            }

            //Write the DataStorage object
            oos.writeObject(ds);
            saveToDatabase(ds);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void load()
    {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));

            //Read the DataStorage object
            DataStorage ds =  (DataStorage)ois.readObject();

            //PLAYER STATS
            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxLife;
            gp.player.life = ds.life;
            gp.player.maxMana = ds.maxMana;
            gp.player.mana = ds.mana;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.coin = ds.coin;

            //PLAYER INVENTORY
            gp.player.inventory.clear();
            for(int i = 0; i < ds.itemNames.size(); i++)
            {
                gp.player.inventory.add(gp.eGenerator.getObject(ds.itemNames.get(i)));
                gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
            }

            //PLAYER EQUIPMENT
            gp.player.currentWeapon = gp.player.inventory.get(ds.currentWeaponSlot);
            gp.player.currentShield = gp.player.inventory.get(ds.currentShieldSlot);
            gp.player.getAttack();
            gp.player.getDefense();
            gp.player.getAttackImage();

            //OBJECTS ON MAP
            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++)
            {
                for(int i = 0; i < gp.obj[1].length; i++)
                {
                    if(ds.mapObjectNames[mapNum][i].equals("NA"))
                    {
                        gp.obj[mapNum][i] = null;
                    }
                    else
                    {
                        gp.obj[mapNum][i] = gp.eGenerator.getObject(ds.mapObjectNames[mapNum][i]);
                        gp.obj[mapNum][i].worldX = ds.mapObjectWorldX[mapNum][i];
                        gp.obj[mapNum][i].worldY = ds.mapObjectWorldY[mapNum][i];
                        if(ds.mapObjectLootNames[mapNum][i] != null)
                        {
                            gp.obj[mapNum][i].setLoot(gp.eGenerator.getObject(ds.mapObjectLootNames[mapNum][i]));
                        }
                        gp.obj[mapNum][i].opened = ds.mapObjectOpened[mapNum][i];
                        if(gp.obj[mapNum][i].opened == true)
                        {
                            gp.obj[mapNum][i].down1 = gp.obj[mapNum][i].image2;
                        }
                        gp.obj[mapNum][i].setDialogue(); // added this line
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("Load Exception!");
        }
    }
    public void saveToDatabase(DataStorage ds)
    {
        try
        {
            Connection conn = DatabaseManager.connect();

            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM inventory");
            stmt.executeUpdate("DELETE FROM equipment");
            stmt.executeUpdate("DELETE FROM item");
            stmt.executeUpdate("DELETE FROM player");

            // Reset auto increment
            stmt.executeUpdate("ALTER TABLE inventory AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE equipment AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE item AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE player AUTO_INCREMENT = 1");

            // PLAYER
            String playerSql = "INSERT INTO player(level, max_life, life, max_mana, mana, strength, dexterity, exp, next_level_exp, coin) VALUES (" +
                    ds.level + "," +
                    ds.maxLife + "," +
                    ds.life + "," +
                    ds.maxMana + "," +
                    ds.mana + "," +
                    ds.strength + "," +
                    ds.dexterity + "," +
                    ds.exp + "," +
                    ds.nextLevelExp + "," +
                    ds.coin + ")";

            stmt.executeUpdate(playerSql, Statement.RETURN_GENERATED_KEYS);

            int playerId = -1;

            var playerRs = stmt.getGeneratedKeys();
            if(playerRs.next())
            {
                playerId = playerRs.getInt(1);
            }

            // ITEM
            for(int i = 0; i < ds.itemNames.size(); i++)
            {
                String itemName = ds.itemNames.get(i);

                String itemSql = "INSERT INTO item(name) VALUES ('" + itemName + "')";
                stmt.executeUpdate(itemSql);
            }

            // INVENTORY
            for(int i = 0; i < ds.itemNames.size(); i++)
            {
                int itemId = i + 1;
                int amount = ds.itemAmounts.get(i);

                String inventorySql = "INSERT INTO inventory(player_id, item_id, quantity) VALUES (" +
                        playerId + ", " +
                        itemId + ", " +
                        amount + ")";

                stmt.executeUpdate(inventorySql);
            }

            // EQUIPMENT
            int weaponItemId = ds.currentWeaponSlot + 1;
            int shieldItemId = ds.currentShieldSlot + 1;

            String weaponSql = "INSERT INTO equipment(player_id, item_id, slot) VALUES (" +
                    playerId + ", " +
                    weaponItemId + ", 'weapon')";

            stmt.executeUpdate(weaponSql);

            String shieldSql = "INSERT INTO equipment(player_id, item_id, slot) VALUES (" +
                    playerId + ", " +
                    shieldItemId + ", 'shield')";

            stmt.executeUpdate(shieldSql);

            conn.close();

            System.out.println("Database saved!");
        }
        catch(Exception e)
        {
            System.out.println("Database Save Error");
            e.printStackTrace();
        }
    }

}
