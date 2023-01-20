package dnd;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

public class Character implements java.io.Serializable {

    String name = "";//character name
    ArrayList<Item> backpack;//backpack for holding items
    String type = "";//player character type
    int ap = 0;//attack points
    int hp = 0;//health points
    int defense = 0;//defense points (maximum of random number chosen when hit)
    Random rand = new Random();//random number generator

    public Character(String n, String t, int a, int h, int d) {//creates character (used for player)

        name = n;//sets values to input values
        type = t;
        ap = a;
        hp = h;
        defense = d;
        backpack = new ArrayList();//instantiates new backpack
    }

    public Character(String n, int a, int h, int d) {//creates character (used for in game mobs)

        name = n;//sets value
        ap = a;
        hp = h;
        defense = d;//doesn't need type or backpack

    }

    public Character() {//creates character for temp use

    }

    public void modAP(int effect, int operator) {//modifies attack points with effect and operator

        if (operator == 0) {//if removing effect
            ap -= effect;
        } else {
            ap += effect;//if assing effect
        }

    }

    public void modHP(int effect, int operator) {//same as above for health points

        if (operator == 0) {
            hp -= effect;
        } else {
            hp += effect;
        }

    }

    public void modDEF(int effect, int operator) {//same as above for defense

        if (operator == 0) {
            defense -= effect;
        } else {
            defense += effect;
        }

    }

    public void playerWon() {//if player wins a fight

        this.modHP( 6, 1);//add 6 points to health

        JOptionPane.showMessageDialog(null, "You Won!");//display victory message

    }

    public void addItem(Item it, Room room, Dungeon dungeon){//move item from a chest to a player's backpack

        Item temp = new Item();//temp item for comparison
        Item item = it;//item input
        String itemType = item.type;//item type (potion, weapon, shield, armor)
        String itemName = item.name;//item name

        if (itemType.equalsIgnoreCase("armor") || itemType.equalsIgnoreCase("Weapon") || itemType.equalsIgnoreCase("shield")) {//if item is a weapon, set of armor, or shield

            for (int i = 0; i < backpack.size(); i++) {//compare to every item in backpack
                temp = (Item) backpack.get(i);
                if (temp.type.equals(itemType)) {
                    if (item.effect > temp.effect) {//if new item is stronger verson of old item, replace old item with new
                        temp.removeAffect(this);//remove old item effect
                        JOptionPane.showMessageDialog(null, temp.name + " is replaced by " + item.name);//tell user
                    }

                }
            }
            item.addAffect(this);//add new effect
        } else if (itemType.equalsIgnoreCase("potion") || itemType.equalsIgnoreCase("aPotion") || itemType.equalsIgnoreCase("dPotion")) {//if item is a potion of any kind

            item.addAffect(this);//add effect
            JOptionPane.showMessageDialog(null, itemName + " used");//tell user it was used

        }else if (itemType.equalsIgnoreCase("key")){//if item is the key
            
            dungeon.unlockGarden();//unlock the garden (final boss)
            JOptionPane.showMessageDialog(null, "You found a key! This unlocks the Garden");//tell user
            
        }        
        item.takenFrom(room.number);//add room number to item for reference when loading in save
        backpack.add(item);//add item to backpack
        room.removeItem(item);//remove item from room

    }

    public void dropItem(Item item, Room room) {//drop an item from backpack to chest

        room.addItem(item);//add item to chest
        backpack.remove(item);//remove from backpack

    }

    public ArrayList dropItems(String t) {//returns a list of items to drop, ready to use in a menu

        ArrayList<Item> ret = new ArrayList();//list for return value
        Item temp = new Item();//temp item for comparison
        for (int i = 0; i < backpack.size(); i++) {//compare all items

            temp = (Item) backpack.get(i);

            if (temp.type.equals(t)) {//get items of specified type

                ret.add(temp);//add to list to return

            }

        }

        return ret;

    }

    public String showItems(String t, String mod) {//shows items in backpack as a string

        String ret = "";
        Item temp = new Item();
        for (int i = 0; i < backpack.size(); i++) {//go through backpack

            temp = (Item) backpack.get(i);

            if (temp.type.equals(t)) {//if item is of specified type

                ret += temp.name + "\n" + mod + temp.effect + "\n\n";//add to text

            }

        }

        return ret;

    }

    public void getsHitBy(Character c1) {//if player gets hit by another

        int attack = c1.ap;//attack is hitter's attack points
        int def = rand.nextInt(defense);//a random number is generated between 0 and the defense value
        int result = Math.abs(attack - def);//gets net hit
        this.modHP(result, 0);//changes hittee's health points to reflect hit

    }

}
