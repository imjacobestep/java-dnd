//Jacob Estep       Fall '18        CSC III
//Program #5
//this program is a version of dungeons and dragons, but with rooms and zombies
//I didn't get the save or load functions to work
//there were also occasional problems with items not showing up in the backpack and dropping items
//this has been a LONG program to work on, so I reached a point of moving on from that
//I'm just glad to have a working game
//the player goes from room to room, fighting zombies and picking up items
//the final boss room (the garden) is locked until the player picks up a key
package dnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class DND {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        JOptionPane.showMessageDialog(null, "Hi, I'm Jacob. Welcome to my version of Dungeons and Dragons!"
                + "\nThe game is pretty self explanatory, traverse through rooms to find a key. The key will unlock the boss room."
                + "\nJust one precaution, hitting fight in the first room crashes the game, so don't do it!");//

        File recent = new File("recent.txt");//creates recent saves file
        PrintWriter writer = new PrintWriter(recent, "UTF-8");//printwriter for recent saves file

        Dungeon dungeon = new Dungeon();//new dungeon
        Character player;//new player character

        ArrayList<String> mMenu = new ArrayList();//main menu options
        mMenu.add("Continue");//adds options
        mMenu.add("Load");
        mMenu.add("New");
        mMenu.add("Quit");
        boolean input1 = true;

        do {//basic while loop allows user to come bac to main menu later on
            int mainMenu = dungeon.showMenu("What would you like to do?", mMenu);//shows menu from dungeon class
            switch (mainMenu) {//switch based on response
                case 0://Continue
                    FileReader reader = new FileReader(recent);//reads from recents file
                    Scanner in = new Scanner(reader);
                    String fName = in.nextLine();

                    player = dungeon.loadSave(fName);//loads most recent save
                    input1 = false;//backs out of loop
                    break;
                case 1://Load
                    String loadFile = JOptionPane.showInputDialog("What is the name of the save? Please include extention.");//inputs filename

                    player = dungeon.loadSave(loadFile);//loads file

                    input1 = false;
                    break;
                case 2://New
                    String pName = JOptionPane.showInputDialog("What is your name");//new player name
                    ArrayList<String> classes = new ArrayList();//class selector options
                    classes.add("Warrior");//new player classes
                    classes.add("Healer");
                    classes.add("Bandit");
                    int pClass = dungeon.showMenu("What class are you?", classes);//shows class selector

                    if (pClass == 0) {
                        player = new Character(pName, "Warrior", 5, 10, 1);//sets player stats based on class chosen
                    } else if (pClass == 1) {
                        player = new Character(pName, "Healer", 2, 14, 1);
                    } else {
                        player = new Character(pName, "Bandit", 2, 10, 4);
                    }
                    input1 = false;
                    break;
                case 3://exit
                    player = null;
                    System.exit(0);//exit game
                    break;
                default:
                    player = null;//default player to get rid of "player may not have been initialized"
            }
        } while (input1);

        ArrayList<String> gMenu = new ArrayList();//game menu optoions
        gMenu.add("Backpack");//shows player backpack and stats
        gMenu.add("Chest");//player can take items out of chest
        gMenu.add("Fight");//fight mob
        gMenu.add("Move");//move to another room 
        gMenu.add("Quit");//quit with prompt to save
        boolean input2 = true;

        do {
            int gameMenu = dungeon.showMenu("What would you like to do?", gMenu);//show menu
            switch (gameMenu) {
                case 0://BACKPACK
                    ArrayList<String> bMenu = new ArrayList();//backpack options
                    bMenu.add("Back");
                    bMenu.add("Potions");
                    bMenu.add("Armor");
                    bMenu.add("Weapons");
                    boolean inputBack = true;

                    do {

                        int backMenu = dungeon.showMenu(player.name + "\nHP: " + player.hp + "\nAP: " + player.ap + "\nDefense: " + player.defense + "\n\n"
                                + "What item type would you like to view", bMenu);//shows stats and menu

                        switch (backMenu) {
                            case 0:
                                inputBack = false;//if user hits back
                                break;
                            case 1:
                                String potions = player.showItems("potion", "HP +");
                                JOptionPane.showMessageDialog(null, "Potions\n\n" + potions);//show potions

                                inputBack = true;
                                break;
                            case 2:
                                String armor = player.showItems("armor", "Defense +");//show armor
                                JOptionPane.showMessageDialog(null, "Armor\n\n" + armor);

                                inputBack = true;
                                break;
                            case 3:
                                String weapons = player.showItems("weapons", "AP +");//show weapons
                                JOptionPane.showMessageDialog(null, "Weapons\n\n" + weapons);
                                inputBack = true;
                                break;
                        }

                    } while (inputBack);

                    break;
                case 1://CHEST
                    Item temp = new Item();

                    boolean inputChest = true;

                    do {

                        ArrayList<Item> chest = dungeon.currentRoom.getItems();//get items from room
                        ArrayList<String> itemNames = new ArrayList();//chest menu options
                        itemNames.add("Back");//add back button
                        itemNames.add("Drop");//add drop button
                        for (int i = 0; i < chest.size(); i++) {//add item buttons
                            Item tempItem = chest.get(i);
                            itemNames.add(tempItem.name);
                        }

                        int chestMenu = dungeon.showMenu(player.name + "What would you like to do?", itemNames);//show menu

                        switch (chestMenu) {
                            case 0://back
                                inputChest = false;//break from loop
                                break;
                            case 1://drop item

                                ArrayList<Item> items = new ArrayList();

                                ArrayList<String> cbMenu = new ArrayList();//item types menu
                                cbMenu.add("Back");
                                cbMenu.add("Potions");//item types
                                cbMenu.add("Armor");
                                cbMenu.add("Weapons");
                                boolean inputCBack = true;

                                do {

                                    int dropMenu = dungeon.showMenu("What item type would you like to drop?", cbMenu);//item type menue

                                    switch (dropMenu) {
                                        case 0:
                                            inputCBack = false;//back button
                                            break;
                                        case 1:
                                            items = player.dropItems("potions");//if player wants to drop a potion
                                            ArrayList<String> poMenu = new ArrayList();
                                            poMenu.add("Back");//add back button

                                            for (int i = 1; i < chest.size(); i++) {
                                                temp = (Item) items.get(i);
                                                poMenu.add(temp.name);//adds potion buttons
                                            }
                                            boolean potionInput = true;

                                            int potMenu = dungeon.showMenu("Select a potion", poMenu);//shows menu of potions to drop

                                            do {

                                                switch (potMenu) {
                                                    case 0:
                                                        potionInput = false;
                                                        break;
                                                    case 1:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);//drops item into chest of current room
                                                        break;
                                                    case 2:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 3:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 4:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 5:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 6:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 7:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 8:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 9:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 10:
                                                        player.dropItem((Item) items.get(potMenu - 1), dungeon.currentRoom);
                                                        break;
                                                }

                                            } while (potionInput);

                                            inputBack = true;
                                            break;
                                        case 2:
                                            items = player.dropItems("armor");
                                            ArrayList<String> arMenu = new ArrayList();//same as above, but for armor
                                            arMenu.add("Back");

                                            for (int i = 1; i < chest.size(); i++) {
                                                temp = (Item) items.get(i);
                                                arMenu.add(temp.name);
                                            }
                                            boolean armorInput = true;

                                            int armorMenu = dungeon.showMenu("Select a potion", arMenu);

                                            do {

                                                switch (armorMenu) {
                                                    case 0:
                                                        armorInput = false;
                                                        break;
                                                    case 1:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 2:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 3:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 4:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 5:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 6:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 7:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 8:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 9:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 10:
                                                        player.dropItem((Item) items.get(armorMenu - 1), dungeon.currentRoom);
                                                        break;
                                                }

                                            } while (armorInput);

                                            inputBack = true;
                                            break;
                                        case 3:
                                            items = player.dropItems("Weapons");
                                            ArrayList<String> wMenu = new ArrayList();//same as above, but for weapons
                                            wMenu.add("Back");

                                            for (int i = 1; i < chest.size(); i++) {
                                                temp = (Item) items.get(i);
                                                wMenu.add(temp.name);
                                            }
                                            boolean weaponInput = true;

                                            int weaponMenu = dungeon.showMenu("Select a potion", wMenu);

                                            do {

                                                switch (weaponMenu) {
                                                    case 0:
                                                        weaponInput = false;
                                                        break;
                                                    case 1:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 2:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 3:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 4:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 5:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 6:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 7:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 8:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 9:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                    case 10:
                                                        player.dropItem((Item) items.get(weaponMenu - 1), dungeon.currentRoom);
                                                        break;
                                                }

                                            } while (weaponInput);

                                            break;
                                    }

                                } while (inputCBack);

                                inputBack = true;
                                break;//next cases are buttons for items in chest
                            case 2://item 1                                
                                player.addItem(chest.get(chestMenu - 2), dungeon.currentRoom, dungeon);
                                break;
                            case 3://item 2
                                player.addItem(chest.get(chestMenu - 2), dungeon.currentRoom, dungeon);
                                break;
                            case 4://item 3
                                player.addItem(chest.get(chestMenu - 2), dungeon.currentRoom, dungeon);
                                break;
                            case 5://item 4
                                player.addItem(chest.get(chestMenu - 2), dungeon.currentRoom, dungeon);
                                break;
                            case 6://item 5
                                player.addItem(chest.get(chestMenu - 2), dungeon.currentRoom, dungeon);
                                break;
                            case 7://item 6
                                player.addItem(chest.get(chestMenu - 2), dungeon.currentRoom, dungeon);
                                break;
                            case 8://item 7
                                player.addItem(chest.get(chestMenu - 2), dungeon.currentRoom, dungeon);
                                break;
                        }

                    } while (inputChest);

                    break;

                case 2://fight
                    Character enemy = dungeon.currentRoom.mob;//enemy is the mob in the room
                    String enemyIcon;
                    if (enemy.name.equalsIgnoreCase("necromancer")) {//sets enemy icon depending on enemy
                        enemyIcon = "ಠ_ಠ";
                    } else {
                        enemyIcon = "（▽д▽）";
                    }

                    String playerIcon = "(ง •̀_•́)ง";//sets player icon

                    while (player.hp > 0 && enemy.hp > 0) {//while player and enemy are alive

                        JOptionPane.showMessageDialog(null, player.name + "      vs      " + enemy.name + "\n\n" + playerIcon + "                " + enemyIcon + "\n\n\n\nHit!");//display fight

                        enemy.getsHitBy(player);//player hits first

                        if (enemy.hp <= 0) {//if enemy is dead
                            player.playerWon();//player wins
                        } else {
                            player.getsHitBy(enemy);//enemy hits player
                            if (player.hp <= 0) {//if player dies
                                JOptionPane.showMessageDialog(null, "You Died!");//tell player
                                System.exit(0);//exit game
                            }
                        }

                    }

                    break;
                case 3://move
                    ArrayList<Room> legal = dungeon.sendLegal();//gets list of legal rooms
                    int roomMenu = dungeon.showMenu("Select a room", dungeon.getNames(legal));//shows menu of legal rooms

                    if (roomMenu != 0) {//if choice isn't back
                        int num = legal.get(roomMenu - 1).number;//set number to that of the selected room
                        dungeon.goToRoom(num);//go to that room
                    }

                    break;
                case 4://quit
                    ArrayList<String> qMenu = new ArrayList();//save prompt options
                    qMenu.add("Yes");
                    qMenu.add("No");
                    boolean inputQuit = true;

                    do {

                        int backMenu = dungeon.showMenu("Would you like to save?", qMenu);//show save prompt

                        switch (backMenu) {
                            case 0://yes, save
                                String fName = JOptionPane.showInputDialog("Enter a file name (dont include .txt)");//take in file name
                                String recentS = dungeon.saveGame(fName, player);//save game
                                writer.println(recentS);//writes file name to recent saves
                                JOptionPane.showMessageDialog(null, "Remember, you can load this up easliy by hitting 'Continue' next time!");//remind about continue feature that doesn't work
                                inputQuit = false;
                                break;
                            case 1:
                                JOptionPane.showMessageDialog(null, "See you next time!");//exit message
                                inputQuit = false;//quit game
                                break;
                        }

                    } while (inputQuit);

                    input2 = false;

                    break;
            }

        } while (input2);

    }

}
