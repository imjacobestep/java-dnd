package dnd;

import java.util.ArrayList;

public class Room {

    String name = "";//room name
    int number;//room number (quick way to get index)
    Character mob = new Character();//creates mob for room
    ArrayList<Item> chest;//arraylist of items held in room

    public Room(String n, Character inMob, Item a, Item b, Item c, Item d, Item e, int i) {//create room

        name = n;//set values to input ones
        number = i;
        mob = inMob;
        chest = new ArrayList();
        chest.add(a);//adds items into chest
        chest.add(b);
        chest.add(c);
        chest.add(d);
        chest.add(e);

    }

    public Room() {//room for temp 

    }

    public ArrayList getItems() {//returns all items

        return chest;

    }

    public ArrayList getNames() {//returns item names for display in chest
        ArrayList<String> ret = new ArrayList();
        Item temp = new Item();
        ret.add("Back");//add back button
        ret.add("Drop Item");//add drop button

        for (int i = 2; i < chest.size(); i++) {
            temp = (Item) chest.get(i);//adds item names
            ret.add(temp.name);
        }

        return chest;

    }
    
    public void removeItem(Item item) {//removes an item from a chest

        chest.remove(item);

    }

    public void addItem(Item item) {//adds an item to a chest

        chest.add(item);

    }

}
