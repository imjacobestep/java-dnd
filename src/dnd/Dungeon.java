package dnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Dungeon {

    //this is where I created all of the items, pretty repetitive
    Item potion = new Item("potion", "potion", 4, 0);
    Item highPotion = new Item("high potion", "potion", 8, 0);
    Item defensePotion = new Item("defense potion", "dPotion", 1, 0);
    Item attackPotion = new Item("attack potion", "aPotion", 1, 0);
    Item knife = new Item("knife", "weapon", 3, 0);
    Item dagger = new Item("dagger", "weapon", 5, 0);
    Item axe = new Item("axe", "weapon", 7, 0);
    Item sword = new Item("sword", "weapon", 10, 0);
    Item wShield = new Item("wooden shield", "shield", 1, 0);
    Item iShield = new Item("iron shield", "shield", 2, 0);
    Item sShield = new Item("steel shield", "shield", 3, 0);
    Item lArmor = new Item("leather armor", "armor", 1, 0);
    Item iArmor = new Item("iron armor", "armor", 3, 0);
    Item sArmor = new Item("steel armor", "armor", 5, 0);
    Item key = new Item("key", "key");
    Item pH = new Item(" ", " ", 0, 0);

    //this is an array of the items for reference
    Item[] items = {potion, highPotion, defensePotion, attackPotion, knife, dagger, axe, sword, wShield, iShield, sShield, lArmor, iArmor, sArmor, key};

    //this is where I created the mobs, named by the room they appear in
    Character zero = new Character("zombie housekeeper", 7, 12, 1);
    Character one = new Character("zombie guard", 10, 14, 8);
    Character two = new Character("waterlogged zombie", 8, 12, 2);
    Character three = new Character("necromancer", 14, 28, 12);
    Character four = new Character("zombie chef", 8, 10, 2);
    Character six = new Character("zombie housekeeper", 4, 10, 1);
    Character seven = new Character("zombie housekeeper", 6, 12, 8);
    Character eight = new Character("zombie nurse", 6, 20, 4);
    Character nine = new Character("zombie scholar", 10, 14, 10);

    //this is the array of rooms used for the Matrix
    //not sure if I did this right, I saw about 35 different ways online that didn't make any sense
    Room[] vertices = new Room[10];

    //creation of rooms, with mobs and items inside
    Room hall = new Room("Hall", zero, attackPotion, axe, iShield, pH, pH, 0);
    Room atrium = new Room("Atrium", one, potion, attackPotion, pH, pH, pH, 1);
    Room cistern = new Room("Cistern", two, highPotion, iArmor, pH, pH, pH, 2);
    Room garden = new Room("Garden", three, highPotion, defensePotion, sShield, sArmor, sword, 3);
    Room pantry = new Room("Pantry", four, dagger, pH, pH, pH, pH, 4);
    Room guardPost = new Room("Guard Post", null, knife, pH, pH, pH, pH, 5);
    Room sunroom = new Room("Sunroom", six, potion, potion, wShield, pH, pH, 6);
    Room cloakRoom = new Room("Cloak Room", seven, lArmor, pH, pH, pH, pH, 7);
    Room infirmary = new Room("Infirmary", eight, highPotion, defensePotion, pH, pH, pH, 8);
    Room study = new Room("Study", nine, key, pH, pH, pH, pH, 9);

    //this is the adjacency matrix, used to keep track of room doorways and determine legality of move
    int[][] edges = new int[][]{
        {0, 1, 0, 0, 0, 0, 1, 1, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
        {1, 0, 0, 0, 1, 1, 0, 0, 1, 0},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 1, 0}
    };

    //current room value, tells the room the player is currently in
    Room currentRoom = new Room();

    public String getCurrent() {//gets name of current room
        return currentRoom.name;
    }

    public Dungeon() {//creates the dungeon with rooms inside, sets default first room

        vertices[0] = hall;
        vertices[1] = atrium;
        vertices[2] = cistern;
        vertices[3] = garden;
        vertices[4] = pantry;
        vertices[5] = guardPost;
        vertices[6] = sunroom;
        vertices[7] = cloakRoom;
        vertices[8] = infirmary;
        vertices[9] = study;

        currentRoom = vertices[5];

    }

    public String saveGame(String fileName, Character player) throws FileNotFoundException, UnsupportedEncodingException {//saves game from file name and player character

        fileName += ".txt";//adds exension to input name
        File file = new File(fileName);//creates new file
        PrintWriter writer = new PrintWriter(file, "UTF-8");//I found some of this on stack overflow because I didn't remmeber all of it
        Item temp = new Item();//temp item
        writer.println(this.getCurrent());//prints current room
        writer.println(player.name);//prints player name
        writer.println(player.hp);//prints player health
        writer.println(player.ap);//prints player attack points
        writer.println(player.defense);//prints player defense
        for (int i = 0; i < player.backpack.size(); i++) {//for size of backpack
            temp = (Item) player.backpack.get(i);
            if ((i % 2) == 0) {//write item name and number of room it was taken from
                writer.println(temp.name);
            } else {
                writer.println(temp.room);
            }
        }
        writer.close();

        return fileName;

    }

    public Character loadSave(String fileName) throws FileNotFoundException {//loads save from file name
        //loading mainly works, but gets a couple of values mixed up

        FileReader reader = new FileReader(fileName);//file reader for save file
        Scanner in = new Scanner(reader);//scanner for file
        String[] bp = new String[21];//aray of item names to add to backack
        int[] rooms = new int[21];//arry of room numbers corresponding with items
        int count = 0;//count for iteration

        String current = in.nextLine();//determines saved current room
        String name = in.nextLine();//determines player name
        String hp = in.nextLine();
        String ap = in.nextLine();//determines player stats
        String defense = in.nextLine();
        while (in.hasNextLine()) {//sets next lines to values within arrays
            if ((count % 2) == 0) {//write item name and number of room it was taken from
                bp[count] = in.nextLine();
            } else {
                rooms[count] = Integer.parseInt(in.nextLine());
            }

            count++;
        }

        for (int i = 0; i < vertices.length; i++) {//searches for room by currents room's name

            if (vertices[i].name.equalsIgnoreCase(current)) {//finds it
                this.goToRoom(i);//sets to room of that index in vertices
            }
        }

        Character p = new Character(name, "Warrior", Integer.parseInt(hp), Integer.parseInt(ap), Integer.parseInt(defense));//creates character

        for (int i = 0; i < bp.length; i++) {//sets contents of backpack

            String temp = bp[i];
            int tempInt = rooms[i];
            for (int n = 0; n < items.length; n++) {//searches in items list for right ones

                if (items[n].name.equalsIgnoreCase(temp)) {//if name matches
                    p.addItem(items[n], vertices[tempInt], this);//adds to backpack and takes from the room it came out of
                }

            }

        }

        return p;//return character

    }

    public int showMenu(String prompt, ArrayList<String> c) {//shows menu from a prompt and arrayList of button options
        //I used this because my main program was already SO long, this made it more manageable

        String[] choices = new String[c.size()];//creates array

        for (int i = 0; i < c.size(); i++) {//sets values of input arrrayList to an array, useable by the menu
            choices[i] = (String) c.get(i);
        }

        int choice = JOptionPane.showOptionDialog(//sets button choice to the response variable
                null//centers in window
                ,
                 prompt//message
                ,
                 "Dungeons and Zombies"//titlebar text
                ,
                 JOptionPane.YES_NO_OPTION//option type
                ,
                 JOptionPane.PLAIN_MESSAGE//message type
                ,
                 null//no icon
                ,
                 choices//button text set to options above
                ,
                 choices[0]//sets default button
        );

        return choice;
    }

    public ArrayList sendLegal() {//send arrayList of legal rooms
        ArrayList<Room> ret = new ArrayList();

        for (int i = 0; i < edges.length; i++) {//for number of rooms
            if (edges[currentRoom.number][i] == 1) {//check if room connects with (i)room in adjacency matrix

                ret.add(vertices[i]);//if yes, add to list

            }
        }

        return ret;

    }

    public ArrayList getNames(ArrayList legal) {//gets an arrayList of names to be used for the move menu
        ArrayList<String> ret = new ArrayList();
        ret.add("Back");//adds back button

        for (int i = 0; i < legal.size(); i++) {//for number of legal rooms
            Room rName = (Room) legal.get(i);
            ret.add(rName.name);//add name to list
        }
        return ret;
    }

    public void goToRoom(int roomNum) {//moves current room to set one

        currentRoom = vertices[roomNum];

    }

    public void unlockGarden() {//unlocks garden if player picks up the key

        edges[0][3] = 1;//doorway from hall to garden
        edges[3][0] = 1;//doorway from garden to hall

    }

}
